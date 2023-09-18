<template>
    <div class="wrapper">
        
          
        <div class="title"><el-icon><Search/></el-icon>Search for products </div>
        <el-input placeholder="Find anything you want..." size="large" style="padding-inline: 2ex;" v-model="searchKeyword">
            <template #prepend>
            <el-button @click="search" :icon="Search" />
          </template>
        </el-input><br/>
        <showcase :list="result"></showcase>
    </div>
</template>

<script setup lang="ts">
import { onMounted,ref } from 'vue';
import axios from 'axios';
import showcase from './showcase.vue';
import { Search } from '@element-plus/icons-vue';

const props = defineProps({
    keyword: String
})

const search = () => {
    window.location.href = '/#/search/' + searchKeyword.value
}

const result = ref(new Array())
const searchKeyword = ref('')

onMounted(() => {
    if (props.keyword) searchKeyword.value = props.keyword
    fetchInfo()
})

const shippingAPI = '/api/search/product'

const fetchInfo = () => {
    axios.post(shippingAPI, {keyword: props.keyword, page: 1, page_size: 100})
        .then((response) => {
            let arr = []
            for (let i in response.data) {
                arr.push(response.data[i].product_id)
            }
            result.value = arr
        })
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