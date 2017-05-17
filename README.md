# Getting started with Liberty on Bluemix
By following this guide, you'll set up a development environment, deploy an app locally and on Bluemix, and integrate a Bluemix database service in your app.

## Prerequisites

You'll need [Git](https://git-scm.com/downloads), [Cloud Foundry CLI](https://github.com/cloudfoundry/cli#downloads), [Maven](https://maven.apache.org/download.cgi) and a [Bluemix account](https://console.ng.bluemix.net/registration/),

## 1. Clone the sample app

Now you're ready to start working with the app. Clone the repo and change the directory to where the sample app is located.
  ```bash
  git clone -b autowiring --single-branch https://github.com/IBM-Bluemix/get-started-java
  cd get-started-java
  ```

## 2. Run the app locally using command line

Use Maven to install dependencies and build the .war file.

  ```
  mvn clean install
  ```

Run the app locally on Liberty.
  ```
  mvn install liberty:run-server
  ```

View your app at: http://localhost:9080/cloudant

You should see an error similar to:
```
Error 404: javax.servlet.UnavailableException: SRVE0203E: Servlet [com.test.CloudantTestServlet]: com.test.CloudantTestServlet was found, but is missing another required class. SRVE0206E: This error typically implies that the servlet was originally compiled with classes which cannot be located by the server. SRVE0187E: Check your class path to ensure that all classes required by the servlet are present.SRVE0210I: This problem can be debugged by recompiling the servlet using only the classes in the application's runtime class path SRVE0234I: Application class path=[com.ibm.ws.classloading.internal.ThreadContextClassLoader@43ee35c]
```

This is normal as the database is not autowired locally it is only autowired to your database in Bluemix when you bind the service to your application.

## 3. Deploy to Bluemix using command line

To deploy to Bluemix using command line, it can be helpful to set up a manifest.yml file. The manifest.yml includes basic information about your app, such as the name, the location of your app, how much memory to allocate for each instance, and how many instances to create on startup. This is also where you'll choose your URL. [Learn more...](https://console.bluemix.net/docs/manageapps/depapps.html#appmanifest)

The manifest.yml is provided in the sample.

  ```
  applications:
  - path: target/GetStartedJava-1.0-CLOUDANT.war
    memory: 512M
    instances: 1
    name: your-appname-here
    host: your-appname-here
  ```

Change both the *name* and *host* to a single unique name of your choice. Note that the *host* value will be used in your public url, for example, http://your-appname-here.mybluemix.net. If you already created an app from the Bluemix UI but haven't pushed your code to it, you can use the same name value. Make sure the path points to the built application, for this example the location is `target/JavaHelloWorldApp.war`.

Choose your API endpoint
   ```
   cf api <API-endpoint>
   ```

Replace the *API-endpoint* in the command with an API endpoint from the following list.
* https://api.ng.bluemix.net # US South
* https://api.eu-gb.bluemix.net # United Kingdom
* https://api.au-syd.bluemix.net # Sydney

Login to your Bluemix account
  ```
  cf login
  ```

Push your application to Bluemix.
  ```
  cf push
  ```

This can take around two minutes. If there is an error in the deployment process you can use the command `cf logs <Your-App-Name> --recent` to troubleshoot.

## 4. Add a database

Next, we'll add a NoSQL database to this application and set up the application so that it can run locally and on Bluemix.

1. Log in to Bluemix in your Browser. Select your application and click on `Connect new` under `Connections`.
2. Select `Cloudant NoSQL DB` and Create the service.
3. Select `Restage` when prompted. Bluemix will restart your application and use autowiring to provide the database credentials to your application.

## 5. Test your application on Bluemix

1. Go in your browser to:
  ```
  http://getstartedjava-yourRoute.mybluemix.net/cloudant
  ```

2. You should see the test pass:
  ```
  Added a new doc: { id: example_id, rev: null, date: "Wed May 17 17:50:45 UTC 2017"}
  Test passed.
  ```
