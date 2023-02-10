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
Vue.component('l-map', LMap);
Vue.component('l-tile-layer', LTileLayer);
Vue.component('l-marker', LMarker);
export default {
  name: "MainView",
}
</script>

<style scoped>

</style>