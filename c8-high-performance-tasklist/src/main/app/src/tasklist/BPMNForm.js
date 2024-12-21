import React, { Component } from 'react';
import { Form } from '@bpmn-io/form-js';


class BPMNForm extends Component {

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
        bpmnForm.importSchema(this.props.schema, this.props.data);
    }

    render() {
        return (
            <div id={"form"}></div>
        );
    }
}

export default BPMNForm;
