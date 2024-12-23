[![Community Extension](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
<img src="https://img.shields.io/badge/Tutorial%20Reference%20Project-Tutorials%20for%20getting%20started%20with%20Camunda-%2338A3E1" alt="A blue badge that reads: 'Tutorial Reference Project - Tutorials for getting started with Camunda'">

# High Performance Tasks

Sample to demonstrate User Task Throughput possible in C8

# Notes

- As of December 2024, User Task Listeners are not planned to be available until 8.7, so, for this experiment, we'll use Job Workers. 
- [How to migrate to Zeebe user tasks](https://docs.camunda.io/docs/apis-tools/migration-manuals/migrate-to-zeebe-user-tasks/)
- Followed [this guide](https://docs.camunda.io/docs/apis-tools/java-client/) to create Zeebe Client. Had to use `grpcs://clusterId.region.camunda.io` instead of `clusterId.region.camunda.io:443`
- Some of the v2 rest api calls are still alpha and aren't complete so I stuck with v1 as much as possible

# Backend - Spring Boot Rest API

The backend is written using Spring Boot to provide a REST API Service.

(Most of this code was copied directly from
[this project](https://github.com/camunda-community-hub/camunda-8-process-solution-template))

To run the backend, update [application.properties](src/main/resources/application.properties) to point to your environment. Then run the application like this:

```shell
./mvnw spring-boot:run
```

The webapp should start on port `8080`.

[TaskListController.java](src/main/java/org/example/camunda/process/solution/facade/TaskListController.java) implements a REST API endpoint that is available at http://localhost:8080/tasklist.

For example, to see all tasks assigned to a `dave`, try this:

```shell
curl 'http://localhost:8080/tasklist/getAssigneeTasks?userId=dave'
```

Behind the scenes, this REST API is using the [Camunda Tasklist Java Client](https://github.com/camunda-community-hub/camunda-tasklist-client-java) to connect and query the [TaskList GraphQL API](https://docs.camunda.io/docs/apis-clients/tasklist-api/tasklist-api-overview/).

# ReactJs Frontend (Still WIP!)

There is a sample ReactJs Applation inside [src/main/app](src/main/app).

Start the reactjs app like this:

```shell
cd src/main/app
npm install
npm start
```
The reactjs webapp should start on port `3000`. You should be able to access this app by browsing to https://localhost:3000

Most of the custom javascript code can be found in [App.js](src/main/app/src/tasklist/App.js).

When you browse to http://localhost:3000, the [Login.js](src/main/app/src/tasklist/Login.js) component will load. This is a very simple sign in form. It just stores whatever is entered into the username field as the `userId`.

Then, the `userId` is used to query tasks from Task List.

If there is only 1 task for `userId`, that form will be displayed immediately.

This ReactJs App supports 2 types of forms: Camunda Forms, or custom react forms.

If a `UserTask` uses a Camunda Form, the [Camunda bpmio Form Js](https://bpmn.io/toolkit/form-js/) library is used to render that form inside a react component.

Otherwise, if the User Task is configured to us a Custom Form Key, then the ReactJs code renders the html found near line 206 in [App.js](src/main/app/src/tasklist/App.js).

This html can be easily modified to display a custom form. And, of course, you could implement more complicated mechanism for looking up, and rendering forms.


