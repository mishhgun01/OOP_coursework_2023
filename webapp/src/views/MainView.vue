<template>
  <div style="display: flex; flex-direction: row; justify-content: flex-start">
    <div class="map">
      <l-map style="width:100%; height: 100%" :zoom="zoom" :center="center">
        <l-tile-layer :url="url" :attribution="attribution"></l-tile-layer>
        <l-control position="topleft">
          <sidebar-panel :user="user"/>
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
                <img src="@/components/icons/flag.svg" alt="flag">
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
      <right-bar v-if="selectedItem" :obj="selectedItem" :show="!!selectedItem" @closeSB="onCloseRightBar"/>
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
import checkUserPermissions from "@/helpers/checkPermissions";
import RightBar from "@/components/RightBar.vue";
import {getStops} from "@/api/stops";
import {getEmployees} from "@/api/employees";
import {getRoutes} from "@/api/routes";
import {getRoles} from "@/api/roles";
import {getClassification} from "@/api/classifications";

export default {
  name: "Map",
  components: {RightBar, SidebarPanel, LeftPanel, LMap, LTileLayer, LMarker, LControl, LPolyline, LTooltip },
  data() {
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
      permissions: null,
      selectedItem: null,
    }
  },
  computed: {
    consts() {
      return consts
    }
  },
  created() {
    console.log(localStorage.getItem('user'))
    this.user = JSON.parse(localStorage.getItem('user'))
    console.log(this.user)
    if (!this.user) {
      this.$router.push("/login")
    }
    console.log(this.user)
    this.permissions = checkUserPermissions(this.user)
    console.log(this.permissions)
    this.getData()
  },
  methods: {
    getData() {
      getStops().then(response=>{
        this.stops = response&&response.data?response.data:[]
        localStorage.setItem("stops", JSON.stringify(this.stops))
      })
      getEmployees().then(response=> {
        this.employees = response && response.data ? response.data : []
        localStorage.setItem("employees", JSON.stringify(this.employees))
      })
      getRoutes().then(response=> {
        this.routes = response && response.data ? response.data : []
        localStorage.setItem("routes", JSON.stringify(this.routes))
        let edges = []
        this.routes.forEach(r=>{
          edges.push(createEdgesFromList(r))
        })
        this.edges = edges.flat()
      })
      getRoles().then(response=>{
        localStorage.setItem("roles", JSON.stringify(response.data))
      })
      getClassification().then(response=>{
        localStorage.setItem("classes", JSON.stringify(response.data))
      })

    },
    onStopClicked(s) {
      console.log(s)
      this.selectedItem = s
      this.selectedItem.isStop =  true
    },
    onEdgeClicked(e) {
      this.selectedItem = e
      this.selectedItem.isStop =  false
      console.log(this.selectedItem)
    },
    onCloseRightBar() {
      this.selectedItem = null
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