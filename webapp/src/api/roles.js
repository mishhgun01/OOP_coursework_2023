import {url} from "@/main";
import axios from "axios";

export function getRoles() {
    return axios.get(url+"/api/v1/roles")
}