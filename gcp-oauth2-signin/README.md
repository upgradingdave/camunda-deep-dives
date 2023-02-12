# Sign in with Google OAuth2

![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
<img src="https://img.shields.io/badge/Tutorial%20Reference%20Project-Tutorials%20for%20getting%20started%20with%20Camunda-%2338A3E1" alt="A blue badge that reads: 'Tutorial Reference Project - Tutorials for getting started with Camunda'">

This is a sample Camunda 8 Platform BPM Process that does the OAuth2 handshake to obtain an Access Bearer Token for Google Cloud APIs.

The purpose of this process is to produce a valid OAuth2 Bearer Access Token that can then be used to make GCP API Calls on behalf of a person.  

# Usage

This process requires 2 things to be defined beforehand: 

1. Google Cloud OAuth2 Client Id
2. Google Cloud OAuth2 Client Secret

The `Client Id` must be passed to start a process instance, for example: 

```json
{
  "gcpClientId": "<YOUR GCP OAUTH2 CLIENT ID>",
  "gcpAccessToken": null, // This is optional, if passed, the process returns immediately
  "gcpRefreshToken": null // This is optional, if passed, the refersh token is traded for an access token
}
```

The `Client Secret` must be set as a Connector Secret. 

If neither refresh token or access token are present at the start of the process, then a person must go to Task List and
follow the directions on the form to manually sign into google, allow the access, and copy the authorization code. 

If you already have done that step and saved the refresh token, then you can pass that at process start as well. This will 
skip the need to have a user manually sign in. 







