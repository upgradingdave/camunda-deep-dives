# Camunda RPA Example (as of Spring 2025)

[Camunda RPA Getting Started Docs](https://docs.camunda.io/docs/components/rpa/getting-started/#test-your-script)

Download a release:
- [1.0.1](https://github.com/camunda/rpa-worker/releases/tag/1.0.1)

Extract the zip and copy the executable into this directory

Set the following environment variables: 

```shell
export CAMUNDA_CLIENT_MODE=saas
export CAMUNDA_CLIENT_AUTH_CLIENTID=xxx
export CAMUNDA_CLIENT_AUTH_CLIENTSECRET=xxx
export CAMUNDA_CLIENT_CLOUD_CLUSTERID=xxx
export CAMUNDA_CLIENT_CLOUD_REGION=xxx
```

Start the RPA worker (Change the following command appropriately for your OS):

```shell
./rpa-worker_1.0.1_darwin_aarch64
```

Open [greenscreen.rpa](rpaScripts/greenscreen.rpa) in Desktop Modeler. Check to ensure you see a green status indicuating the RPA Worker is connected. Edit the script to update the url for your local environment. For example, change `file:///Users/dave/code/camunda-deep-dives/rpa/index.html` to point to the [index.html](index.html) in this directory.

Click the test tube icon. Enter the following payload: 

```
{
  "name": "Dave",
  "employment": "freelancer",
  "package": "mortgage",
  "dob":  "1980-01-01"
}
```

Run the script. You should see a green screen pop up in your browser and the test should pass successfully.

## Troubleshooting tips

The rpa-worker executable will create a `python` directory. In my environment, it was complaining that it couldn't find `pip`. I deleted the `python` directory and re-run the executable to get it to work. 




