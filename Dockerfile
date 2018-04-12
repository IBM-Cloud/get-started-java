# For reference only

FROM websphere-liberty
ADD target/GetStartedJava.war /opt/ibm/wlp/usr/servers/defaultServer/dropins/
ENV LICENSE accept
EXPOSE 9080

## Running the container locally
# mvn clean install
# docker build -t getstartedjava:latest .
# docker run -d --name myjavacontainer getstartedjava
# docker run -p 9080:9080 --name myjavacontainer getstartedjava
# Visit http://localhost:9080/GetStartedJava/

## Push container to IBM Cloud
# Install cli and dependencies: https://console.ng.bluemix.net/docs/containers/container_cli_cfic_install.html#container_cli_cfic_install
# docker tag getstartedjava:latest registry.ng.bluemix.net/<my_namespace>/getstartedjava:latest
# docker push registry.ng.bluemix.net/<my_namespace>/getstartedjava:latest
# bx ic images # Verify new image
