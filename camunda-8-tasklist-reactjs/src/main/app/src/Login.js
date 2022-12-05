import React, { Component } from 'react';
import { Form } from '@bpmn-io/form-js-viewer';
const schema = require('./forms/login.form.json');

class Login extends Component {

    constructor(props) {
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(e, results) {
        this.props.onSubmit(results);
    }

    componentDidMount() {
        const container = document.querySelector('#form');
        const bpmnForm = new Form({container: container});
        bpmnForm.on('submit', this.onSubmit);
        bpmnForm.importSchema(schema, this.props.data);
    }

    render() {
        return (
            <div id={"form"}></div>
        );
    }
}

export default Login;
