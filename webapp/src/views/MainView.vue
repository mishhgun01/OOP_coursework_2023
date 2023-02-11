<template>
  <l-map style="height: 300px" :zoom="zoom" :center="center">
    <l-tile-layer :url="url" :attribution="attribution"></l-tile-layer>
    <l-marker :lat-lng="markerLatLng"></l-marker>
  </l-map>
</template>

<script>
import L from 'leaflet'
import Vue from "vue";
import {LMap, LPolyline, LTooltip, LIcon, LLayerGroup, LTileLayer, LMarker} from "vue2-leaflet";
import {url} from "@/main";
Vue.component('l-map', LMap);
Vue.component('l-tile-layer', LTileLayer);
Vue.component('l-marker', LMarker);
export default {
  name: "MainView",
  created() {
    this.$http.post(url+"/api/v1/authentication", {name: "testAuth", login: "sdfg", password: "password", role: {id:1, name: "машинист"}}).then(resp=>{
      console.log(resp.data)
    })
  }
}
</script>

<style scoped>

</style>