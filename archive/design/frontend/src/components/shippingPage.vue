<template>
    <div class="wrapper">
        <div class="title"><el-icon><Van></Van></el-icon>Shipping detail </div>
        <div class="info">
            Order ID: <i>{{ generateUniqueID(shipping_id) }}</i><br />
            Recipient: <i>{{ recipient }}</i><br />
            Order time: <i>{{ date2str(purchaseDate) }}</i><br />
            Unit price: <i>{{ price2Str(price) }}</i><br />
            Address: <i> {{ address }} </i><br/>
            Contact Number: <i>{{ contactNumber }}</i><br/>
            Product Name: <i>{{ product_name }}</i><br/>
            Product id: <i>{{ product_id }}</i><br/>
        </div>
        <el-divider />
        <div>
            <thumbnailProductCard :id="id"></thumbnailProductCard>
        </div>
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import thumbnailProductCard from './thumbnailProductCard.vue'
import axios from 'axios';
const props = defineProps({
    id: Number
})
props

const address = ref('')
const purchaseDate = ref(new Date())
const price = ref(0)
const recipient = ref('')
const contactNumber = ref('')
const shippingMethod = ref('')
const product_name = ref('')
const product_id = ref('')
const shipping_id = ref(0)
onMounted(() => {
    fetchInfo()
})

const shippingAPI = '/api/shipment'

const fetchInfo = () => {
    // setTimeout(() => {
    //     address.value = 'Baya Street 231, George Town, Penang, Malaysia'
    //     purchaseDate.value = new Date()
    //     price.value = 100.3
    //     recipient.value = 'frank515'
    //     contactNumber.value = '+60 2123 1239'
    // }, 200)
    axios.post(shippingAPI, {id: props.id})
        .then((response) => {
            address.value = response.data.address
            purchaseDate.value = new Date(response.data.order_date)
            recipient.value = response.data.username
            shippingMethod.value = response.data.shipping_method
            contactNumber.value = response.data.contact_number
            price.value = response.data.product_price
            product_id.value = response.data.product_id
            product_name.value = response.data.product_name
            shipping_id.value = response.data.order_id
        })
}

const price2Str = (price: number) => {
    const formattedPrice = price.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD'
    });
    return formattedPrice;
};

const date2str = (date: Date): String => {
    return date.getFullYear() + '-' + date.getMonth() + '-'
        + date.getDay() + ' ' + date.getHours() + ':' + date.getSeconds()
}

function generateUniqueID(number: number) {
    const prime = 4294967311; // 一个接近2^32的质数
    let id = number * prime; // 将数字与质数相乘，并取模
    let binaryID = id.toString(16); // 将ID转换为二进制字符串
    let paddedID = binaryID.padStart(16, "0"); // 将二进制字符串填充至32位长
    return paddedID;
}

</script>

<style scoped>
.title {
    font-size: xx-large;
    font-weight: bolder;
    padding-block: 3ex;
    display: flex;
    align-items: center;
}

.info {
    font-size: large;

}

.info i {
    text-decoration: underline;
    font-weight: bold;
}

.wrapper {
    padding-inline: 5ex;
}
</style>