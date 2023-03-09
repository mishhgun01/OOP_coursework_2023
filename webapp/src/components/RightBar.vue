<template>
  <div>
    <b-sidebar id="stop" :title="obj.name || `${obj.from.name} - ${obj.to.name}`" right shadow :visible="show" @change="updateSidebar" width="30%">
      <div class="px-3 py-2">
        <div v-if="obj.timeInterval" class="mt-3"> {{`Интервал движения: ${ obj.isStop ?
            obj.timeInterval + " мин" :
            obj.timeInterval.join('-') }`}}</div>
        <div v-if="obj.routeName || obj.routeID && !obj.isStop" class="display_web mt-3">
          <div> {{'Ветка: '+obj.routeName || "Без Названия, ID:" + obj.routeID}}</div>
          <div style="width: 50px; height: 30px">
          <svg v-if="obj.color && !obj.isStop" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            <line x1="20"
                  y1="30"
                  x2="100"
                  y2="30"
                  :stroke="obj.color"
                  stroke-width="10"
            />
          </svg>
          </div>
        </div>
        <div v-if="obj.machinists && !obj.isStop" class="mt-3">
          Машинисты: {{obj.machinists.join(',')}}
        </div>
        <div v-if="obj.dispatchers && !obj.isStop" class="mt-3">
          Диспетчеры: {{obj.dispatchers.join(',')}}
        </div>
        <div class="mt-3" v-if="obj.isEnd && obj.isStop">
          Конечная
        </div>
        <div v-if="obj.notes" class="mt-3">
          Объявления: {{obj.notes}}
        </div>
        <b-button class="btn w-100 mt-3" variant="outline-primary" @click="onEdit">Редактировать</b-button>
      </div>
    </b-sidebar>
  </div>
</template>

<script>

import {BButton, BSidebar, BImg} from "bootstrap-vue";

export default {
  name: "RightBar",
  props: {
    show: Boolean,
    obj: Object
  },
  components: {
    BButton,
    BSidebar,
    BImg
  },
  methods: {
    updateSidebar(value) {
      console.log(value)
      this.$emit('closeSB')
    },
    onEdit() {
      if (this.obj.isStop) {
        this.$router.push('/stops')
        return
      }
      this.$router.push('/routes')
    }
  }
}
</script>

<style scoped>
.display_web{
  display: flex;
  flex-direction: row;
  height: 10%;
  align-items: stretch;
  justify-content: flex-start;
}
</style>