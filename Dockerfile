FROM websphere-liberty:microProfile
COPY server.xml /config/
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
# docker tag getstartedjava:latest registry.ng.bluemix.net/<my_namespace>/getstartedjava:latest
# docker push registry.ng.bluemix.net/<my_namespace>/getstartedjava:latest
# ibmcloud ic images # Verify new image
