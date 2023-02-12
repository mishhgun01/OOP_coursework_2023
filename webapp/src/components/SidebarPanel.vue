<template>
  <div>
  <b-button v-b-toggle href="#sidebar" @click.prevent>Сотрудники</b-button>
  <b-sidebar id="sidebar"  :no-close-on-route-change="true" title="Sidebar" shadow>
    <div class="p-0">
      <left-panel v-for="o in object" :profile="o" :key="o.id"/>
    </div>
  </b-sidebar>
  </div>
</template>

<script>
import LeftPanel from "@/components/LeftPanel.vue";
import {BButton, BSidebar, BCard, VBToggle} from "bootstrap-vue"
import Vue from "vue";

export default {
  name: "SidebarPanel",
  components: {LeftPanel, BButton, BSidebar},
  props: {
    object: Array
  }
}
</script>

<style scoped>

</style>