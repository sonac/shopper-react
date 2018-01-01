import React, { Component } from 'react';
import { Button } from 'react-foundation-components/lib/button';

export default class PopUpButton extends Component {
  handleButtonPress() {
    console.log('PopUpButton was pressed')
  }

  render() {
    return (
      <div className="create-popup-button">
        <Button color="primary" onClick={this.handleClick}>Play german stuff game</Button> <br />
      </div>
    );
  }

}
