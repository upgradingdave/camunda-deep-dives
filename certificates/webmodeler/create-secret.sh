kubectl create secret generic "custom-keystore" \
--namespace=camunda \
--from-file=keystore.jks=keystore.jks