# Camunda 8 from AWS Java Lambda Functions

This is an example of how to use the [Zeebe Java Client](https://docs.camunda.io/docs/apis-clients/java-client/) from an AWS Lambda. It also includes an example of how to call a lambda function from a Camunda 8 SaaS connector.

Here are the steps to see this working: 

1. Sign up for a trial account on https://camunda.io. Create a cluster and create [API Client Credentials](https://docs.camunda.io/docs/guides/setup-client-connection-credentials).
2. Edit `AppConstants.java` and add your cluster id, client id, and secret. Note that this code can be improved to use Environment variables, but for now, these values are hard coded. 
3. Use the following command to compile and build a jar file: 
```shell
mvn clean install`
```
4. Create a new AWS Lambda function. For example, I named mine `DavePJavaLambdaFunction`. I chose to run on Java 8 on Amazon Linux 2. 
5. After the Lambda is created, go to `Code` in the Lambda config screen and click `Upload from`. Choose `.zip or .jar file` and upload the `target/camunda-lambda-example-1.0.0-SNAPSHOT.jar`
6. Under `Runtime Settings` in the Lambda config screen, click edit. Configure the Handler to use `org.example.camunda.lambda.LambdaFunction::handleRequest`. 
7. Upload the `src/main/resources/trip-booking.bpm` to your SaaS web modeler (or open in desktop modeler). 
8. Edit the Lambda Connector Task inside the business process diagram. For example, click on "Book Hotel", and update the lambda ARN in the properties panel to match the ARN of the Lambda function you just created. 
9. Deploy the Trip Booking process to your Camunda 8 SaaS Cluster. 
10. Create the following two secrets in your Camunda 8 SaaS environment. These credentials should have permission to run your Lambda function:  

```shell
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
```

11. Run the Lambda function passing a payload like the following. This should create an instance of the Trip Booking Process Instance: 

```json
{
  "event": "createInstance"
}
```
