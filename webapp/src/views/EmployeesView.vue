<template>
<div class="table-container">
  <b-editable-table
      bordered
      class="editable-table"
      v-model="employees"
      :fields="fields"
      @input-change="handleInput"
  >
  </b-editable-table>
  <pre>
      {{ employees }}
    </pre>
</div>
</template>

<script>
import BEditableTable from "bootstrap-vue-editable-table";
export default {
  name: "EmployeesView",
  components: {
    BEditableTable
  },
  data() {
    return {
      fields: [
        {
          key: "name",
          label: "Name",
          type: "text",
          editable: true,
          placeholder: "Enter Name...",
          class: "name-col",
        },
        {
          key: "role",
          label: "role",
          type: "select",
          editable: true,
          class: "department-col",
          options: JSON.parse(localStorage.getItem('roles')).map(r=>{
            return {
              value: r.id,
              text: r.name,
              id: r.id,
              name: r.name
            }
          })
        },
        {
          key: "classification",
          label: "role.classification",
          type: "select",
          editable: true,
          placeholder: "Choose responsibility",
          class: "age-col",
          options: JSON.parse(localStorage.getItem('classes')).map(r=>{
            return {
              value: r.id,
              text: r.name,
              id: r.id,
              name: r.name
            }
          })
        },
        {
          key: "workingDays",
          label: "Date Of Birth",
          type: "date",
          editable: true,
          class: "date-col",
          locale: "en",
          "date-format-options": {
            year: "numeric",
            month: "numeric",
            day: "numeric",
          },
        },
        {
          key: "isActive",
          label: "Is Active",
          type: "checkbox",
          editable: true,
          class: "is-active-col",
        }
      ],
      employees: [],
      roles: [],
      roleField: [],
    };
  },
  created() {
    console.log(JSON.parse(localStorage.getItem('roles')))
    console.log(JSON.parse(localStorage.getItem('classes')))
    console.log((JSON.parse(localStorage.getItem('employees'))))
    console.log(JSON.parse(localStorage.getItem('employees')))
    this.employees = JSON.parse(localStorage.getItem('employees')).map(emp=>{
      return {
        id: emp.id,
        name: emp.name,
        role: emp.role.id,
        workingDays: emp.workingDays,
        classification: emp.classification.id
      }
    })
    const roles = JSON.parse(localStorage.getItem('roles'))
    this.roles  = roles.map(r=>{
      return {
        value: r.id,
        text: r.name,
        id: r.id,
        name: r.name,
        classification: r.classification
      }
    })
    console.log(this.roles)
  },
  methods: {
    handleInput(value, data) {},
  },
};
</script>

<style>
table.editable-table {
  margin: auto;
  background: antiquewhite;
}

table.editable-table td {
  vertical-align: middle;
}

.editable-table .data-cell {
  padding: 8px;
  vertical-align: middle;
}

.editable-table .custom-checkbox {
  width: 50px;
}

.name-col {
  width: 120px;
}

.department-col {
  width: 150px;
}

.age-col {
  width: 150px;
}

.date-col {
  width: 200px;
}

.is-active-col {
  width: 100px;
}


.table-container{
  background: wheat;
  display: flex;
  flex-direction: column;
}
</style>