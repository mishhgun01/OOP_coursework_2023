<template>
  <div class="container-form">
  <div class="container-form__login">
    <h2 class="mb-3">Регистрация</h2>
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
          class="form-control"
          :options="roles"
          v-model="role"
      />
    </div>
    <div class="input">
      <label for="password">Ответственен за</label>
      <b-form-select
          id="inline-form-custom-select-pref"
          class="form-control"
          :options="consts.RESPONSIBILITIES"
          v-model="responsibility"
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
        pill
        variant="outline-primary"
        @click="onRegister"
        :disabled="disable"
    >Зарегистрироваться</b-button>

    <div class="alternative-option mt-4">
      Уже есть аккаунт? <b-button class="btn-smaller" pill variant="outline-primary" @click="moveToLogin">Войти</b-button>
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
      responsibility: ""
    }
  },
  computed: {
    consts() {
      return consts
    },
    disable() {
      return !this.login || !this.password || !this.role || !this.name || !this.lastname || !this.responsibility
    }
  },
  created() {
    this.getData()
  },
  updated() {
    console.log(this.role)
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
    },
    onRegister() {
      this.$http.post(url+"/api/v1/authentication", {
        name: `${this.name} ${this.lastname}`,
        role: this.role,
        login: this.login,
        password: hash(this.password),
        responsibility: this.responsibility}).then(response=>{
          if (response&&response.data!==0) {
            this.$http.get(url+"/api/v1/employees", {params: {id:response.data}}).then(response=>{
              localStorage.setItem('user', JSON.stringify(response.data))
            })
            this.$router.push("/map")
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
.container-form__login {
  flex-direction: column;
  justify-content: space-between;
  align-self: center;
  align-content: center;
  width: 700px;
  max-width: 95%;
}

.alternative-option {
  align-self: center;
}
.btn {
  width: 100%;
}

.btn-smaller {
  width: 30%;
 }

.input {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}
.input > label {
  text-align: start;
}
.input > input {
  margin-top: 6px;
  height: 38px !important;
}
</style>