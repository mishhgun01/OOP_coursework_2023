<template>
  <div class="table-container">
    <div class="d-flex flex-row">
    <b-button v-if="editable" class="btn" @click="handleAdd()">
      Добавить остановку
      <img src="../components/icons/add.svg" width="25px" height="25px">
    </b-button>
    <b-button v-if="editable" class="btn_save" @click="onSaveClicked" :disabled="disable">Сохранить</b-button>
    <b-button v-if="editable" class="btn_save" @click="downloadAsFile">Скачать</b-button>
    <b-button v-if="editable" class="btn_save" @click="generateDocument">Сгенерировать отчет</b-button>
    </div>
    <b-editable-table v-if="editable"
        bordered
        class="editable-table w-100"
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
    <b-modal id="ask" hide-footer size="sm">
      <template #modal-title>
        Вы уверены?
      </template>
      <b-button class="btn w-50 mt-3" variant="outline-success" @click="onDeleteClicked">Да</b-button>
      <b-button class="bty w-50 mt-3" variant="danger" @click="$bvModal.hide('ask')">Отменить</b-button>
    </b-modal>
    <b-modal id="success" hide-footer size="sm">
      <template #modal-title>
        Успешно!
      </template>
    </b-modal>
  </div>
</template>

<script>
import {url} from "@/main";

import BEditableTable from "bootstrap-vue-editable-table";
import {BButton, BIconTrash, BModal} from "bootstrap-vue";
import checkUserPermissions from "@/helpers/checkPermissions";
import {deleteStop, getStops, patchStops, postStop} from "@/api/stops";
import jsPDF from "jspdf";
import consts from "@/helpers/consts";
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
          class: "name-col w-25",
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
          class: "date-col w-50"
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
  computed: {
    disable() {
      console.log(this.editedItems)
      return this.editedItems.length === 0
    }
  },
  created() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.stops = JSON.parse(localStorage.getItem('stops'))
    if (!this.stops.length) {
      this.getStops()
    }
    this.editable = checkUserPermissions(this.user).stops === 2
  },
  methods: {
    getStops() {
      getStops().then(response=>{
        this.stops = response&&response.data?response.data:[]
        localStorage.setItem("stops", JSON.stringify(response.data))
      })
    },
    onSaveClicked() {
      let success = true
      this.editedItems.forEach(stop=>{
        if (!stop.isNew) {
          patchStops(stop).then(response => {
            if (!response || !response.data) {
              success = false
            }
            getStops().then(response=> {
              this.stops = response && response.data ? response.data : []
              localStorage.setItem("stops", JSON.stringify(response.data))
              window.location.reload()
            })
          })
        } else {
          const data = {
            id: stop.id,
            name: stop.name,
            lat: stop.lat,
            lon: stop.lon,
            isEnd: stop.isEnd,
            notes: stop.notes,
            timeInterval: stop.timeInterval
          }
          postStop(data).then(response => {
            if (!response || !response.data) {
              success = false
            }
            getStops().then(response=> {
              this.stops = response && response.data ? response.data : []
              localStorage.setItem("stops", JSON.stringify(response.data))
              window.location.reload()
            })
          })
        }
      })
      if (success) {
        this.$bvModal.show("success")
      }
      this.editedItems.length=0
    },
    onDeleteClicked() {
      deleteStop(this.deleteId).then(response=>{
        if(response&&response.data) {
          this.$bvModal.show("success")
          getStops().then(response=>{
            this.stops = response&&response.data?response.data:[]
            localStorage.setItem("stops", JSON.stringify(response.data))
            window.location.reload()
          })
        }
      })
    },
    handleAdd() {
      this.rowUpdate = {
        edit: true,
        action: "add",
        data: {
          id: Math.floor(Math.random() * 1000),
          name: "",
          lat: 0,
          lon: 0,
          isEnd: false,
          notes: "",
          timeInterval: 0,
          isNew: true
        },
      };
    },
    handleInput(value) {
      console.log(value)
      let stop = this.stops.find(s=>s.id===value.id)
      let field = value.field.key
      stop[field] = value.field.type === "text" ? value.value : JSON.parse(value.value)
      stop.lat = Number(stop.lat)
      stop.lon = Number(stop.lon)
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
    downloadAsFile() {
      const a = document.createElement("a");
      const data = JSON.stringify(this.stops, null, '\t')
      const file = new Blob([data], {type: 'application/json'});
      a.href = URL.createObjectURL(file);
      a.download = "stops.json";
      a.click();
    },
    generateDocument() {
      let stops = ""
      const stopNames = this.stops.map(s=>{
        return s.name
      })
      stopNames.forEach(s=>{
        stops+=`${s}\n`
      })
      const incidents = this.stops.filter(s=> s.notes !== "").map(s=>{
        return {
          name: s.name,
          note: s.notes
        }
      })
      let strIncidents = ""
      incidents.forEach(i=>{
        strIncidents += `${i.name}:\n ${i.note}\n`
      })
      const data = ` Остановки:\n\n\t ${stops}\n\n Инциденты/Объявления:\n\n${strIncidents}`
      const customFont = consts.FONT
      const doc = new jsPDF()
      doc.addFileToVFS("customFont.ttf", customFont);
      doc.addFont("customFont.ttf", "customFont", "normal");
      doc.setFont("customFont");
      doc.text(data, 10,10, {lang: 'ru'})
      doc.save("test.pdf")
      // const file = new Blob([data], {type: 'application/pdf'});
      // a.href = URL.createObjectURL(file);
      // a.download = "отчет по остановкам.pdf";
      // a.click();
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

.btn {
  width: 20vw;
  margin: 5px;
  height: 3vw;
  background: none;
  color: black;
}

.btn_save{
  width: 20vw;
  margin: 5px;
  height: 3vw;
  background: none;
  color: black;
}

.btn_save:hover {
  color: antiquewhite;
  background: aquamarine;
}

.btn:hover {
  color: antiquewhite;
  background: #328d0d;
}

</style>