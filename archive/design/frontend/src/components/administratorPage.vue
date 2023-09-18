<template>
    <div>
        <div style="display: flex; align-items: center; padding: 3ex;">
            <el-input size="large" style="padding-right: 6ex;" v-model="keyword"
                placeholder="Search by product name"></el-input>
            <el-button type="primary" size="large" @click="executeSearch"><el-icon>
                    <Search />
                </el-icon>Search</el-button>
            <el-button type="primary" size="large" @click="onAddItem"><el-icon>
                    <Plus />
                </el-icon>Add</el-button>
        </div>
        <ProductAdminList :list="currentList"></ProductAdminList>
    </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import ProductAdminList from './productAdminList.vue';
import { onMounted, ref } from 'vue';
import { Plus } from '@element-plus/icons-vue';

const keyword = ref('')
const page = ref(1)

const currentList = ref<Array<Object>>(new Array())

onMounted(() => {
    executeSearch()
})

const searchProductAPI = '/api/search/product'

const onAddItem = () => {
    window.location.href = '/#/a/manage'
}

const executeSearch = () => {
    // ...
    // setTimeout(() => {
    //     currentList.value = [
    //     {product_id: 23002, product_name: "xu's", product_quantity: 20, product_price: 23, product_description: "I have never seen a pig beautiful like this, Mua, I Love this, My God, all-round GOD", on_shelf_date: '2022-99-00'},
    //     {product_id: 23002, product_name: "xu's", product_quantity: 20, product_price: 23, product_description: "I have never seen a pig beautiful like this, Mua, I Love this, My God, all-round GOD", on_shelf_date: '2022-99-00'},
    //     {product_id: 23002, product_name: "xu's", product_quantity: 20, product_price: 23, product_description: "I have never seen a pig beautiful like this, Mua, I Love this, My God, all-round GOD", on_shelf_date: '2022-99-00'}
    //     ]
    // }, 200);
    axios.post(searchProductAPI, {
        keyword: keyword.value,
        page: page.value
    })

        .then((response) => {
            currentList.value = response.data
        })
        .catch(err => {
            console.error(err.message);
        })
}

</script>

<style></style>