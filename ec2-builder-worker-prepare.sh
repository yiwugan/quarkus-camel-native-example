sudo yum install java-17-amazon-corretto-headless -y
sudo yum install git -y

sudo yum install docker -y
sudo systemctl enable docker
sudo systemctl daemon-reload
echo restarting docker
sudo systemctl start docker
sudo chmod 666 /var/run/docker.sock

wget https://dlcdn.apache.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz
sudo tar -xvzf ~/apache-maven-3.8.8-bin.tar.gz
sudo mv apache-maven-3.8.8 /opt
sudo ln -s /opt/apache-maven-3.8.8 /opt/maven
sudo ln -s /opt/maven/bin/mvn /usr/local/bin/mvn

wget https://downloads-openshift-console.apps.sandbox-m2.ll9k.p1.openshiftapps.com/amd64/linux/oc.tar
tar -xf oc.tar
sudo mv oc /bin
