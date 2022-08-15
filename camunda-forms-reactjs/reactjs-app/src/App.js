import React, { Component } from 'react';
import { Form } from '@bpmn-io/form-js-viewer';
import Login from './Login';
import {Stomp} from '@stomp/stompjs';

const sockUrl = 'ws://localhost:8080/ws';
let stompClient = null;

const initial = {
    user: { userId: null },
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
        stompClient.onConnect = function(frame){
            stompClient.subscribe('/topic/process', onProcessEvent);
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

    onLogin = (result) => {
        this.setState({processVariables: merge(this.state.processVariables, result.data)});
        let userId = result.data.field_userId;
        if(userId) {
            this.setState({user: {userId: userId}});
            stompClient.subscribe('/topic/forms/'+userId, this.onFormReady)
            this.startProcess(userId);
        }
    }

    doTaskComplete = (e, results) => {
        this.debug("DO TASK COMPLETE");
        this.setState({screen: "waiting"});
        this.completeTask(this.state.controlVariables.jobKey, results.data);
    }

    componentDidMount() {
        this.wsConnect();
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
                    <div>Processing ...</div>
                )

            } else if(this.state.screen === "loadForm" || this.state.screen === "form") {

                return (<div id={"form"}></div>);

            } else if (this.state.screen === "tasks") {

                const listItems = this.state.tasks.map((task) =>
                  <li key={task.id}><button id={task.id} onClick={this.onTaskClick}>{task.name}></button>[ {task.id} ]</li>
                );

                return (<ul>{listItems}</ul>);

            } else if (this.state.screen === "task") {

                return (
                    <div>
                        <div>Loading Task ...</div>
                    </div>
                );

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
                <Login
                    data={this.state.processVariables}
                    onSubmit={this.onLogin}
                />
            )

        }
    }
}

export default App;
