<template>
	<div class="outter-wrapper">
		<div class="img-wrapper">
			<el-image v-if="imgURL" :src="imgURL" fit="cover"></el-image>
			<el-icon v-else>
				<Loading></Loading>
			</el-icon>
		</div>
		<div class="info">
			<a>{{ productName }}</a><br/>
			<a style="color: gray;"> {{ normalizeDesc(description) }}</a>
			<br />
			<div style="display: flex;">
				<b>{{ price2Str( Number.parseFloat(price.toString())) }}</b>

				<el-tag round v-if="isRecommended" size="small" effect="dark"
					:type="Math.random() > 0.5 ? 'danger' : 'warning'">
					{{ getRandomPromoWords() }}
				</el-tag>
			</div>
			<el-rate v-model="value" disabled/>
			<a style="font-size: small; color: gray;">{{ product_type }}</a>
		</div>
	</div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { Loading } from '@element-plus/icons-vue'
import axios from 'axios';

const props = defineProps({
	id: Number
})
props

const description = ref('')
const price = ref(0.00)
const imgURL = ref('')
const value = ref(0)
const isRecommended = ref(false)
const productName = ref('')
const product_type = ref('')
onMounted(() => {
	fetchProductInfo()
})

const getProductInfoAPI = '/api/product'
const imgAPI = '/api/image'
const fetchProductInfo = () => {
	// setTimeout(() => {
	// 	description.value = "Piggie boy's bag poster"
	// 	price.value = 23.2
	// 	imgURL.value = "https://sns-img-hw.xhscdn.net/1000g0081mu6a17oda0605ohaati8chd9dp7h3io?imageView2/2/w/1920/format/webp|imageMogr2/strip"
	// 	value.value = Math.floor(Math.random() * 10) / 2
	// }, 200);
	axios.post(getProductInfoAPI, {product_id: props.id})
		.then((response) => {
			price.value = response.data.product_price
			description.value = response.data.product_description
			imgURL.value = imgAPI + '/' + response.data.product_image
			value.value = response.data.product_value | (Math.floor(Math.random() * 3) + 8) / 2
			isRecommended.value = response.data.is_recommended
			productName.value = response.data.product_name
			product_type.value = response.data.product_type
		})
}

const normalizeDesc = (desc: String): String => {
	if (desc.length <= 11) return desc;
	else return desc.slice(0, 9) + '...'
}

const price2Str = (price: number) => {
	const formattedPrice = price.toLocaleString('en-US', {
		style: 'currency',
		currency: 'USD'
	});
	return formattedPrice;
};


const getRandomPromoWords = (): String => {
	const promoWords = ['Popular', 'Best', 'Hot', 'Limited', 'Exclusive', 'Special', 'Discounted', 'Featured'];
	return promoWords[Math.floor(Math.random() * promoWords.length)]
}

</script>

<style scoped>
.outter-wrapper {
	width: 160px;
	height: 280px;
}

.img-wrapper {
	width: 160px;
	height: 160px;
	border: 1px solid gainsboro;
	border-radius: 4px;
	display: flex;
	align-items: center;
	justify-content: center;
}

.info {
	padding-inline: 1ex;
}

.info a {
	width: 160px;
	font-size: smaller;
	overflow: hidden;
	/* 文字溢出时隐藏 */
	white-space: nowrap;
	/* 设置文本不换行 */
	text-overflow: ellipsis;
	/* 显示省略号 */
}

.info b {
	color: darkorange;
	padding-right: 2ex;
}
</style>