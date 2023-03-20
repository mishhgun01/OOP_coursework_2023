<template>
<div class="image">
  Ищущий, да найдет
</div>
</template>

<script>
export default {
  name: "NotFound"
}
</script>

<style scoped>
.image {
  width: 100vw;
  height: 100vh;
  background: url("@/components/icons/train.png") no-repeat;
  background-size: cover;
  color: aliceblue;    font-size: 64px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-left: 50px;
}
</style>