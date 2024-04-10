[![Community Extension](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
[![](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)

# Zeebe Client Examples

Very simple example of connecting to a Self Managed environment via Azure Entra Id

To test, change the values in [application.yaml](src/main/resources/application.entra.yaml) and then run the unit test in [EntraZeebeClientTest](src/test/java/org/example/camunda/process/solution/EntraZeebeClientTest.java)

Or, run the [ProcessApplication](src/main/java/org/example/camunda/process/solution/ProcessApplication.java) spring boot app to auto deploy bpmn files located inside [src/main/resources/models](src/main/resources/models)



