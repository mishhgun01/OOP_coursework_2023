<template>
  <div>
    <b-sidebar id="stop" :title="obj.name" right shadow :visible="show" @change="updateSidebar" width="30%">
      <div class="px-3 py-2">
        <p>
          Cras mattis consectetur purus sit amet fermentum. Cras justo odio, dapibus ac facilisis
          in, egestas eget quam. Morbi leo risus, porta ac consectetur ac, vestibulum at eros.
        </p>
        <b-img src="https://picsum.photos/500/500/?image=54" fluid thumbnail></b-img>
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
    }
  }
}
</script>

<style scoped>

</style>