import React from 'react';
import { geolocated } from "react-geolocated";

class DiscoverLocation extends React.Component {

    constructor(props) {
        super(props);
        this.state = { location: null }
    }

    onLocationFetched() {
        let newLocation = { lat: this.props.coords.latitude, long: this.props.coords.longitude }
        this.setState({ location: newLocation });
        this.props.onLocationChange(newLocation);
    }

    render() {
        if (!this.props.isGeolocationAvailable) {
            return <div>Your browser does not support Geolocation</div>
        } else if (!this.props.isGeolocationEnabled) {
            return <div>Waiting for your location :)</div>
        }

        if (this.props.coords && this.state.location == null) {
            this.onLocationFetched()
        }

        if (this.state.location == null) {
            return <div>Waiting for your location :)</div>
        }
        return <div>Your location is latitude {this.state.location.lat} and longitude {this.state.location.long}</div>
    }
}

// see https://no23reason.github.io/react-geolocated/#basic-usage
export default geolocated({
    positionOptions: {
        enableHighAccuracy: false,
    },
    userDecisionTimeout: 5000,
})(DiscoverLocation);