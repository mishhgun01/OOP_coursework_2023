<template>
  <div style="display: flex; flex-direction: row; justify-content: flex-start">
    <div class="map">
      <l-map style="width:100%; height: 100%" :zoom="zoom" :center="center">
        <l-tile-layer :url="url" :attribution="attribution"></l-tile-layer>
        <l-control position="topleft">
          <sidebar-panel/>
        </l-control>
        <div v-for="stop in stops" :key="stop.id">
          <l-marker
            :lat-lng="{lat: stop.lat, lon: stop.lon}"
            :icon="consts.ICON_STOP_DEFAULT"
            @click="onStopClicked(stop)"
          >
            <l-tooltip class="map-tooltip" id="stop-tooltip">
              <div class="map-tooltip__block">
                <div class="map-tooltip__item">
                  <img src="@/components/icons/flag.svg" alt="flag">
                    {{stop.name || "Без Названия, ID:" + stop.id}}
                </div>
                <div class="map-tooltip__item">
                  <img src="@/components/icons/clock.svg" alt="flag">
                    {{`Время ожидания: ${stop.timeInterval} ${stop.timeInterval<5?'минуты':'минут'}`}}
                </div>
                <div class="map-tooltip__item" v-if="stop.isEnd">
                  Конечная
                </div>
                <div class="map-tooltip__item" v-if="stop.notes && stop.notes.length">
                  Объявления: {{stop.notes}}
                </div>
              </div>
            </l-tooltip>
              </l-marker>
        </div>
        <l-polyline
          v-for="(e,idx) in edges"
          :key="`edge${idx}`"
          :lat-lngs="[{lat: e.from.lat, lon: e.from.lon}, {lat: e.to.lat, lon: e.to.lon}]"
          :color="e.color"
          :weight="4"
          :opacity="2"
          @click="onEdgeClicked(e)"
        >
          <l-tooltip class="map-tooltip">
            <div class="map-tooltip__block">
              <div class="map-tooltip__item">
                <img src="@/components/icons/path.svg" alt="flag">
                {{`${e.from.name} - ${e.to.name}`}}
              </div>
              <div class="map-tooltip__item">
                <img src="@/components/icons/metro.svg" alt="flag">
                {{'Ветка: '+e.routeName || "Без Названия, ID:" + e.routeID}}
              </div>
              <div class="map-tooltip__item">
                <img src="@/components/icons/clock.svg" alt="flag">
                {{`Интервал движения: ${e.timeInterval.join('-')}`}}
              </div>
              <div class="map-tooltip__item" v-if="e.machinists">
                Машинисты: {{e.machinists.join(',')}}
              </div>
              <div class="map-tooltip__item" v-if="e.dispatchers">
                Диспетчеры: {{e.dispatchers.join(',')}}
              </div>
            </div>
          </l-tooltip>
        </l-polyline>
      </l-map>
    </div>
  </div>
</template>

<script>
import "leaflet";
import "leaflet/dist/leaflet.css";

import { LMap, LTileLayer, LMarker, LControl, LPolyline, LTooltip } from "vue2-leaflet";
import LeftPanel from "@/components/LeftPanel.vue";
import {url} from "@/main";
import SidebarPanel from "@/components/SidebarPanel.vue";
import consts from "@/helpers/consts";
import createEdgesFromList from "@/helpers/createEdge";

export default {
  name: "Map",
  computed: {
    consts() {
      return consts
    }
  },
  components: {SidebarPanel, LeftPanel, LMap, LTileLayer, LMarker, LControl, LPolyline, LTooltip },
  data: function() {
    return {
      user: null,
      zoom: 12,
      center: [59.93428, 30.335098],
      url: "https://{s}.basemaps.cartocdn.com/rastertiles/voyager_labels_under/{z}/{x}/{y}{r}.png",
      attribution: "&copy; <a href='http://osm.org/copyrighte'>OpenStreetMap</a> contributors",
      employees: [],
      stops: [],
      routes: [],
      edges: [],
      permissions: null
    }
  },
  created() {
    this.user = localStorage.getItem('user')
    if (!this.user) {
      this.$router.push("/login")
    }
    this.permissions = JSON.parse(localStorage.getItem('user_permissions'))
    this.getData()
  },
  methods: {
    getData() {
      this.$http.get(url + "/api/v1/employees").then(response => {
        this.employees = response && response.data ? response.data : []
        localStorage.setItem("employees", JSON.stringify(response.data))
      })
      this.$http.get(url+"/api/v1/stops").then(response=>{
        this.stops = response&&response.data?response.data:[]
        localStorage.setItem("stops", JSON.stringify(response.data))
      })
      this.$http.get(url+"/api/v1/routes").then(response=>{
        this.routes = response&&response.data?response.data:[]
        localStorage.setItem("routes", JSON.stringify(response.data))
        let edges = []
        response.data.forEach(r=>{
          edges.push(createEdgesFromList(r))
        })
        this.edges = edges.flat()
      })
      this.$http.get(url+"/api/v1/roles").then(response=>{
        localStorage.setItem("roles", JSON.stringify(response.data))
      })
      this.$http.get(url+"/api/v1/classification").then(response=>{
        localStorage.setItem("classes", JSON.stringify(response.data))
      })

    },
    onStopClicked(s) {
      console.log(s)
    },
    onEdgeClicked(e) {
      console.log(e)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.map {
  position: absolute;
  height: 100vh;
  width: 100vw;
}

.map-tooltip__item {
  display: flex;
  height: 30px;
  line-height: 30px;
  padding: 0 15px 0 10px;
}

</style>