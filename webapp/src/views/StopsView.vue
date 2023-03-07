<template>
  <div class="table-container">
    <b-button v-if="editable" variant="success" @click="handleAdd()">
      Добавить остановку
      <img src="../components/icons/add.svg" width="25px" height="25px">
    </b-button>
    <b-editable-table v-if="editable"
        bordered
        class="editable-table"
        v-model="stops"
        :fields="fields"
        :rowUpdate="rowUpdate"
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
      <b-button class="btn w-50 mt-3" variant="outline-success" @click="onDelete">Да</b-button>
      <b-button class="bty w-50 mt-3" variant="danger" @click="$bvModal.hide('ask')">Отменить</b-button>
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

import BEditableTable from "bootstrap-vue-editable-table";
import {BButton, BIconTrash, BModal} from "bootstrap-vue";
import checkUserPermissions from "@/helpers/checkPermissions";
export default {
  name: "StopsView",
  components: {
    BEditableTable,
    BButton,
    BIconTrash,
    BModal
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
          editable: true,
          placeholder: "Enter Name...",
          class: "name-col",
        },
        {
          key: "lat",
          label: "Широта",
          type: "number",
          editable: true,
          class: "department-col",
          placeholder: "Укажите широту в десятичных градусах"
        },
        {
          key: "lon",
          label: "Долгота",
          type: "number",
          editable: true,
          placeholder: "Укажите долготу в десятичных градусах",
          class: "age-col"
        },
        {
          key: "isEnd",
          label: "Конечная",
          type: "select",
          editable: true,
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
          editable: true,
          class: "date-col"
        },
        {
          key: "timeInterval",
          label: "Интервал (минут)",
          type: "number",
          editable: true,
          class: "is-active-col",
        },
        { key: "delete", label: "" }
      ],
      fieldsFixed: [
        {
          key: 'id',
          sortable: true,
          label: 'ID'
        }
        ,
        {
          key: 'name',
          sortable:true,
          label: 'Название'
        },
        {
          key: 'lat',
          sortable: true,
          label: 'Широта'
        },
        {
          key: 'lon',
          sortable: true,
          label: 'Долгота'
        },
        {
          key: 'isEnd',
          sortable: true,
          label: 'Конечная',
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
          key: 'notes',
          sortable: true,
          label: 'Объявления'
        },
        {
          key: 'timeInterval',
          sortable: false,
          label: 'Время ожидания'
        }
      ],
      editable: false,
      user: null,
      stops: [],
      editedItems: [],
      notify: false,
      deleteId: 0,
      rowUpdate: {}
    }
  },
  created() {
    this.user = JSON.parse(localStorage.getItem('user'))
    console.log(this.user)
    this.stops = JSON.parse(localStorage.getItem('stops'))
    if (!this.stops.length) {
      this.getStops()
    }
    this.editable = checkUserPermissions(this.user).stops === 2
  },
  methods: {
    getStops() {
      this.$http.get(url+"/api/v1/stops").then(response=>{
        this.stops = response&&response.data?response.data:[]
        localStorage.setItem("stops", JSON.stringify(response.data))
      })
    },
    handleAdd() {
      this.rowUpdate = {
        edit: true,
        action: "add",
        data: {
          id: 0,
          name: "",
          lat: 0,
          lon: 0,
          isEnd: false,
          notes: "",
          timeInterval: 0
        },
      };
    },
    handleInput(value) {
      console.log(value)
      let stop = this.stops.find(s=>s.id===value.id)
      let field = value.field.key
      stop[field] = value.field.type === "text" ? value.value : JSON.parse(value.value)
      console.log(stop)
      let id = -1
      if (this.editedItems.length) {
        this.editedItems.forEach((s, index)=>{
          if (s.id===stop.id) {
            id = index
          }
        })
      }
      id !==-1 ? this.editedItems[id] = stop : this.editedItems.push(stop)
      console.log(this.editedItems)
    },
    onSaveClicked() {
      let success = true
      this.editedItems.forEach(stop=>{
        this.$http.patch(url+"/api/v1/stops", stop).then(response=>{
          console.log(response.data)
          if (!response||!response.data) {
           success = false
          }
       })
     })
      if (success) {
       this.$bvModal.show("success")
       this.$http.get(url+"/api/v1/stops").then(response=>{
         this.stops = response&&response.data?response.data:[]
         localStorage.setItem("stops", JSON.stringify(response.data))
       })
      }
      this.editedItems.length=0
    },
    handleDelete(data) {
      if (data.id === 0) {
        this.rowUpdate = { id: data.id, action: "delete" }
        return
      }
      console.log(data)
      this.notify = true
      this.$bvModal.show("ask")
      this.deleteId = data.id
    },
    onDelete() {
      console.log(this.deleteId)
      this.$http.delete(url+"/api/v1/stops", {id: this.deleteId}).then(response=>{
        if(response&&response.data) {
          this.$bvModal.show("success")
          this.$http.get(url+"/api/v1/stops").then(response=>{
            this.stops = response&&response.data?response.data:[]
            localStorage.setItem("stops", JSON.stringify(response.data))
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.editable-table .data-cell {
  padding: 8px;
  vertical-align: middle;
}
.remove-icon {
  color: red;
  cursor: pointer;
  font-size: 20px;
}

</style>