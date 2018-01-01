import React, { Component } from 'react';
import { Button } from 'react-foundation-components/lib/button';
import ReactModal from 'react-modal';
import GermanWords from './GermanWords.jsx';

const customStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : 'auto',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)'
  }
};

export default class PopUp extends Component {
  constructor() {
    super();
    this.state = {
      showModal: false
    };

    this.handleOpenModal = this.handleOpenModal.bind(this);
    this.handleCloseModal = this.handleCloseModal.bind(this);
  }

  handleOpenModal() {
    this.setState({ showModal: true });
  }

  handleCloseModal() {
    this.setState({ showModal: false });
  }

  render() {
    return (
      <div>
        <Button color="success" size="large" onClick={this.handleOpenModal}>Play german stuff game</Button>
        <ReactModal
          isOpen={this.state.showModal}
          contentLabel="Male, female or its a trap?"
          onRequestClose={this.handleCloseModal}
          style={customStyles}
        >
          <h3>German Stuff Game</h3>
          <GermanWords />
          <Button onClick={this.handleCloseModal}>Close</Button>
        </ReactModal>
      </div>
    );
  }

}
