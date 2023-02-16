<template>
  <div>
    <b-editable-table
        bordered
        class="editable-table"
        v-model="stops"
        :fields="fields"
        @input-change="handleInput"
    >
  </b-editable-table>
  </div>
</template>

<script>
import {url} from "@/main";

import BEditableTable from "bootstrap-vue-editable-table";
import checkUserPermissions from "@/helpers/checkPermissions";
export default {
  name: "StopsView",
  components: {
    BEditableTable
  },
  data() {
    return {
      fields: [
        {
          key: "id",
          label: "ID",
          type: "number",
          editable: false
        },
        {
          key: "name",
          label: "Название",
          type: "text",
          editable: this.editable,
          placeholder: "Enter Name...",
          class: "name-col",
        },
        {
          key: "lat",
          label: "Широта",
          type: "number",
          editable: this.editable,
          class: "department-col",
          placeholder: "Укажите широту в десятичных градусах"
        },
        {
          key: "lon",
          label: "Долгота",
          type: "number",
          editable: this.editable,
          placeholder: "Укажите долготу в десятичных градусах",
          class: "age-col"
        },
        {
          key: "isEnd",
          label: "Конечная",
          type: "select",
          editable: this.editable,
          placeholder: "Choose responsibility",
          class: "age-col",
          options: [
              {
                value: true,
                text: "Да"
              },
            {
              value: false,
              text: "Нет"
            }
          ]
        },
        {
          key: "notes",
          label: "Объявление",
          type: "text",
          editable: this.editable,
          class: "date-col"
        },
        {
          key: "timeInterval",
          label: "Интервал (минут)",
          type: "number",
          editable: this.editable,
          class: "is-active-col",
        }
      ],
      stops: [],
      editable: false,
      user: null
    }
  },
  created() {
    this.stops = JSON.parse(localStorage.getItem('stops'))
    if (!this.stops.length) {
      this.getStops()
    }
    this.user = JSON.parse(localStorage.getItem('user'))
    this.editable = checkUserPermissions(this.user).stops === 2
  },
  methods: {
    getStops() {
      this.$http.get(url+"/api/v1/stops").then(response=>{
        this.stops = response&&response.data?response.data:[]
        localStorage.setItem("stops", JSON.stringify(response.data))
      })
    }
  }
}
</script>

<style scoped>

</style>