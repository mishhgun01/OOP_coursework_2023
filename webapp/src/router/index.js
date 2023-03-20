import Vue from 'vue'
import VueRouter from 'vue-router'
import MainView from "@/views/MainView.vue";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import EmployeesView from "@/views/EmployeesView.vue";
import RoutesView from "@/views/RoutesView.vue";
import StopsView from "@/views/StopsView.vue";
import OpenView from "@/views/OpenView.vue";
import NotFound from "@/views/NotFound.vue";

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'open',
      component: OpenView
    },
    {
      path: '/map',
      name: 'map',
      component: MainView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/employees',
      name: 'employees',
      component: EmployeesView
    },
    {
      path: '/routes',
      name: 'routes',
      component: RoutesView
    },
    {
      path: '/stops',
      name: 'stops',
      component: StopsView
    },
    {
      path: '/*',
      component: NotFound
    }
  ]
})

export default router
