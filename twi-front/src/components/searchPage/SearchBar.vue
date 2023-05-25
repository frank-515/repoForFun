<template>
  <div class="search-wrapper">
  <input type="text" placeholder="Search..." v-model="keyword">
  <button @click="onSearch"><i>Go</i></button>
</div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
const keyword = ref('');
const onSearch = () => {
  alert(keyword.value)
}

</script>

<style scoped>
.search-wrapper {
  display: flex;
  align-items: center;
  max-width: 500px;
  width: 100%;
  background-color: #fff;
  border: 2px solid dodgerblue;
  border-radius: 25px;
  padding: 0 10px;
}

.search-wrapper input[type="text"] {
  border: none;
  height: 100%;
  padding: 10px;
  width: 100%;
  border-radius: 25px;
  outline: none;
}

.search-wrapper button {
  background-color:dodgerblue;
  border: none;
  color: #fff;
  cursor: pointer;
  font-size: 16px;
  margin-left: 10px;
  padding: 10px 15px;
  border-radius: 25px;
  position: relative;
  left: 13px;
}

.search-wrapper button i {
  font-size: 16px;
}
</style>