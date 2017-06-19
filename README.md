# Getting started with Liberty on Bluemix
By following this guide, you'll set up a development environment, deploy an app locally and on Bluemix, and integrate a Bluemix database service in your app.

<p align="center">
  <kbd>
    <img src="docs/GettingStarted.gif" width="300" style="1px solid">
  </kbd>
</p>

## Prerequisites

You'll need [Git](https://git-scm.com/downloads), [Cloud Foundry CLI](https://github.com/cloudfoundry/cli#downloads), [Maven](https://maven.apache.org/download.cgi) and a [Bluemix account](https://console.ng.bluemix.net/registration/),

## 1. Clone the sample app

Now you're ready to start working with the app. Clone the repo and change the directory to where the sample app is located.
  ```bash
  git clone https://github.com/IBM-Bluemix/get-started-java
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

View your app at: http://localhost:9080/GetStartedJava


## 3. Deploy to Bluemix using command line

To deploy to Bluemix using command line, it can be helpful to set up a manifest.yml file. The manifest.yml includes basic information about your app, such as the name, the location of your app, how much memory to allocate for each instance, and how many instances to create on startup. This is also where you'll choose your URL. [Learn more...](https://console.bluemix.net/docs/manageapps/depapps.html#appmanifest)

The manifest.yml is provided in the sample.

  ```
  applications:
  - path: target/GetStartedJava.war
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
* https://api.eu-de.bluemix.net # Germany
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

## 4. Developing and Deploying using Eclipse

IBM® Eclipse Tools for Bluemix provides plug-ins that can be installed into an existing Eclipse environment to assist in integrating the developer's integrated development environment (IDE) with Bluemix.

1. Download and install  [IBM Eclipse Tools for Bluemix](https://developer.ibm.com/wasdev/downloads/#asset/tools-IBM_Eclipse_Tools_for_Bluemix).

2. Import this sample into Eclipse using `File` -> `Import` -> `Maven` -> `Existing Maven Projects` option.

3. Create a Liberty server definition:
  - In the `Servers` view right-click -> `New` -> `Server`
  - Select `IBM` -> `WebSphere Application Server Liberty`
  - Choose `Install from an archive or a repository`
  - Enter a destination path (/Users/username/liberty)
  - Choose `WAS Liberty with Java EE 7 Web Profile`
  - Continue the wizard with default options to Finish

4. Run your application locally on Liberty:
  - Right click on the `GetStartedJava` sample and select `Run As` -> `Run on Server` option
  - Find and select the localhost Liberty server and press `Finish`
  - In a few seconds, your application should be running at http://localhost:9080/GetStartedJava/

5. Create a Bluemix server definition:
  - In the `Servers` view, right-click -> `New` -> `Server`
  - Select `IBM` -> `IBM Bluemix` and follow the steps in the wizard.\
  - Enter your credentials and click `Next`
  - Select your `org` and `space` and click `Finish`

6. Run your application on Bluemix:
  - Right click on the `GetStartedJava` sample and select `Run As` -> `Run on Server` option
  - Find and select the `IBM Bluemix` and press `Finish`
  - A wizard will guide you with the deployment options. Be sure to choose a unique `Name` for your application
  - In a few minutes, your application should be running at the URL you chose.

Now you have your code running locally and on the cloud!

The `IBM Eclipse Tools for Bluemix` provides many powerful features such as incremental updates, remote debugging, pushing packaged servers, etc. [Learn more](https://console.ng.bluemix.net/docs/manageapps/eclipsetools/eclipsetools.html#eclipsetools)


## 5. Add a database

Next, we'll add a NoSQL database to this application and set up the application so that it can run locally and on Bluemix.

1. Log in to Bluemix in your Browser. Select your application and click on `Connect new` under `Connections`.
2. Select `Cloudant NoSQL DB` and Create the service.
3. Select `Restage` when prompted. Bluemix will restart your application and provide the database credentials to your application using the `VCAP_SERVICES` environment variable. This environment variable is only available to the application when it is running on Bluemix.

## 6. Use the database

We're now going to update your local code to point to this database. We'll store the credentials for the services in a properties file. This file will get used ONLY when the application is running locally. When running in Bluemix, the credentials will be read from the VCAP_SERVICES environment variable.

1. In Eclipse, open the file src/main/resources/cloudant.properties:
  ```
  cloudant_url=
  ```

2. In your browser open the Bluemix UI, select your App -> Connections -> Cloudant -> View Credentials

3. Copy and paste just the `url` from the credentials to the `url` field of the `cloudant.properties` file.

4. Your Liberty server in Eclipse should automatically pick up the changes and restart the application.

  View your app at: http://localhost:9080/GetStartedJava/. Any names you enter into the app will now get added to the database.

  Make any changes you want and re-deploy to Bluemix!
