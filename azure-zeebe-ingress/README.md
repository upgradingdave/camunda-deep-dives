# Access Zeebe over Azure Kubernetes Ingress

<img src="https://img.shields.io/badge/Tutorial%20Reference%20Project-Tutorials%20for%20getting%20started%20with%20Camunda-%2338A3E1" alt="A blue badge that reads: 'Tutorial Reference Project - Tutorials for getting started with Camunda'">

Usually (especially from a security perspective) it's best to limit the ports and services exposed from a Kubernetes 
Cluster as much as possible. But, sometimes it might be convenient (and even necessary) to access Zeebe Gateway from 
outside your Kubernetes Cluster.

The following is a write up of how I created an Azure Kubernetes Camunda Cluster from scratch. And then I describe how 
it's possible to open network traffic communication between Zeebe Brokers and an outside network.

Note that the "outside network" can either be the public internet, or it could also refer to a private virtual network.

Also, because Camunda is often an integral part of a larger network, sometimes it can be difficult to understand where
Camunda ends and the larger network begins. So, just to be clear: nothing I discuss here is specific to Camunda. This 
task of opening the Zeebe port to a network is very similar to opening a network port to a database, or to an
email server. 

Ok, now that all that boring stuff is out of the way(!) here are 2 strategies to open access to your zeebe brokers: 

1. [Configure a Nginx Ingress](#create-a-nginx-ingress-controller) 

This is convenient for when you want to open traffic to the public internet over https/tls on port 443.  

Note that this uses a Layer 7 load balancer. Layer 7 provides more control over routing, however due to the 
implementation of Nginx support for grpc, this option requires setting up tls certificates. In other words, the 
connection must be made over tls in order for the nginx ingress to allow grpc communication.

2. [Kubernetes Load Balancer Service](#create-kubernetes-load-balancer-service)

This is probably more convenient for opening backend communication between networks. 

This solution uses networking Layer 4. This option gives less control over routing, but it's a direct connection over 
TCP which, in some cases, might be more convenient than having to go thru nginx.

# Create an Azure Account

If you don't already have one, sign up for a new Azure Account. At the time of this writing, you can sign up for a free
account with a $200 free credit. 

> :information_source: **Note** I couldn't start a cluster on the free tier with the command describe below. 
> I had to upgrade to a paid account. 
> But, this was probably because of my ignorance. I'm guessing that someone more familiar with Azure will be able to 
> get this to work within the bounds of the free account. 

## Setup Azure Command Line Tool

If you haven't already, make sure to install the `az` cli tool as described here: 

https://docs.microsoft.com/en-us/cli/azure/install-azure-cli

After installing and configuring the Azure command line tool (`az`), you should be able to run the following command 
and see output similar to the following: 

     $ az version
     {
       "azure-cli": "2.38.0",
       "azure-cli-core": "2.38.0",
       "azure-cli-telemetry": "1.0.6",
       "extensions": {}
     }

The next step is to make sure you are authenticated. Run the following and then follow the instructions to authenticate
via your browser. 

     $ az login

> :information_source: **Tip** If you're using SSO, first, open a browser and sign in to your Azure/Microsoft account. 
> Then try doing the `az login` command again. 

##  Create a Resource Group

Resource groups are useful to keep track of all the objects that Azure will create. Create a specific resource group for 
this project. I called mine "dave-group". Also choose your location. I chose "eastus2" because it seemed that this location had 
the machine types that I wanted to use. 

     $ az group create --name dave-group --location eastus2

## Machine Types

Speaking of machine types, here's a command that lists all machine types for a given location. Try different searches 
for `size`. 

> :information_source: **Heads up** Another stumbling point for me (again, mostly because of my ignorance of Azure) was 
> choosing a machine type that worked for my location. I tried a few different machine types that didn't work because 
> they weren't available in the `eastus2` location. The following command helped me find which machine types to use.  

    $ az vm list-skus --location useast2 --size Standard_B --all --output table

I settled on using `StandardB2s` for this experiment. These machines each have 2 vCPUs and 4 GiB of memory.

## Create a Cluster

The following creates a cluster that will auto scale between 1-5 
nodes. Note that I had to use a different name for the `node-resource-group`

    $ az aks create -g dave-group --node-resource-group dave-node-group \
         -n dave-cluster-01 --enable-cluster-autoscaler --min-count 1 --max-count 5 \
         --node-vm-size Standard_B2s

## Connect using kubectl

Once you have the group and cluster created, run the following to configure your local `kubectl` environment. In other 
words, runing this command tells `kubectl` how to connect to your new Azure Kubernetes cluster. 

    az aks get-credentials --resource-group dave-group --name dave-cluster-01

Test things out using this command

    kubectl cluster-info

If everything is happy so far, you should see something like this:  

```
Kubernetes control plane is running at https://dave-clust-dave-group-9ac003-1adf4442.hcp.eastus2.azmk8s.io:443
CoreDNS is running at https://dave-clust-dave-group-9ac003-1adf4442.hcp.eastus2.azmk8s.io:443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
Metrics-server is running at https://dave-clust-dave-group-9ac003-1adf4442.hcp.eastus2.azmk8s.io:443/api/v1/namespaces/kube-system/services/https:metrics-server:/proxy
```

## Install Camunda via Helm

Open a terminal and `cd` into the same directory (`azure-zeebe-ingress`) as this `README.md`. 

Run the following commands to install Camunda components using helm. The `camunda-values.yaml` file inside this same 
directory provides configuration for a simple environment for this proof of concept.

    kubectl create namespace camunda
    helm install --namespace camunda camunda8 camunda/camunda-platform -f camunda-values.yaml

Wait for the containers to startup. If all goes well, the zeebe brokers, operate and tasklist pods 
should all be started.

At this point, I like to check to make sure Camunda services are up. Since we haven't setup any ingress yet, there's no 
way to access the services. Use the following command to temporarily open a port on our localhost network so we can
check on things: 

     kubectl port-forward svc/camunda8-operate  8081:80 --namespace camunda

Now, you should be able to see operate up and running here: 

    http://localhost:8081/operate

If that comes up, congrats! You're getting close!

Now you have 2 options (actually, to be precise, there are many, many options, but here are at least 2):

- [Create a nginx ingress controller](#create-a-nginx-ingress-controller)
- [Create a load balancer type service](#create-kubernetes-load-balancer-service)

# Create a nginx ingress controller

Again, this option is very convenient for opening connection to a public ip address. This option requires you to setup a custom dns name and valid tls 
certificate. Another option, that doesn't require certificates is to use a Kubernetes Load Balance Service [describe here](#create-kubernetes-load-balancer-service).

Here's the command I used to create an ingress controller. Note that I created my ingress controller inside a namespace
called `ingress-basic`. See this link for more info: https://docs.microsoft.com/en-us/azure/aks/ingress-basic?tabs=azure-cli#create-an-ingress-controller

     helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx

     helm install nginx-ingress ingress-nginx/ingress-nginx \
       --version 4.1.3 \
       --namespace ingress-basic \
       --create-namespace \
       --set controller.replicaCount=1 \
       --set controller.service.annotations."service\.beta\.kubernetes\.io/azure-load-balancer-health-probe-request-path"=/healthz

## Configure a Static IP Address

In order to configure nginx ingress to proxy gRPC traffic, we need to configure TLS Certificates. And in order to 
configure TLS certificates, we need a domain name. And in order for a domain name, it's nice to have a static ip. 

    az network public-ip create --resource-group dave-node-group \
       --name dave-public-ip-01 \
       --sku Standard \
       --allocation-method static \
       --query publicIp.ipAddress -o tsv

If that command runs successfully, it should give you an ip address. Don't forget to copy it! For this time, my ip was:

20.230.108.198

## Update ingress controller with IP and DNS

Now we can set up dns associated with the ip address above. Feel free to choose whatever "dns label" you want. Don't forget
to update this to use your specific ip address. 

    helm upgrade nginx-ingress ingress-nginx/ingress-nginx \
      --namespace ingress-basic \
      --set controller.service.annotations."service\.beta\.kubernetes\.io/azure-dns-label-name"=camundadave \
      --set controller.service.loadBalancerIP=20.230.108.198

At this point, you should be able to point your browser to your dns name. You'll see a browser warning that the certificate is invalid, so not very exciting, 
but, at least you should be able to connect ;-)  

https://camundadave.eastus2.cloudapp.azure.com/

## Setup a Certificate Manager

This step creates a Kubernetes Certificate Manager using helm. See more details here: https://cert-manager.io/docs/configuration/

If you've ever had to deal with manually creating and configuring ssl certificates, 
I think you'll find this makes managing certificates very easy!

     kubectl label namespace ingress-basic cert-manager.io/disable-validation=true
     helm repo add jetstack https://charts.jetstack.io
     helm repo update

     helm install cert-manager jetstack/cert-manager \
       --namespace ingress-basic \
       --set installCRDs=true \
       --set nodeSelector."kubernetes\.io/os"=linux

## Use Let's Encrypt Cluster Issuer

We'll use Let's Encrypt to issue ssl certificates. For that, we need to install a Kubernetes Cluster Issuer. 

Edit `azure-cluster-issuer.yaml` file found in this `azure-zeebe-ingress` directory and update with your own email address. 

Then open a terminal, make sure to `cd` into this same directory and run the following command. 

    kubectl apply -f azure-cluster-issuer.yaml 

## Create Ingress Rules for Tasklist and Operate

Open the `camunda-ingress.yaml` file in this directory and update the two `host` values to match your specific dns. 
Then, `cd` into this same directory and run the following command: 

     kubectl apply -f camunda-ingress.yaml

If everything is happy, you should be able to access operate at your domain name like this:

https://camundadave.eastus2.cloudapp.azure.com/operate/login

And you should be able to access tasklist at your domain name like this:

https://camundadave.eastus2.cloudapp.azure.com/tasklist/login

This is the "magic" line that makes this all work: 

    cert-manager.io/cluster-issuer: letsencrypt

If everything is configured correctly, the Let's Encrypt Cluster Issuer should recognize that we just created new
ingress rules. It automatically creates corresponding ssl certificates for your domain name. 

## Create Ingress Rules for Zeebe 

We can finally (!) setup ingress rules for Zeebe over gRPC. 

Edit the `zeebe-ingress.yaml` file in this same directory. Again, change the `host` values to match your dns name. 
Then, run the following command. 

     kubectl apply -f zeebe-ingress.yaml

Now you should be able to connect over gRPC on port 26500. Use `zbctl` to confirm that it's working:

    zbctl --address camundadave.eastus2.cloudapp.azure.com:443 status

```
Cluster size: 1
Partitions count: 1
Replication factor: 1
Gateway version: 8.0.4
Brokers:
Broker 0 - camunda8-zeebe-0.camunda8-zeebe.camunda.svc:26501
Version: 8.0.4
Partition 1 : Leader, Healthy
```

You can even try deploying a process if you'd like:

    zbctl --address camundadave.eastus2.cloudapp.azure.com:443 deploy camunda-ingress.bpmn

# Create Kubernetes Load Balancer Service

This option establishes a load balancer that manages traffic over TCP. This can be a great option 
for internal communication between different networks. 

By this point, I assume you have a Azure Kubernetes cluster running a Camunda environment. Your Camunda Kubernetes 
cluster should contain a service called `camunda-zeebe-gateway`. For example, it will look something like this:

    $ kubectl get service camunda-zeebe-gateway --namespace camunda

    NAME                    TYPE        CLUSTER-IP    EXTERNAL-IP   PORT(S)              AGE
    camunda-zeebe-gateway   ClusterIP   10.0.63.140   <none>        9600/TCP,26500/TCP   3h1m

Notice this service only has a Cluster-IP. In other words, only Nodes in the cluster can communicate with this service. 

We can create another Kubernetes Service of type "Load Balancer" to route traffic between the Kubernetes Cluster and an
outside network. Again, this "outside" network can be the public internet or a virtual private network. This depends on 
how you configure your Azure Load balancer. 

Run the following: 

     $ kubectl apply -f ./zeebe-lb.yaml

This creates a new Kubenetes Service.  

    kubectl get service zeebe-lb --namespace camunda

    NAME       TYPE           CLUSTER-IP     EXTERNAL-IP     PORT(S)           AGE
    zeebe-lb   LoadBalancer   10.0.103.248   20.237.59.201   26500:32314/TCP   11m

Now you should be able to connect over gRPC on port 26500 to the `EXTERNAL-IP`. Use `zbctl` to confirm that it's
working:

    $ zbctl --address 20.237.59.201:26500 --insecure status

You can even try deploying a process if you'd like:

    zbctl --address 20.237.59.201:26500 --insecure deploy camunda-ingress.bpmn

A few notes: 

- Notice that we now have an "external ip". This is because, behind the scenes, Azure will create a corresponding 
Azure Load Balance Object that connects this external ip address to the kubernetes service loadbalancer. 

- Note that if you run this command from an internal network (such as an on premise Azure environment), the "external 
ip address" most likely will not be completely publicly available.

- Note that this connection is not `tls` enabled. However, configuring this ip address to be encrypted by tls should be 
  no different then configuring a web server's ip address with a tls certificate.

# Cleaning up

Don't forget to clean things up so that you're not charged!

    az aks delete -g dave-group -n dave-cluster-01
    az group delete --resource-group dave-group



