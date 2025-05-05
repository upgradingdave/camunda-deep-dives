# How to use Microsoft Teams Connectors (Spring 2025)

## Create a Microsoft Entra App Registration

Sign in to https://portal.azure.com/ and navigate to `Entra Identity` > `App registrations`

Click `New registration` and create a new app registration.

Navigate to API permissions and add appropriate permissions

Navigate to Certificates & secrets and create a new client secret. Copy the secret value, as you will need it later.

## Microsoft Delegated Authentication in Postman

Follow this guide to set up Postman for Microsoft delegated authentication:

https://learn.microsoft.com/en-us/graph/use-postman-with-delegated-authentication

## Microsoft Delegated Authentication in Camunda

Deploy the [Microsoft - Get Access Token.bpmn](bpmn/Microsoft%20-%20Get%20Access%20Token.bpmn) to automate the process of getting a Microsoft access token on behalf of a user. 

### Initial setup

1. Deploy all the resources in the [bpmn](bpmn) folder. 

2. Record the url of the webhook inbound endpoint inside the [Microsoft - Get Authorization Code.bpm](bpmn/Microsoft%20-%20Get%20Authorization%20Code.bpmn) process. This will be used as the `authCodeCallbackUrl` in the payload.

3. Create a new `AZURE_CLIENT_SECRET` secret in your Camunda Cluster. Get the value of the secret from your Azure App Registration.

4. Then, start an instance of the [Microsoft - Get Access Token.bpm](bpmn/Microsoft%20-%20Get%20Access%20Token.bpmn) using a payload like this. Note, if you leave any of these blank, the processes will prompt you to enter them into a User Task Form.

```json
{
  "azure": {
    "tenantId": "(tenant id from App Registration)",
    "clientId": "(App Registration client id)",
    "clientSecret": "{{secrets.AZURE_CLIENT_SECRET}}",
    "scopes": "https://graph.microsoft.com/ChannelMessage.Send",
    "authCodeCallbackUrl": "https://(your-region).connectors.camunda.io/(your-cluster-id)/inbound/microsoftAuthCodeCallback"
  },
  "teams": {
    "groupId": "(your-group-id)",
    "channelId": "(your-channel-id)",
    "message": "This message was programmatically from Camunda Teams Connector"
  }
}
```

Note: After the success first runs you can copy a valid `azure.accessToken` to the payload if you want to use the access token in the process.

## Send Message to Channel

https://learn.microsoft.com/en-us/graph/api/chatmessage-post?view=graph-rest-1.0&tabs=http

I granted the app registration the following delegated API Permissions. There are other permissions that you can use as well, but this is the one I used 

`ChannelMessage.Send` (Delegated)

```
POST https://graph.microsoft.com/v1.0/teams/{team-id}/channels/{channel-id}/messages
```

### Troubleshooting 

I received the following before I realized I need to use Delegated Authentication: 
```shell
Access token validation failure. Invalid audience
```

I received the following and realized I needed to grant `ChannelMessage.Send` to the App Registration, and this is also needed to be sent inside the `scopes` during the authorization code request. 

```shell
Missing scope permissions on the request. API requires one of 'ChannelMessage.Send, Group.ReadWrite.All'. Scopes on the request 'User.Read, profile, openid, email'
```

## List Channels

https://learn.microsoft.com/en-us/graph/api/channel-list?view=graph-rest-1.0&tabs=http

```
GET /teams/{team-id}/channels
```

This requires Admin consent. You can also use delegated permissions I believe, but I used Admin consent. 

`Channel.ReadBasic.All` (Delegated)
`ChannelSettings.Read.All` (Admin)

## Other Useful Links

https://learn.microsoft.com/en-us/entra/identity-platform/access-tokens

https://login.microsoftonline.com/common/v2.0/.well-known/openid-configuration

https://login.microsoftonline.com/(your-tenant-id)/oauth2/v2.0/token

https://login.microsoftonline.com/common/oauth2/v2.0/token








