# Sign in with Google OAuth2

![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
<img src="https://img.shields.io/badge/Tutorial%20Reference%20Project-Tutorials%20for%20getting%20started%20with%20Camunda-%2338A3E1" alt="A blue badge that reads: 'Tutorial Reference Project - Tutorials for getting started with Camunda'">

This is a sample Camunda 8 Platform BPM Process that does the OAuth2 handshake to obtain an Access Bearer Token for Google Cloud APIs.

The purpose of this process is to produce a valid OAuth2 Bearer Access Token that can then be used to make GCP API Calls on behalf of a person.  

![](screenshot.png)

# Usage

This process requires 2 things to be defined beforehand: 

1. Google Cloud OAuth2 Client Id passed as a process instance variable
2. Google Cloud OAuth2 Client Secret, defined as `secrets.GCP_OAUTH2_CLIENT_SECRET`

The `OAuth2 Client Id` must be passed to start a process instance, for example: 

```json
{
  "gcpClientId": "<YOUR GCP OAUTH2 CLIENT ID>",
  "gcpAccessToken": null, // This is optional, if passed, the process returns immediately
  "gcpRefreshToken": null // If you've already used this process to generate a refresh token, pass it here
}
```

The `OAuth2 Client Secret` must be set inside a Connector Secret named `secrets.GCP_OAUTH2_CLIENT_SECRET`.

The first time this process is run, just pass `gcpClientId`. Sign into TaskList and follow the directions on the form to manually sign in with Google, allow access, and copy the authorization code. 

After running the process once, you will have a refresh token. You can pass an existing refresh token when creating an instance as well. This will skip the need to have a user manually sign in. 

# TODO

- If an access token is passed when the process is started, we should check if it's valid, if not, then try to refresh
- GCP Scope is currently hard coded. This should be chaged to be an process instance variable. 







