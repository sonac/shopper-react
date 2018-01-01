import React, { Component } from 'react';
import SuggestItem from './SuggestItem.jsx';

export default class Calculation extends Component {
  constructor(props) {
    super(props);
    this.state = {
      items: {},
      quals: []
    };

    this.onClick = this.onClick.bind(this);

  }

  onSugChange = (id, newValue) => {
    this.setState(state => ({
      items: {...state.items, [id]: newValue}
    }));
  };

  onSelChange = (id, newQual) => {
    this.setState({quals: [...state.quals, [id]: newQual]});
  }

  onClick() {
    const items = this.state.items
    const quals = this.state.quals
    fetch('/api/calc', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        items
      })
    });
  }

  render() {
    const ids = ['1', '2', '3', '4', '5'];
    const qualities = ['Common', 'Good', 'Great', 'Flawless', 'Epic', 'Legendary']
    return (
      <div>
        {ids.map(id =>
          <div>
            <SuggestItem key = {id} id = {id} onChange = {this.onSugChange.bind(this, id)} style={styles.suggest}/>
            <select key = {id + 'asd'} value={this.state.selectValue} onChange={this.handleChange} style={styles.select}>
              <option value="Orange">Orange</option>
              <option value="Radish">Radish</option>
              <option value="Cherry">Cherry</option>
            </select>
          </div>
        )}
        <button onClick={this.onClick}>
          Calculate
        </button>
      </div>
    );
  }

}

const styles = {
  suggest: {
    float: 'left'
  },
  select: {
    float: 'left'
  }
};
