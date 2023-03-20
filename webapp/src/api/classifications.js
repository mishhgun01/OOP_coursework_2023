import {url} from "@/main";
import axios from "axios";


export function getClassification() {
    return axios.get(url+"/api/v1/classification")
}
