import React, { Component } from 'react';
import Autosuggest from 'react-autosuggest';

function escapeRegexCharacters(str) {
  return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
};

export default class SuggestItem extends Component {
  constructor() {
    super();
    this.state = {
      value: '',
      suggestions: [],
      items: []
    };

    this.onSuggestionFetchRequested = this.onSuggestionFetchRequested.bind(this);
    this.onSuggestionClearRequested = this.onSuggestionClearRequested.bind(this);
    this.getSuggestionValue = this.getSuggestionValue.bind(this);
    this.renderSuggestion = this.renderSuggestion.bind(this);
    this.getSuggestion = this.getSuggestion.bind(this);
    this.onSuggestionSelected = this.onSuggestionSelected.bind(this);
  }

  componentDidMount() {

    fetch('/api/items')
      .then(response => response.json())
      .then(data => this.setState({ items: data }));
  }

  onChange = (event, { newValue }) => {
    this.setState({
      value: newValue
    });
  };

  onSuggestionSelected(event, { suggestion }) {
    this.props.onChange(suggestion.itemName);
  }

  getSuggestion(value) {
    const escapeValue = escapeRegexCharacters(value.trim());

    if (escapeValue === '') {
      return [];
    }

    const regex = new RegExp('^' + escapeValue, 'i');

    return this.state.items.filter(item => regex.test(item.itemName))
  };

  getSuggestionValue(suggestion) {
    return `${suggestion.itemName} (${suggestion.itemLvl})`;
  };

  renderSuggestion(suggestion) {
    return (
      <span>{suggestion.itemName} ({suggestion.itemLvl})</span>
    )
  };

  onSuggestionFetchRequested = ({ value }) => {
    this.setState({
      suggestions: this.getSuggestion(value)
    });
  };

  onSuggestionClearRequested = () => {
    this.setState({
      suggestions: []
    });
  };

  render() {
    const { id, placeholder } = this.props;
    const { value, suggestions } = this.state;
    const inputProps = {
      placeholder,
      value,
      onChange: this.onChange
    }

    return (
      <Autosuggest
        id={id}
        suggestions={suggestions}
        onSuggestionsFetchRequested={this.onSuggestionFetchRequested}
        onSuggestionsClearRequested={this.onSuggestionClearRequested}
        getSuggestionValue={this.getSuggestionValue}
        renderSuggestion={this.renderSuggestion}
        onSuggestionSelected={this.onSuggestionSelected}
        inputProps={inputProps}
      />
    );
  };
}
