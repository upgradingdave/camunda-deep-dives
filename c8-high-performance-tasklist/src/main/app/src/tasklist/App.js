import React, { Component } from 'react';
import { Form } from '@bpmn-io/form-js-viewer';
import BPMNForm from './BPMNForm';

const loginFormSchema = require('../forms/login.form.json');

var rest = require('rest');
var mime = require('rest/interceptor/mime');
var client = rest.wrap(mime);

const authApi = "http://localhost:8080/auth";
const taskListApi = "http://localhost:8080/tasklist";

const initial = {
    bpmnForm: null,
    schema: null,
    data: {},
    task: null,
    user: { userId: null },
    tasks: [],
    screen: "waiting",
    history: {}
};

function arrToObj(arr, keyName) {
  if(arr) {
      return arr.reduce(function (acc, currVal) {
          let k = currVal[keyName];
          acc[k] = currVal;
          return acc;
      }, {});
  } else {
      return {}
  }
}

class App extends Component {

    constructor(props) {
        super(props);
        this.state = initial;

        this.poll = this.poll.bind(this);
        this.onLogin = this.onLogin.bind(this);
        this.handleTasks = this.handleTasks.bind(this);
        this.onTaskClick = this.onTaskClick.bind(this);
        this.loadTask = this.loadTask.bind(this);
        this.handleTask = this.handleTask.bind(this);
        this.loadForm = this.loadForm.bind(this);
        this.handleForm = this.handleForm.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.doTaskComplete = this.doTaskComplete.bind(this);
        this.handleTaskComplete = this.handleTaskComplete.bind(this);
        this.loadVariables = this.loadVariables.bind(this);
        this.handleVariables = this.handleVariables.bind(this);
    }

    poll() {
        if(this.state.user.userId && this.state.screen === "waiting") {
            console.log("check for tasks ...");
            client(`${taskListApi}/findTasksByAssignee/${this.state.user.userId}`).then(this.handleTasks);
        }
        this.setState({history: {lastCheck: new Date()}});
    }

    onLogin(result) {
        console.log("Sign in attempt ...");
        if(result.data.field_userId) {
            console.log("Signing in ...");
            this.setState({user: {userId: result.data.field_userId}});
        }
    }

    handleTasks(response) {
        let tasks = response.entity;
        console.log(tasks);
        this.setState({tasks: tasks});

        if(!tasks || tasks.length <=0) {
            // keep waiting
        } else if(tasks.length === 1) {
            this.setState({screen: "task"})
            this.loadTask(tasks[0].id);
        } else {
            this.setState({screen: "tasks"});
        }
    }

    onTaskClick(e) {
        this.loadTask(e.target.id);
    }

    loadTask(taskId) {
        console.log("loadTask");
        client(`${taskListApi}/findTaskById/${taskId}`).then(this.handleTask);
    }

    handleTask(response) {
        console.log("handleTask");
        let task = response.entity;
        console.log(task);
        this.setState({task: task});
        this.loadVariables(task.id);
    }

    loadVariables(taskId) {
        console.log("loadVariables");
        client(`${taskListApi}/findTaskVariablesById/${taskId}`).then(this.handleVariables);
    }

    handleVariables(response) {
        let variables = response.entity;
        console.log("handleVariables")
        console.log(variables);
        let varMap = arrToObj(variables, "name");
        this.setState({variablesMap: varMap})
        this.setState({screen: 'variablesOnlyForm'});

        /*
                if(task.formId) {
                    this.loadForm(task);
                }
                if(task.formKey.startsWith("camunda-forms")) {
                  this.loadForm(task);
                } else {
                  // convert variables into a map structure for convenience:
                  let varMap = arrToObj(this.state.task.variables, "name");
                  this.setState({variablesMap: varMap})
                  // manually display custom form
                  this.setState({screen: 'customForm'});
                }*/
    }

    loadForm(task) {
        client(`${taskListApi}/getFormByKey?formKey=${task.formKey}&processDefinitionId=${task.processDefinitionId}`)
            .then(this.handleForm);
    }

    handleForm(response) {

        let formResp = response.entity;
        let schema = JSON.parse(formResp.schema);
        this.setState({schema: schema});
        this.setState({screen: "loadForm"});

    }

    handleFormSubmit(evt) {
      console.log(evt);
      evt.preventDefault();
      //TODO: if the `customForm` is implemented, then the data passed back should be the data that is currently entered
      // into the form. But for now, we're just passing `taskComplete` back to the process.
      this.doTaskComplete(evt, {data: {"taskComplete": true}});
    }

    doTaskComplete(e, results) {
        console.log("complete task ...");
        client({
            path: `${taskListApi}/completeTask`,
            headers: {'Content-Type': 'application/json'},
            entity: {
                taskId: this.state.task.id,
                variables: results.data,
            }
        }).then(this.handleTaskComplete);
    }

    handleTaskComplete() {
        this.setState({
            bpmnForm: null,
            schema: null,
            data: {},
            task: null,
            user: { userId: this.state.user.userId },
            tasks: [],
            screen: "waiting",
            history: {}
        });
    }

    componentDidMount() {
        this.timerId = setInterval(() => this.poll(), 900);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if(this.state.screen === "loadForm") {
            console.log('rendering form ... ');
            this.setState({screen: "form"});

            // Load the form
            const container = document.querySelector('#form');
            if(!container.firstChild) {
                const bpmnForm = new Form({container: container});
                bpmnForm.on('submit', this.doTaskComplete);
                bpmnForm.importSchema(this.state.schema, this.state.data).then(
                    function (result) {
                        console.log(result);
                    });

                this.setState({bpmnForm: bpmnForm});
            }
        }
    }

    render() {

        if(this.state.user.userId) {
            // User is Authenticated
            if(this.state.screen === "waiting") {

                return (
                    <div>Searching for tasks assigned to {this.state.user.userId} ...</div>
                )

            } else if(this.state.screen === "loadForm" || this.state.screen === "form") {

                return (<div id={"form"}></div>);

            } else if (this.state.screen === "tasks") {

                const listItems = this.state.tasks.map((task) =>
                    <li key={task.id}><a href={"#"} id={task.id} onClick={this.onTaskClick}>{task.name} </a>[ {task.id} ]</li>
                );

                return (<ul>{listItems}</ul>);

            } else if (this.state.screen === "task") {

                return (
                  <div>
                      <div>Attempting to load Task ... </div>
                  </div>
                );

            } else if (this.state.screen === "variablesOnlyForm") {

                /*let listItems = [];
                if(this.state.variablesMap) {
                    listItems = this.state.variablesMap.map((variable) =>
                      <li key={variable.name}>{variable.name}</li>
                    );
                }*/

                return (
                  <div>
                      <h3>Process: {this.state.task.processName}</h3>
                      <h3>Task: {this.state.task.name}</h3>
                  </div>
                );


            } else if (this.state.screen === "customForm") {

              return (
                <form onSubmit={this.handleFormSubmit}>
                  <h1>{this.state.task.name}</h1>
                  <h3>Process Name: {this.state.task.processName}</h3>
                  <h3>Task Id: {this.state.task.id}</h3>
                  <h3>Assigned To: {this.state.task.assignee}</h3>
                  <br/>
                  <h1>(Custom Form can be implemented here)</h1>
                  <div class={"field"}>
                    <label class={"label"}>Sample Custom Text Field</label>
                    <input className="input" type={"text"} placeholder={"Implement custom fields here"}></input>
                  </div>

                  <br/>
                  <input className={"button"} type={"submit"} value={"submit"}/>
                </form>
              )
            } else {
                return (
                    <div>
                        <div>Hmm, not sure what to do?</div>
                    </div>
                );

            }

        } else {

            // User is not Authenticated
            return (
                <BPMNForm
                    data={this.state.data}
                    schema={loginFormSchema}
                    onSubmit={this.onLogin}
                />
            )

        }
    }
}

export default App;
