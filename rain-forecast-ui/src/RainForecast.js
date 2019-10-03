import React from 'react';

class RainForecast extends React.Component {

    constructor(props) {
        super(props);
        this.state = { rain: null, fetching: false } 
    }

    render() {
        if (this.props.location == null) {
            return <div>Hey we need location!</div>
        }else {
            return <div>thanks for your location</div>
        }
    }
}

export default RainForecast