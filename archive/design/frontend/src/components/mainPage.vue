<template>
	<el-carousel :interval="4000" type="card" height="400px" style="padding: 3ex;">
		<el-carousel-item v-for="item in bannerItem" :key="item">
			<!-- <h3 text="2xl" justify="center">{{ item }}</h3> -->
			<banner :id="item"></banner>
		</el-carousel-item>
	</el-carousel>
	<el-divider content-position="left">Popular products</el-divider>
	<Showcase :list="popularItem"></Showcase>
	<br/>
	<el-divider content-position="left">Recommendations</el-divider>
	<Showcase :list="moreItem"></Showcase>
	<br/>
	<el-backtop :right="100" :bottom="100" />
</template>

<script setup lang="ts">
import axios from 'axios';
import Showcase from './showcase.vue';
import { onMounted, ref } from 'vue';
import banner from './banner.vue';

const popularItem = ref<number[]>([])
const moreItem = ref<number[]>([])
const bannerItem = ref<number[]>([])

onMounted(() => {
	fetchInfo()
})

const recommendAPI = '/api/recommend'

const fetchInfo = () => {
	// setTimeout(() => {
	// 	popularItem.value = [1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9]
	// }, 200)
	axios.post(recommendAPI, {item: 20})
		.then((response) => {
			let arr = []
			for (let i in response.data) {
				
				arr.push(response.data[i].product_id)
			}
			popularItem.value = arr
		})
		.catch(err => {
			console.error(err.message);
			
		})

		axios.post(recommendAPI, {item: 600})
		.then((response) => {
			
			let arr = []
			for (let i in response.data) {
				
				arr.push(response.data[i].product_id)
			}
			moreItem.value = arr
		})
		.catch(err => {
			console.error(err.message);
			
		})

		axios.post(recommendAPI, {item: 6})
		.then((response) => {
			
			let arr = []
			for (let i in response.data) {
				
				arr.push(response.data[i].product_id)
			}
			bannerItem.value = arr
		})
		.catch(err => {
			console.error(err.message);
			
		})
}

</script>

<style scoped>
</style>