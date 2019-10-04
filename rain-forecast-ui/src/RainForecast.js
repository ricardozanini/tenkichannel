import React from 'react';

class RainForecast extends React.Component {

    constructor(props) {
        super(props);
        this.state = { rain: null, error: null, fetched: false }
    }

    componentDidUpdate() {
        if (this.state.fetched && this.state.error != null) {
            return;
        }

        if (this.state.rain == null && this.props.location != null) {
            fetch("/rainforecast", {
                headers: {
                    "Content-Type": "application/json",
                    "accept": "application/json"
                },
                method: "POST",
                body: JSON.stringify({
                    location: {
                        latitude: this.props.location.latitude,
                        longitude: this.props.location.longitude
                    }
                })
            })
                .then(res => res.json())
                .then(
                    (result) => {
                        this.setState({
                            rain: result.result.rain,
                            error: null,
                            fetched: true
                        });
                    },
                    (error) => {
                        this.setState({
                            error: error,
                            fetched: true
                        });
                    }
                )
        }
    }

    render() {
        if (this.state.error != null) {
            return <div>Ops! Something bad happened: {this.state.error.message}</div>
        }

        if (this.state.rain != null) {
            return  <div>
                        <p>Is it going to rain?</p>
                        {this.state.rain ? <p>YES</p> : <p>NO</p>}
                    </div>
            
        }
        // we didn't reveive the location yet.
        // TODO: spinner?
        return null
    }
}

export default RainForecast