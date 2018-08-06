## Connecting to a MongoDB Database

You'll need access to a MongoDB instance. The below steps outline how to do this:

1. Sign in to your IBM Cloud Console, and select the 'Create Resource' button in the top right of the page.
2. Type 'MongoDB' in the search field and select 'Compose for MongoDB' under the 'Data & Analytics' section.
3. Check that the name/region/organization/space/pricing fields are accurate.
4. Select the 'Create' button in the bottom left of the page.
5. Return to the IBM Cloud dashboard and select the newly created service.
6. In the sidebar on the left side of the page, select 'Service Credentials'
7. Select the 'New Credential' button, and then the 'Add' button in the page that appears.

To prepare the application for use in a local environment, use the below steps. Otherwise, skip the steps to move on to deploying the application to IBM Cloud:

1. The new credential will be listed in the table on the same page. Select the 'View credentials' option in the 'Actions' column.
2. Copy the contents of the 'uri' field to the 'mongo_url' field in 'src/main/resources/mongo.properties'.
3. Copy the contents of the 'ca_certificate_base64' field to the 'mongo_ssl' field in the same file as the previous step.

Before deploying the application, make sure that it builds successfully by running `mvn install liberty:run-server` from the root directory of the project (where this README document is located). If no errors are shown, run `cf push` to deploy the application. Once the deployment process completes, run `cf bind-service GetStartedJava [SERVICE_NAME]`, where [SERVICE_NAME] is the name of your MongoDB service. Finally, run `cf restage GetStartedJava`. Once this finishes, you can access the application using the URL provided in the output from the 'cf push' command from earlier.

## Additional Notes on Changes

The application may work with either Cloudant or MongoDB. However, if both services are available, **the application will default to MongoDB**.

When deployed on IBM Cloud, this application does **not** require bound MongoDB services to have any permutation of 'mongodb' in the name. User-provided services (as created with the cf utility) are also acceptable. Finally, a TrustStore does **not** have to be created prior to building/running the application. This is generated during runtime using the MongoDB service's credentials (Though the application will accept or add to a preconfigured TrustStore if one is provided). Generated TrustStores are stored in the working directory of the application, and can be deleted when the application finishes execution.
