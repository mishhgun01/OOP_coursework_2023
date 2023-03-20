import axios from "axios";
import {url} from "@/main";

export function getRoutes() {
    return axios.get(url+"/api/v1/routes")
}
