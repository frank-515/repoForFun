<template>
<div style="padding: 3ex; background-color: color: white; " @click="onClick">
    
    <a style="font-size: 36px; font-weight: bolder; font-family:Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;">{{ shoppingPhrases[randomPhrase] }}</a><br/>
    <a>{{ product_name }}</a><br/>
    <a style="font-size: 24px; color: rgb(181, 118, 2);">{{ price2Str(product_price.valueOf()) }}</a><br/>
    <el-image :src="'/api/image/' + product_image" style="width: 256px; position:static; right: 3ex; bottom: 3ex;"></el-image>
    
</div>
</template>

<script setup lang="ts">
import axios from 'axios';
import { onMounted, ref } from 'vue';

const product_description = ref('')
const product_image = ref('')
const product_name = ref('')
const product_price = ref(0)

const props = defineProps({
    id: Number
})

const onClick = () => {
    window.location.href = '/#/product/' + props.id
}

onMounted(() => {
    fetchInfo()
})

const fetchInfo = () => {
    axios.post('/api/product', {product_id: props.id})
        .then((response) => {
            product_description.value = response.data.product_description
            product_image.value = response.data.product_image
            product_name.value = response.data.product_name
            product_price.value = response.data.product_price
        })
}

const price2Str = (price: number) => {
    const formattedPrice = price.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD'
    });
    return formattedPrice;
};
price2Str(0)



const shoppingPhrases = [
  "Bestseller",
  "Editor's Choice",
  "New Arrivals",
  "Limited Time Offer",
  "Sale",
  "Clearance",
  "Featured Products",
  "Customer Reviews",
  "Add to Cart",
  "Wishlist",
  "Out of Stock",
  "Discount",
  "Free Shipping",
  "Buy One, Get One ",
  "Flash Sale",
  "Pre-order",
  "Recommended",
  "Popular Categories",
  "Shopping Cart",
  "Checkout"
];
const randomPhrase = Math.floor(Math.random() * shoppingPhrases.length);
</script>

<style scoped>

</style>

 <!-- 我玩游戏，追求的是体验，念念不忘的体验，而不是某种可以量化的东西，比如战绩，或者某些游戏设计师和心理学家精心设计的让人上瘾的数值  --Frank515 -->