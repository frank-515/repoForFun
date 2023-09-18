<template>
    <div class="wrappar">
        <div class="head">
            <el-avatar :size="120" shape="square" :src="'/api/image/' +avatarURL"></el-avatar>
            <a style="padding-left: 3ex;">{{ username }}'s profile</a>
            <span style="flex: 1;"></span>
            <el-button type="primary">Reset password</el-button>
        </div>
        <el-divider content-position="left">Information</el-divider>
        <div>
            <el-descriptions class="margin-top" title="member profile" :column="1" border>
                <template #extra>
                    <router-link to="/edit-profile">
                        <el-button>Edit profile</el-button>
                    </router-link>
                </template>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <user />
                            </el-icon>
                            Username
                        </div>
                    </template>
                    {{ username }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <iphone />
                            </el-icon>
                            Telephone
                        </div>
                    </template>
                    {{ contactNumber }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <location />
                            </el-icon>
                            Address
                        </div>
                    </template>
                    {{ defaultAddress }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <tickets />
                            </el-icon>
                            Email
                        </div>
                    </template>
                    {{ email }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <Loading />
                            </el-icon>
                            Gender
                        </div>
                    </template>
                    <el-tag :type="genderToColor(gender)">{{ gender }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <tickets />
                            </el-icon>
                            Registration
                        </div>
                    </template>
                    {{ date2str(registrationTime) }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <TrophyBase />
                            </el-icon>
                            User level
                        </div>
                    </template>
                    <el-tag>VIP {{ userLevel }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon>
                                <Coin />
                            </el-icon>
                            Points
                        </div>
                    </template>
                    {{ total_points }}
                </el-descriptions-item>
            </el-descriptions>
        </div>
        <el-divider content-position="left">Recently purchase</el-divider>
        <div>
            <OrderListPage :list="purchaseList"></OrderListPage>
        </div>
        <el-divider content-position="left">Reviews</el-divider>
        <div>
            <a v-if="!reviewList.length">There is no review yet</a>
            <ul v-else v-for="item in reviewList" :key="item.valueOf()">
                <ReviewCard :id="item.valueOf()"></ReviewCard>
            </ul>
        </div>
    </div>
</template>

<script setup lang="ts">
import { Loading } from '@element-plus/icons-vue';
import { onMounted, ref } from 'vue'
import OrderListPage from './orderListPage.vue'
import ReviewCard from './reviewCard.vue'
import axios from 'axios'
import { useGlobalStore } from '../pinia/globalStore';

const globalStore = useGlobalStore()

const username = ref('')
const gender = ref('Other')
const email = ref('')
const contactNumber = ref('')
const registrationTime = ref(new Date())
const total_points = ref(0)
const userLevel = ref(1)
const avatarURL = ref('')
const defaultAddress = ref('')
const purchaseList = ref(new Array<Number>())
const reviewList = ref(new Array<Number>())
const user_id = ref(0)
onMounted(() => {
    fetchInfo()

})

const getUserInfoAPI = '/api/user'

const fetchInfo = () => {
    // setTimeout(() => {
    //     username.value = 'ss'
    //     gender.value = 'Other'
    //     email.value = 'example@example.com'
    //     contactNumber.value = '+99 213881323'
    //     registrationTime.value = new Date()
    //     total_points.value = 3000;
    //     avatarURL.value = 'https://5b0988e595225.cdn.sohucs.com/images/20181202/c95d13af69a5498e8b9c840b13819f0b.jpeg'
    //     defaultAddress.value = 'ZhongNan sea, Beijing, China'
    //     purchaseList.value = [1, 2, 3]
    //     reviewList.value = [3, 2, 3]
    // }, 200);

    const user_real_id = props.id || getCookieValue('user_id') || globalStore.userID
    axios.post(getUserInfoAPI, { user_id: user_real_id })
        .then((response) => {
            username.value = response.data.username
            gender.value = response.data.gender
            email.value = response.data.email
            contactNumber.value = response.data.contact_number
            registrationTime.value = new Date(response.data.registration_time)
            total_points.value = response.data.total_points
            avatarURL.value = response.data.user_avatar
            defaultAddress.value = response.data.default_address
            user_id.value = response.data.user_id
        })
        .catch((error) => {
            console.error(error.message);

        })

    const getProductReviewByUserID = '/api/review'
    axios.post(getProductReviewByUserID, { user_id: user_real_id })
        .then((response) => {

            let arr = []
            for (let i in response.data) {
                arr.push(response.data[i].review_id)
            }
            reviewList.value = arr
            
        })
        .catch((error) => {
            console.error(error.message);
        })

    const getUserBuyRecordAPI = '/api/user-order'
    axios.post(getUserBuyRecordAPI, { user_id: user_real_id })
        .then((response) => {
            console.log(response.data);
            
            let arr = []
            for (let i in response.data) {
                arr.push(response.data[i].product_id)
            }
            purchaseList.value = arr
            console.log('s2' + purchaseList.value)
        })
        .catch((error) => {
            console.error(error.message);
        })



}

const props = defineProps({
    id: Number
})


const genderToColor = (gender: String): String => {
    if (gender == "Male") return ""
    else if (gender == "Female") return "danger"
    else return "info"
}


const date2str = (date: Date): String => {
    return date.getFullYear() + '-' + date.getMonth() + '-'
        + date.getDay() + ' ' + date.getHours() + ':' + date.getSeconds()
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

.wrappar {
    padding: 5ex;
}

.margin-top {
    margin-top: 20px;
}

.cell-item {
    display: flex;
    align-items: center;
}</style>