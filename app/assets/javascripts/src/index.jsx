import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class CraftSkills extends Component {
  constructor(props) {
    super(props);
    this.state = {
      skills: [],
    };
  };

  componentDidMount() {

    fetch('/api')
      .then(response => response.json())
      .then(data => this.setState({ skills: data }));
  }

  render() {
    const { skills } = this.state
    return (
      <div>
        <select>
        {skills.map(skill =>
          <option key={skill.id} value={skill.id}>
            {skill.skillName}
          </option>
        )}
        </select>
      </div>
    )
  };
}

ReactDOM.render(
  <CraftSkills />,
  document.getElementById('root')
);
