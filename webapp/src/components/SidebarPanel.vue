<template>
  <div>
  <b-button v-b-toggle href="#sidebar" size="sm" variant="outline-primary" @click.prevent>
    <img src="@/components/icons/edit.svg" alt="edit">
  </b-button>
  <b-sidebar id="sidebar" :no-close-on-route-change="true" :title="`${user.name} (${user.role.name}, ${user.classification.name})`" shadow>
    <template #footer="{ hide }">
      <div class="d-flex text-light align-items-center px-3 py-2">
        <b-button class="btn w-100" variant="danger" size="md" @click="exit">Выйти</b-button>
      </div>
    </template>
    <div class="d-flex justify-content-between flex-column px-3 py-3">
      <div class="py-2">
        <b-button class="btn w-100" variant="outline-primary" @click="$router.push('/employees')">Сотрудники</b-button>
      </div>
      <div class="py-2">
        <b-button class="btn w-100" variant="outline-primary" @click="$router.push('/routes')">Маршруты / Ветки</b-button>
      </div>
      <div class="py-2">
        <b-button class="btn w-100" variant="outline-primary" @click="$router.push('/stops')">Остановки</b-button>
      </div>
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
    object: Array,
    user: Object
  },
  methods: {
    exit() {
      localStorage.clear()
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>

</style>