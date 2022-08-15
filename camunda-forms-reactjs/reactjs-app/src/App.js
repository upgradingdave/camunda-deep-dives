import React, { Component } from 'react';
import { Form } from '@bpmn-io/form-js-viewer';
import Login from './Login';
import AppScreen from './AppScreen';
import {Stomp} from '@stomp/stompjs';

const sockUrl = 'ws://localhost:8080/ws';
let stompClient = null;

const initial = {
    user: { userId: localStorage.getItem("userId") },
    bpmnForm: null,
    schema: null,
    processVariables: {},
    controlVariables: {},
    tasks: [],
    task: null,
    screen: "waiting"
};

let merge = (a, b) => ({...a,...b});

class App extends Component {

    constructor(props) {
        super(props);
        this.state = initial;
    }

    debug = (msg) => {
        console.log(msg);
        console.log(this.state);
    }

    handleForm = (schema) => {
        this.setState({schema: schema});
        this.setState({screen: "loadForm"});
    }

    wsConnect = () => {
        stompClient = Stomp.client(sockUrl);
        let onProcessEvent = this.onProcessEvent.bind(this);
        let userId = this.state.user.userId;
        let onFormReady = this.onFormReady.bind(this);
        stompClient.onConnect = function(frame){
            stompClient.subscribe('/topic/process', onProcessEvent);
            stompClient.subscribe('/topic/forms/'+userId, onFormReady)
        };

        stompClient.onStompError =function(frame) {
            console.log('STOMP error');
        };

        stompClient.activate();
    }

    wsSend = (endpoint, json) => {
        stompClient.send(endpoint, {}, JSON.stringify(json));
    }

    startProcess = (userId) => {
        this.wsSend("/app/startProcess",{
            "key":"Process_screenFlow1",
            "processVariables":{
                "userId": userId
            }
        });
    }

    completeTask = (jobKey, processVariables) => {
        this.wsSend("/app/completeTask",{
            "key": jobKey,
            "processVariables": processVariables
        });
    }

    onProcessEvent = (message) => {
        this.debug("ON PROCESS EVENT");
        let processSolutionResponse = JSON.parse(message.body);
        console.log(processSolutionResponse);
        this.setState({controlVariables: merge(this.state.controlVariables, processSolutionResponse.result)});
    }

    onFormReady = (message) => {
        this.debug("ON FORM READY");
        let processSolutionResponse = JSON.parse(message.body);
        console.log(processSolutionResponse);
        this.setState({controlVariables: merge(this.state.controlVariables, processSolutionResponse.result)});
        this.setState({processVariables: merge(this.state.processVariables, processSolutionResponse.variables)});
        let schema = JSON.parse(processSolutionResponse.result.formSchema);
        this.handleForm(schema);
    }

    init = () => {
        console.log("init");
        if(this.state.user.userId) {
            this.wsConnect();
            this.setState({screen: "home"});
            //this.startProcess(userId);
        }
    }

    onLogin = (result) => {
        this.setState({processVariables: merge(this.state.processVariables, result.data)});
        this.setState({user: {userId: result.data.field_userId}});
        localStorage.setItem("userId", result.data.field_userId);
        this.init();
    }

    onLogout = (result) => {
        stompClient.deactivate();
        this.setState({user: {userId: null}});
        localStorage.removeItem("userId");
    }

    doTaskComplete = (e, results) => {
        this.debug("DO TASK COMPLETE");
        this.setState({screen: "waiting"});
        this.completeTask(this.state.controlVariables.jobKey, results.data);
    }

    componentDidMount() {
        this.init();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if(this.state.screen === "loadForm") {
            this.setState({screen: "form"});

            // Load the form
            const container = document.querySelector('#form');
            if(!container.firstChild) {
                const bpmnForm = new Form({container: container});
                bpmnForm.on('submit', this.doTaskComplete);
                bpmnForm.importSchema(this.state.schema, this.state.processVariables).then(
                  function (result) {
                      //console.log(result);
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
                  <AppScreen userId={this.state.user.userId} onlogout={this.onLogout}>
                      <div>Processing ...</div>
                  </AppScreen>)

            } else if(this.state.screen === "home") {

                return (
                  <AppScreen userId={this.state.user.userId} onlogout={this.onLogout}>
                      <div></div>
                  </AppScreen>
                );

            } else if(this.state.screen === "loadForm" || this.state.screen === "form") {

                return (
                  <AppScreen userId={this.state.user.userId} onlogout={this.onLogout}>
                      <div id={"form"}></div>
                  </AppScreen>);

            } else if (this.state.screen === "tasks") {

                const listItems = this.state.tasks.map((task) =>
                  <li key={task.id}><button id={task.id} onClick={this.onTaskClick}>{task.name}></button>[ {task.id} ]</li>
                );

                return (
                  <AppScreen userId={this.state.user.userId} onlogout={this.onLogout}>
                      <ul>{listItems}</ul>
                  </AppScreen>);

            } else if (this.state.screen === "task") {

                return (
                  <AppScreen userId={this.state.user.userId} onlogout={this.onLogout}>
                      <div>Loading Task ...</div>
                  </AppScreen>
                );

            } else {

                return (
                  <AppScreen userId={this.state.user.userId} onlogout={this.onLogout}>
                      <div>Hmm, not sure what to do?</div>
                  </AppScreen>
                );

            }

        } else {

            // User is not Authenticated
            return (
              <AppScreen userId={this.state.user.userId} onlogout={this.onLogout}>
                <Login
                    data={this.state.processVariables}
                    onSubmit={this.onLogin}
                />
              </AppScreen>
            )

        }
    }
}

export default App;
