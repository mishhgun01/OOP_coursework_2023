import * as L from "leaflet"
const consts = Object.freeze( {
    ICON_STOP_DEFAULT: L.icon({
        iconUrl: require("@/components/icons/stop_filled.svg"),
        iconSize: [15, 15],
        iconAnchor: [7.5, 7.5],
        popupAnchor: [0, 0],
        tooltipAnchor: [7.5, 0]
    }),
    RESPONSIBILITIES: [
        {
            id: 1,
            text: "Красная ветка",
            value: "Красная ветка"
        },
        {
            id: 2,
            text: "Синяя ветка",
            value: "Синяя ветка"
        },
        {
            id: 3,
            text: "Зелёная ветка",
            value: "Зеленая ветка"
        },
        {
            id: 4,
            text: "Оранжевая ветка",
            value: "Оранжевая ветка"
        },
        {
            id: 5,
            text: "Фиолетовая ветка",
            value: "Фиолетовая ветка"
        }
    ]
})

export default consts