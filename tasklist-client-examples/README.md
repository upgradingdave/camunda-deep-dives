[![Community Extension](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
[![](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)

# Connect Tasklist Client to a SM environment with "Simple Authentication" (no Identity or Keycloak)

Very simple example of connecting to Tasklist API in a Self Managed environment with Simple Authentication. For example, if you used docker compose to run only `docker-compose-core.yaml` (without identity and keycloak).   

If needed, update the configuration inside [application.yaml](src/main/resources/application.yaml)

To test, run the unit test in [TasklistClientTest](src/test/java/org/example/camunda/process/solution/TasklistClientTest.java)

Or, run the [ProcessApplication](src/main/java/org/example/camunda/process/solution/ProcessApplication.java) spring boot app





