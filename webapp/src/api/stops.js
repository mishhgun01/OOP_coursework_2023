import {url} from "@/main";
import axios from "axios";


export function getStops() {
    return axios.get(url+"/api/v1/stops")
}

export function patchStops(stop) {
    return axios.patch(url + "/api/v1/stops", stop)
}

export function postStop(stop) {
    return axios.post(url + "/api/v1/stops", stop)
}

export function deleteStop(id) {
    return axios.delete(url+"/api/v1/stops", {id: id})
}