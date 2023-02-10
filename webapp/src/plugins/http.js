import axios from "axios"

export default {
    install(Vue, options) {
        Vue.prototype.$http = axios.create({
            baseUrl: "http://localhost:8080",
            headers: options.headers || null
        })
    }
}