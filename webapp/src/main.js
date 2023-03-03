import Vue from 'vue'

import App from './App.vue'
import router from './router'

import http from "@/plugins/http";
import BootstrapVue from "bootstrap-vue";
Vue.use(http, {
  baseUrl: "http://localhost:8080"
})
Vue.prototype.$baseUrl = "http://localhost:8080"
export const url = Vue.prototype.$baseUrl

Vue.use(BootstrapVue);
new Vue({
  router,
  render: (h) => h(App)
}).$mount('#app')
