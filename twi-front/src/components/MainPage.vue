<script setup lang="ts">
import TimelineList from './timelinePage/TimelineList.vue';
import { useUserStore } from '../store/globalStore'

const UserStore = useUserStore();
</script>

<template>
  <div class="common-layout">
    <el-container>
			<div class="outter-container">
      <el-header>
				<p>Twine</p>
			</el-header>
      <el-aside width="200px">Aside</el-aside>
      <el-main>Main</el-main>
			</div>
    </el-container>
  </div>
</template>



<style scoped>
.outter-container {
	display: flex;
}
.outter-container p {
	font: bold;
	padding-top: 1ex;
	padding-left: 1ex;
	padding-right: 1ex;
}
.common-layout {
	background-color: #1c1c1c;
  color: #ffffff;
}
</style>