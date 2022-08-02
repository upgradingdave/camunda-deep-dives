# Install Camunda with nginx ingress on AWS

<img src="https://img.shields.io/badge/Tutorial%20Reference%20Project-Tutorials%20for%20getting%20started%20with%20Camunda-%2338A3E1" alt="A blue badge that reads: 'Tutorial Reference Project - Tutorials for getting started with Camunda'">

The following is a writeup of how to install Camunda with a nginx ingress on AWS. 

## Create AWS Cluster

You should already have an AWS cluster, but if not, use `eksctl` to create one like so. 
Feel free to customize `cluster.yaml` as needed:  

       eksctl create cluster -f cluster.yaml

 ## Setup OIDC

(I'm not 100% sure this step is required?)

       eksctl utils associate-iam-oidc-provider --cluster camunda-greenfield --approve

## Setup nginx Ingress Controller

       kubectl create namespace ingress-nginx
       helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
       helm repo update ingress-nginx
       helm search repo ingress-nginx
       helm install ingress-nginx ingress-nginx/ingress-nginx --namespace ingress-nginx

## Configure Camunda to use dns host name routing

> :information_source: **Note** Path based routing, for example, http://(domain name)/operate, is not yet supported. We 
> need to use dns host name based routing instead, for example: http://operate.(domain name).

There are probably several ways to set this up. Here's how I set it up for this proof of concept. 

First, we need to find the IP Address of the nginx ingress Load Balancer created in the previous step. Run the following 
command: 


     kubectl get service -n ingress-nginx
     NAME                                 TYPE           CLUSTER-IP      EXTERNAL-IP                                                              PORT(S)                      AGE
     ingress-nginx-controller             LoadBalancer   10.100.66.193   a6e4157656634474fb0c4480dd894683-683984428.us-east-1.elb.amazonaws.com   80:30828/TCP,443:30157/TCP   112m

In this example, the dns host name is: `a6e4157656634474fb0c4480dd894683-683984428.us-east-1.elb.amazonaws.com`. 

Because I used 2 availability zones in my `cluster.yaml`, there are 2 external ip addresses associated with this dns name. I found the ip addresses like so: 

     nslookup a6e4157656634474fb0c4480dd894683-683984428.us-east-1.elb.amazonaws.com
     ...
     Address: 44.196.82.237
     ...
     Address: 54.221.146.146

If `nslookup` is not available, `dig` should work as well:

    dig +short a6e4157656634474fb0c4480dd894683-683984428.us-east-1.elb.amazonaws.com
    54.221.146.146
    44.196.82.237

Choose one of these ip addresss. Edit the `camunda-values.yaml` file located
in this directory and replace all occurrences of `127.0.0.1` with the ip address you chose.

This uses a service called [nip.io](https://nip.io/). It's a convenient way to map ip address to a domain name. 

## Use Helm to install camunda

     helm repo add camunda https://helm.camunda.io
     helm repo update camunda
     helm search repo camunda/camunda-platform
     helm install --namespace camunda camunda camunda/camunda-platform -f camunda-values.yaml --skip-crds

Wait for services and pods to startup. Once everything has started, you should be able to access the various components
at the following urls: 

     http://identity.<your ip address>.nip.io
     http://keycloak.<your ip address>.nip.io
     http://operate.<your ip address>.nip.io
     http://tasklist.<your ip address>.nip.io
     http://optimize.<your ip address>.nip.io

These domain names should resolve to the ip address you chose earlier. The nginx ingress rules looks for these host
names to know where to send traffic to the backend kubernetes services. 

## Next steps

Of course, you'll most likely want to set up your own domain name and subdomains for each application. You'll also most likely
want to setup ssl certificates and change the camunda-values to use https.
