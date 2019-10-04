import React from 'react';

class RainForecast extends React.Component {
    DEV_PROXY_HOST = "http://localhost:8080"

    constructor(props) {
        super(props);
        this.state = { rain: null, error: null, fetched: false, fetching: false }
    }

    componentDidUpdate() {
        if (this.state.fetched || this.state.error != null) {
            return;
        }
        if (this.state.fetching) {
            return;
        }

        if (this.state.rain == null && this.props.location != null) {
            let host = process.env.BACKEND_HOST 
            if (process.env.NODE_ENV !== 'production') {
                console.log("Running on non production environment");
                host = this.DEV_PROXY_HOST
            }

            fetch(host + "/rainforecast", {
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
                            fetched: true,
                            fetching: false
                        });
                    },
                    (error) => {
                        this.setState({
                            error: error,
                            fetched: true,
                            fetching: false
                        });
                    }
                )
        }
        this.setState({ fetching: true });
    }

    render() {
        if (this.state.error != null) {
            return <div>Ops! Something bad happened: {this.state.error.message}</div>
        }

        let loadingMessage = "";

        if (this.state.fetching) {
            loadingMessage = "Asking to our gurus..."
        } else if (this.state.fetched) {
            loadingMessage = this.state.rain ? <h1>YES</h1> : <h1>NO</h1>;
        }

        return <div>
            <div><h4>Is it gonna rain?</h4></div>
            <div>{loadingMessage}</div>
            {this.state.fetched && <small>Your location is {this.props.location.latitude}, {this.props.location.longitude}</small>}
        </div>
    }
}

export default RainForecast