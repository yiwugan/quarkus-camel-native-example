oc delete all --selector app=quarkus-camel-native-example
oc delete secret ecr-docker-secret
oc delete servicemonitor quarkus-camel-native-example

