<template>

</template>

<script>
export default {
  name: "OpenView",
  data() {
    return {
      user: null,
      routes: [],
      stops: []
    }
  },
  created() {

    this.user = JSON.parse(localStorage.getItem('user'))
    this.stops = JSON.parse(localStorage.getItem('stops'))
    this.routes = JSON.parse(localStorage.getItem('routes'))
    if (!(this.user && this.routes && this.stops && this.routes.length && this.stops.length)) {
      this.$router.push('/login')
    } else {
      this.$router.push('/map')
    }
  }
}
</script>

<style scoped>

</style>