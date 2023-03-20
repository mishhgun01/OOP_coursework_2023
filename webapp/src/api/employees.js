import axios from "axios";
import {url} from "@/main";

export function getEmployees() {
    return axios.get(url + "/api/v1/employees")
}

export function getEmployeeById(id) {

}