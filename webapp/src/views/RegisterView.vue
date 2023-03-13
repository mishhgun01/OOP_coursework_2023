<template>
  <div class="container-form">
  <div class="container-form__login">
    <h2 class="align-self-center mb-3">Регистрация</h2>
    <div class="input">
      <label for="email">Имя</label>
      <input
          class="form-control"
          type="text"
          placeholder="Иван"
          v-model="name"
      />
    </div>
    <div class="input">
      <label for="email">Фамилия</label>
      <input
          class="form-control"
          type="text"
          placeholder="Иванов"
          v-model="lastname"
      />
    </div>
    <div class="input">
      <label for="password">Должность</label>
      <b-form-select
          id="inline-form-custom-select-pref"
          class="form-control mt-2"
          :options="roles"
          v-model="role"
      />
    </div>
    <div class="input">
      <label for="password">Классификация</label>
      <b-form-select
          id="inline-form-custom-select-pref"
          class="form-control mt-2"
          :options="classifications"
          v-model="classification"
      />
    </div>
    <div class="input">
      <label for="password">Ответственен за</label>
      <b-form-select
          id="inline-form-custom-select-pref"
          class="form-control mt-2"
          :options="routes"
          v-model="route"
      />
    </div>
    <div class="input">
      <label for="email">Логин</label>
      <input
          class="form-control"
          type="text"
          name="email"
          placeholder="email@adress.com"
          v-model="login"
      />
    </div>
    <div class="input">
      <label for="password">Пароль</label>
      <input
          class="form-control"
          type="password"
          name="password"
          placeholder="password123"
          v-model="password"
      />
    </div>
    <b-button
        class="btn align-self-center w-50"
        pill
        variant="outline-primary"
        @click="onRegister"
        :disabled="disable"
    >Зарегистрироваться</b-button>

    <div class="alternative-option mt-4">
      Уже есть аккаунт? <b-button class="btn" pill variant="outline-primary" @click="moveToLogin">Войти</b-button>
    </div>
    </div>
  </div>
</template>

<script>

import {BButton, BFormSelect} from "bootstrap-vue";
import {url} from "@/main";
import hash from "@/helpers/helpers";
import consts from "@/helpers/consts";

export default {
  name: "LoginView",
  components:{
    BButton, BFormSelect
  },
  data() {
    return {
      name: "",
      lastname: "",
      roles: [],
      login: "",
      password: "",
      role: null,
      responsibility: "",
      classifications: [],
      classification: null,
      routes: [],
      route: null
    }
  },
  computed: {
    consts() {
      return consts
    },
    disable() {
      return !this.login || !this.password || !this.role || !this.name || !this.lastname || !this.classification || !this.route
    }
  },
  created() {
    this.getData()
  },
  methods: {
    getData() {
      this.$http.get(url+"/api/v1/roles").then(response=>{
        const roles = response&&response.data? response.data : []
        localStorage.setItem('roles', JSON.stringify(roles))
        this.roles = roles.map(r=>{
          return {
            id: r.id,
            name: r.name,
            text: r.name,
            value: r
          }
        })
      })
      this.$http.get(url+"/api/v1/routes").then(response=>{
        const routes = response&&response.data? response.data : []
        localStorage.setItem('routes', JSON.stringify(routes))
        this.routes = routes.map(r=>{
          return {
            id: r.id,
            name: r.name,
            text: r.name,
            value: r
          }
        })
        console.log(this.routes)
      })
      this.$http.get(url+"/api/v1/classification").then(response=>{
        const classifications = response&&response.data? response.data : []
        localStorage.setItem('classifications', JSON.stringify(classifications))
        this.classifications = classifications.map(r=>{
          return {
            id: r.id,
            name: r.name,
            text: r.name,
            value: r
          }
        })
      })
    },
    onRegister() {
      console.log(this.classification)
      const data = {  name: `${this.name} ${this.lastname}`,
        role: this.role,
        login: this.login,
        password: hash(this.password),
        classification: this.classification,
        routes: [this.route.id]}
      console.log(data)
      this.$http.post(url+"/api/v1/authentication", {
        name: `${this.name} ${this.lastname}`,
        role: this.role,
        login: this.login,
        password: hash(this.password),
        classification: this.classification,
        routes: [this.route.id]}).then(response=>{
          if (response&&response.data!==0) {
            this.$http.get(url+"/api/v1/employees", {params: {id:response.data}}).then(response=>{
              localStorage.setItem('user', JSON.stringify(response.data))
              this.$http.get(url + "/api/v1/routes").then(response => {
                const routes = response && response.data ? response.data : []
                localStorage.setItem('routes', JSON.stringify(routes))
                this.$router.push("/map")
              })
            })
          }
      })
    },
    moveToLogin() {
      this.$router.push("/login");
    }
  },
};
</script>
<style>

.alternative-option {
  align-self: center;
}

.form-control {
  border-radius: 45px;
}

</style>