<template>
  <div style="width: 250px; margin-bottom: 5px; justify-content: space-between">
  <b-button v-b-toggle :href="'#collapse-'+profile.id.toString()" variant="primary" class="w-100 text-white">{{profile.name}}</b-button>
  <b-collapse :id="'collapse-'+profile.id.toString()" class="mt-2 w-100">
    <b-card>
      <p class="card-text">{{profile.role.name}}<b-button class="edit"></b-button></p>
      <b-button v-b-toggle :href="'#collapse-'+profile.id.toString()+'.inner'" size="sm" class="text-white">Информация</b-button>
      <b-collapse :id="'collapse-'+profile.id.toString()+'.inner'" class="mt-2">
        <b-card>
          Зона ответственности: {{profile.responsibility}}<b-button class="edit"></b-button>
          Рабочие дни: {{profile.workingdays}}<b-button class="edit"></b-button>
        </b-card>
      </b-collapse>
    </b-card>
  </b-collapse>
  </div>
</template>

<script>
import {BButton, BCollapse, BCard, VBToggle} from "bootstrap-vue";
import Vue from 'vue';

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
export default {
  name: "LeftPanel",
  components: {
    BButton,
    BCard,
    BCollapse,
    VBToggle
  },
  props: {
    label: String,
    profile: Object
  }
}
</script>

<style scoped>
.edit{
  width: 10px;
  margin-left: 5px;
  background: none;
  border: black 1px solid;
  border-radius: 90px;
}
</style>