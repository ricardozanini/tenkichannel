import React from 'react';
import './App.css';
import RainForecast from './RainForecast';
import DiscoverLocation from './DiscoverLocation';


class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { location: null }
    this.onLocationChange = this.onLocationChange.bind(this)
  }

  onLocationChange(newLocation) {
    this.setState({ location: newLocation })
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <RainForecast location={this.state.location} />
          <DiscoverLocation onLocationChange={this.onLocationChange} />
        </header>
      </div>
    );
  }
}


export default App;
