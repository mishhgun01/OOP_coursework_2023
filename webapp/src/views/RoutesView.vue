<template>
  <div>
    <b-editable-table
                      bordered
                      class="editable-table"
                      v-model="routes"
                      :fields="fields"
                      @input-change="handleInput"
    >
      <template #cell(delete)="data">
        <BIconTrash
            class="remove-icon"
            @click="handleDelete(data)"
        ></BIconTrash>
      </template>
    </b-editable-table>
    <b-table v-if="!editable"
             :items="stops"
             :fields="fieldsFixed"
    />
    <div class="d-flex justify-content-center mt-5">
      <b-button v-if="editable" class="btn-smaller" pill variant="outline-success" @click="onSaveClicked">Сохранить</b-button>
    </div>
    <b-modal id="ask" hide-footer size="sm">
      <template #modal-title>
        Вы уверены?
      </template>
      <b-button class="mt-3" variant="outline-success" @click="onDelete">Да</b-button>
      <b-button class="mt-3" variant="danger" @click="$bvModal.hide('ask')">Отменить</b-button>
    </b-modal>
    <b-modal id="success" hide-footer size="sm">
      <template #modal-title>
        Успешно!
      </template>
      <b-button class="mt-3" variant="outline-success" size="sm" @click="$bvModal.hide('success')">OK</b-button>
    </b-modal>
  </div>
</template>

<script>
import {url} from "@/main";

export default {
  name: "RoutesView",
  data() {
    return {
      fields: [
        {
          key: "id",
          label: "ID",
          type: "number",
          editable: false,
          class: "name-col",
        },
        {
          key: "name",
          label: "Название",
          type: "text",
          editable: true,
          placeholder: "Enter Name...",
          class: "name-col",
        },
        {

        }
      ],
      routes: []
    }
  },
  created() {
    this.routes = JSON.parse(localStorage.getItem("routes"))
    if (!this.routes.length) {
      this.getData()
    }
    console.log(this.routes)
  },
  methods: {
    getData() {
      this.$http.get(url+"/api/v1/routes").then(response=> {
        this.routes = response && response.data ? response.data : []
        localStorage.setItem("routes", JSON.stringify(response.data))
      })
    }
  }
}
</script>

<style scoped>

</style>