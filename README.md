
# Liberty on Bluemix getting started application
The Bluemix Getting started tutorial for Liberty uses this sample application to provide you with a sample workflow for working with any Liberty app on Bluemix; you set up a development environment, deploy an app locally and on Bluemix, and integrate a Bluemix database service in your app.

The Liberty app uses Liberty's [`mongodb-2.0` feature](https://www.ibm.com/support/knowledgecenter/en/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_mongodb_create.html) and [MongoDB Java driver](https://mongodb.github.io/mongo-java-driver/) to add information to a database and then return information from a database to the UI.

<p align="center">
  <kbd>
    <img src="docs/GettingStarted.gif" width="300" style="1px solid" alt="Gif of the sample app contains a title that says, Welcome, a prompt asking the user to enter their name, and a list of the database contents which are the names Joe, Jane, and Bob. The user enters the name, Mary and the screen refreshes to display, Hello, Mary, I've added you to the database. The database contents listed are now Mary, Joe, Jane, and Bob.">
  </kbd>
</p>

The following steps are the general procedure to set up and deploy your app. See more detailed instructions in the [Getting started tutorial for Liberty](https://console.stage1.bluemix.net/docs/runtimes/liberty/getting-started.html#getting-started-tutorial).

## Before you begin

You'll need a [Bluemix account](https://console.ng.bluemix.net/registration/), [Git](https://git-scm.com/downloads) [Cloud Foundry CLI](https://github.com/cloudfoundry/cli#downloads) and [Maven](https://maven.apache.org/download.cgi) installed.
