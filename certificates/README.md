[![Community Extension](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
[![](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)

# Camunda 8 and TLS Certificates

Often teams use tls certificates that have been signed by their own internal Certificate Authorities (CA's) which are not commonly known by operating systems and browsers. 

This folder includes notes on how to configure non-standard CA certificates to allow clients to connect to the Camunda 8 Zeebe gateway

# Concepts

Servers use public and private keys in order to encrypt data. For example, `Transport Layer Security`, or `tls` can be used to encrypt http data. Web servers typically listen on port 443 (aka `https`) for encrypted http requests. 

Clients need to discover the server's public key. Public keys can be found in certificates. A certificate can be formatted in different ways, but basically, it's a plain text file that contains a bit of metadata along with the actual binary contents of a public or private key. 

A client should not blindly trust a server's public key unless they were issued from a trusted Certificate Authority. There are a number of Certificate Authorities that are trusted by default by most Operating Systems. However, often, large companies will maintain their own Certificate Authority.

Each Certificate Authority also has a public and private key. There can also be intermediary Certificate Authorities which also have their own publich and private keys. 

In order for a client (such as your browser, or a java program) to establish a tls connection over port 443 (aka `https`), it's first necessary to tell the client that it's ok to trust the CA that issued the public key of the server. A client can read the public certificate of a server to figure out which CA's issued the server's certificates.

# Obtaining a Certificate Authority Certificate (CA cert)

Open your browser and browse to a site over port 443 (aka `https`). In other words, any site that begins with `https`. Click near the location bar in the browser to check if the certificate is valid.

Then click to view certificate details. 

Click the `Details` view to show the CA chain

Most browsers allow you to click on the top level (root CA cert) and choose Export

There are several formats that you can export. The most common are `.cer` and `.pem`. 

For this example, I exported using `.cer`. 

You can use `openssl` tool to convert certificates to different formats if needed. 

# Create a Java Truststore

The java `keytool` can be used to create a `Java Trust Store` and then add `CA certificates` to the `Trust Store`.

First, you need to obtain the CA certificate.

In order to communicate over a http connection which as been encrypted by tls, clients (such as your web browser, or `openssl`, or java programs) need to be able to establish trust with the server.

```shell
cd certs
```
```shell
keytool -import -keystore keystore.jks -storepass changeit -noprompt -file pretendpear.cer -alias pretendpear
```
```shell
keytool -list -keystore keystore.jks
```

Configure the jvm to use the truststore. There are many ways to do this!

Add as arguments to the java cli: 

```shell
-Djavax.net.ssl.trustStore=./certs/letsencrypt -Djavax.net.ssl.trustStorePassword=changeit
```

Use JAVA_TOOL_OPTIONS environment variable: 
```shell
EXPORT JAVA_TOOL_OPTIONS=-Djavax.net.ssl.trustStore=./certs/keystore.jks -Djavax.net.ssl.trustStorePassword=changeit
```

# Web Modeler

Shell into pod
```shell
kubectl exec --stdin --tty camunda-web-modeler-restapi-868c74d774-7crnz -- /bin/sh
cd /usr/local/certificates
keytool -list -keystore keystore.jks
```
