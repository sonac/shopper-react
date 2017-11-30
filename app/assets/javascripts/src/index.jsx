import React from 'react';
import ReactDOM from 'react-dom';
import CraftSkills from './CraftSkills.jsx';
import CraftForm from './CraftForm.jsx';
import PopUp from './PopUp.jsx';
import Calculation from './Calculation.jsx';

function App() {
  return (
    <div>
      <CraftSkills />
      <CraftForm />
      <PopUp />
      <Calculation />
    </div>
  )
}

ReactDOM.render(
  <App />,
  document.getElementById('root')
);
