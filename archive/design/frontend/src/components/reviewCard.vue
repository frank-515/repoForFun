<template>
    <div class="review-card">
        <div class="review-head">
                <el-avatar v-if="userAvatarURL" :src="'/api/image/' + userAvatarURL" />
                <el-avatar v-else :icon="UserFilled">
                </el-avatar>

            <b style="font-size: large; margin-right: 5ex; margin-left: 2ex;">{{ username }}</b>
                <el-rate v-model="value" disabled show-score text-color="#ff9900" score-template="{value} star" size="large"/>
        </div>
        <br/>
        <div style="font-weight: bold; padding-left: 7ex;">
            {{ reviewText }}
        </div>
        <div style="text-align: end; color: grey;">
            {{ formatTimeSincePurchase(reviewDate) }}
            <el-button style="background-color:rgb(237, 255, 211); "><el-icon><CaretTop/></el-icon></el-button>
            <el-button style="background-color:rgb(255, 225, 203); "><el-icon><CaretBottom/></el-icon></el-button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { UserFilled, CaretTop,CaretBottom } from '@element-plus/icons-vue'
import axios from 'axios';

const username = ref('')
const userAvatarURL = ref('')
const value = ref(0)
const reviewText = ref('')
const reviewDate = ref(new Date())

onMounted(() => {
    fetchInfo()
})

const getReviewAPI = '/api/review'
const fetchInfo = () => {
    // setTimeout(() => {
    //     username.value = 'frank515';
    //     value.value = 4
    //     reviewDate.value = new Date()
    //     reviewText.value = "I Never had a piggie like this one before, THAT'S AMAZING AND I LIKE THIS ONE!"
    // }, 200)
    axios.post(getReviewAPI, {review_id: props.id!})
        .then(response => {
            username.value = response.data[0].username
            userAvatarURL.value = response.data[0].user_avatar
            value.value = response.data[0].star
            reviewDate.value = new Date(response.data[0].review_date)
            reviewText.value = response.data[0].content
        })
        .catch(error => {
            console.error(error.message);
            
        })

}

const props = defineProps({
    id: Number
})
props

const date2str = (date: Date): String => {
    return date.getFullYear() + '-' + date.getMonth() + '-'
        + date.getDay() + ' ' + date.getHours() + ':' + date.getSeconds()
}

function formatTimeSincePurchase(purchaseTime: Date): string {
    // Get the time difference between the current time and purchase time (in milliseconds)
    const currentTime: Date = new Date();
    const timeDifference: number = currentTime.getTime() - purchaseTime.getTime();

    // Calculate the maximum time unit for the time difference
    const seconds: number = Math.floor(timeDifference / 1000);
    const minutes: number = Math.floor(seconds / 60);
    const hours: number = Math.floor(minutes / 60);
    const days: number = Math.floor(hours / 24);

    // Return different results based on the time difference
    if (days > 7) {
        return date2str(purchaseTime).toString()
    } else if (days > 0) {
        return `${days} days ago`;
    } else if (hours > 0) {
        return `${hours} hours ago`;
    } else if (minutes > 0) {
        return `${minutes} minutes ago`;
    } else {
        return "Just now";
    }
}
</script>

<style scoped>
.review-card {
    border-radius: 1ex;
    background-color: #f1f1f1;
    padding: 2ex;
}
</style>