import * as L from "leaflet"
const consts = Object.freeze( {
    ICON_STOP_DEFAULT: L.icon({
        iconUrl: require("@/components/icons/stop_filled.svg"),
        iconSize: [15, 15],
        iconAnchor: [7.5, 7.5],
        popupAnchor: [0, 0],
        tooltipAnchor: [7.5, 0]
    })
})

export default consts