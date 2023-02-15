<template>
  <div class="container-form">
  <div class="container-form__login">
      <h2 class="mb-3">Login</h2>
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
      <div class="input">
        <label for="password">Должность</label>
        <b-form-select
            id="inline-form-custom-select-pref"
            class="form-control"
            :options="roles"
            v-model="role"
        />
      </div>
      <b-button
          class="btn"
          variant="outline-primary"
          pill
          :disabled="disable"
          @click="onLogin"
      >Войти
      </b-button>
      <div class="alternative-option mt-4">
        Не зарегистрированы? <b-button class="btn-smaller" pill variant="outline-primary" @click="moveToRegister">Зарегистрироваться</b-button>
      </div>
  </div>
  </div>
</template>

<script>

import {BButton, BFormSelect} from "bootstrap-vue";
import {url} from "@/main";
import hash from "@/helpers/helpers";
import checkUserPermissions from "@/helpers/checkPermissions";

export default {
  name: "LoginView",
  components:{
    BButton, BFormSelect
  },
  data() {
    return {
      roles: [],
      login: "",
      password: "",
      role: null,
      failed: false
    }
  },
  computed: {
    disable() {
      return !this.login || !this.password || !this.role
    }
  },
  created() {
    this.getData()
  },
  methods: {
    getData() {
      this.$http.get(url+"/api/v1/roles").then(response=>{
        const roles = response&&response.data? response.data : []
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
    onLogin() {
      const data = {login: this.login, password: hash(this.password)}
      this.$http.patch(url+"/api/v1/authentication", data).then(response=>{
        if(response&&response.data!==0) {
          this.$http.get(url+"/api/v1/employees", {params: {id:response.data}}).then(response=>{
            localStorage.setItem('user', JSON.stringify(response.data))
            localStorage.setItem('user_permissions', checkUserPermissions(response.data))
          })
          this.$router.push("/map")
        } else {
          this.failed = true
        }
      })
    },
    moveToRegister() {
      this.$router.push("/register");
    },
  },
};
</script>
<style>
.container-form {
  display: flex;
  justify-content: center;
  align-items: baseline;
}

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