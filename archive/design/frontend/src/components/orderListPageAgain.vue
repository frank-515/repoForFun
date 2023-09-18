<template>
    <div style="padding-left: 5ex;">
        <div class="head">
            <el-icon>
                <Goods />
            </el-icon>
            Order list
        </div>
        <div class="list">
            <ul :class="i % 2 ? 'item' : ''" v-for="item, i in list" :key="item.valueOf()">
                <thumbnailProductCard style="padding: 1ex;" :id="item.valueOf()"></thumbnailProductCard>
            </ul>
            <a v-if="list!.length" style="color: grey;">{{ list!.length }} results</a>

            <div v-else>
                <el-result icon="info" title="There is no more results"></el-result>
            </div>

        </div>
    </div>
</template>

<script setup lang="ts">
import thumbnailProductCard from './thumbnailProductCard.vue';
import { onMounted, ref } from 'vue'
import axios from 'axios';

const list = ref(Array())

const props = defineProps({
    id: Number //userID
})



onMounted(() => {
    fetchInfo()
})

const fetchInfo = () => {
    const user_real_id = props.id || getCookieValue('user_id')

    const getUserBuyRecordAPI = '/api/user-order'
    axios.post(getUserBuyRecordAPI, { user_id: user_real_id })
        .then((response) => {
            
            let arr = []
            for (let i in response.data) {
                arr.push(response.data[i].product_id)
            }
            list.value = arr
        })
        .catch((error) => {
            console.error(error.message);
        })



}

function getCookieValue(cookieName: string) {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].trim();

        if (cookie.indexOf(cookieName + "=") === 0) {
            return cookie.substring(cookieName.length + 1, cookie.length);
        }
    }

    return null;
}

</script>

<style scoped>
.head {
    font-size: xx-large;
    font-weight: bolder;
    padding-block: 3ex;
    display: flex;
    align-items: center;
}

.list {}

.item {
    background-color: rgb(240, 247, 249);
    border-radius: 1ex;
}
</style>