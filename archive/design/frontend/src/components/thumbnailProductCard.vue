<template>
    <div class="wrapper">
        <el-avatar shape="square" size="large" :src="'/api/image/' + imgURL" style="margin-right: 10ex;"/>
        <a style="font-weight: bold;">{{ description }}</a>
        <div style="flex: 1;"></div>
        <a style="color: gray;">{{ price2Str(price) }}<br/>{{ count }} pcs</a>
    </div>
    

</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'

const imgURL = ref('')
const description = ref('')
const price = ref(0)
const count = ref(1)


const props = defineProps({
    id: Number
})
props

onMounted(() => {
    fetchInfo()
})

const getProductInfoAPI = '/api/product'


const fetchInfo = () => {
    // setTimeout(() => {
    //     imgURL.value = 'https://5b0988e595225.cdn.sohucs.com/images/20181202/c95d13af69a5498e8b9c840b13819f0b.jpeg'
    //     description.value = 'A beautiful Pig You must Love to!'
    //     price.value = 304
    //     count.value = 30
    // }, 200);
    axios.post(getProductInfoAPI, {product_id: props.id})
		.then((response) => {
			price.value = response.data.product_price
			description.value = response.data.product_description
			imgURL.value =response.data.product_image
		})
}

const price2Str = (price: number) => {
    const formattedPrice = price.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD'
    });
    return formattedPrice;
};

</script>

<style scoped>
.wrapper {
    display: flex;
    align-items: center;
}

</style>