import React from 'react';
import { geolocated } from "react-geolocated";

class DiscoverLocation extends React.Component {

    render() {
        if (!this.props.isGeolocationAvailable) {
            return <div>Your browser does not support Geolocation</div>
        } else if (!this.props.isGeolocationEnabled) {
            return <div>Waiting for your location :)</div>
        }

        if (!this.props.coords) {
            return <div>Reading for your location...</div>
        }

        console.log("Your location is latitude: " + this.props.coords.latitude + " and longitude: " + this.props.coords.longitude);
        return null
    }
}

// see https://no23reason.github.io/react-geolocated/#basic-usage
export default geolocated({
    positionOptions: {
        enableHighAccuracy: false,
    },
    userDecisionTimeout: 5000
})(DiscoverLocation);