export ECR_PASSWORD=$(aws ecr get-login-password)

oc create secret docker-registry ecr-docker-secret --docker-server=aws-accountid.dkr.ecr.ca-central-1.amazonaws.com --docker-username=AWS --docker-password=$ECR_PASSWORD --docker-email=none

oc secrets link default ecr-docker-secret --for=pull

oc secrets link builder ecr-docker-secret 



