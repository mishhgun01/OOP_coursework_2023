import Vue from 'vue'

import App from './App.vue'
import router from './router'

import http from "@/plugins/http";
import BootstrapVue from "bootstrap-vue";
import jsPDF from "jspdf";
Vue.use(http, {
  baseUrl: "http://localhost:8081"
})
Vue.prototype.$baseUrl = "http://localhost:8081"
export const url = Vue.prototype.$baseUrl

Vue.use(BootstrapVue);
Vue.use(jsPDF)
new Vue({
  router,
  render: (h) => h(App)
}).$mount('#app')
