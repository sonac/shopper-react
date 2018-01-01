import React, { Component } from 'react';
import { Button } from 'react-foundation-components/lib/button';

export default class GermanWords extends Component {
  constructor(props) {
    super(props);
    this.state = {
      words: [{word: "", article: "loading"}],
      word: {word: "", article: ""},
      correct: 0,
      wrong: 0
    };
    this.handleClick = this.handleClick.bind(this);
    this.articleClick = this.articleClick.bind(this);
    this.renderRandomWord = this.renderRandomWord.bind(this);
  };

  componentDidMount() {

    fetch('/api/words')
      .then(response => response.json())
      .then(data => this.setState({ words: data }));
  }

  renderRandomWord() {
    const words = this.state.words;
    var word = words[Math.floor(Math.random() * words.length)];
    this.setState({word : word})
  }

  handleClick() {
    this.renderRandomWord();
  }

  articleClick(value) {
    console.log(this.state.word);
    if (value === this.state.word.article.toString()) {
      this.setState({correct : this.state.correct + 1});
      this.renderRandomWord();
    }
    else {
      this.setState({wrong: this.state.wrong + 1})
      this.renderRandomWord()
    }
  }

  render() {
    var word  = this.state.word;
    if (word.word.includes("\\u")) {
      var code = word.word.match(/\\u([0-9a-f]{4})/g);
      var uDigit = code.toString().substring(2);
      var uni = String.fromCharCode(parseInt(uDigit, 16));
      var myWord = word.word.toString().replace(code, uni);
    } else {
      var myWord = word.word;
    }
    return (
      <div>
        {myWord} <br />
        <div className="generate-new-word">
          <Button color="primary" onClick={this.handleClick}>Get random word</Button> <br />
        </div>
        <div className="gender-buttons">
          <Button color="success" onClick={() => this.articleClick("der")}>Der</Button>
          <Button color="primary" onClick={() => this.articleClick("die")}>Die</Button>
          <Button color="alert" onClick={() => this.articleClick("das")}>Das</Button>
        </div>
        Correct guesses: {this.state.correct} <br />
        Wrong guesses: {this.state.wrong}
      </div>
    )
  }
}
