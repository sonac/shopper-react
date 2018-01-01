import React, { Component } from 'react';
import axios from 'axios';

const uuidv4 = require('uuid/v4');

export default class CraftForm extends Component {
  constructor(props) {
    super(props);
    this.state = {skillName: ''}

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    this.setState({skillName: event.target.value});
  }

  handleSubmit(event) {
    const skillName = this.state.skillName
    const skillId = uuidv4()
    fetch('/api/skill', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        "skillName": skillName,
        "id": skillId
      })
    })
    event.preventDefault();
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          Skill Name:
          <input type="text" value={this.state.value} onChange={this.handleChange} />
        </label>
        <input type="submit" value="Submit" />
      </form>
    );
  }

}
