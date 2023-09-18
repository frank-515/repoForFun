<template>
    <div class="page-container">
        <div class="detail-container">
            <div style="display: flex; padding: 5ex;">
                <div class="image-container">
                    <el-carousel height="300px">
                        <el-carousel-item v-for="item in images" :key="item" style="display: flex;">
                            <el-image :src="item" fit="cover"></el-image>
                        </el-carousel-item>
                    </el-carousel>
                </div>
                <div class="right-side">
                    <a style="font-size: larger; font-weight: bolder;">{{ product_name }}</a><br />
                    <a style="font-weight: bolder; color:gray;">{{ description }}</a>
                    <el-divider border-style="dashed" />
                    <a class="price-style">
                        {{ price2Str(price) }}</a>
                    <a style="color:gray; padding-right: 3ex;">{{ stock }} in stock</a>
                    <el-input-number v-model="amount" :min="1" :max="stock < 0 ? 1 : stock" />
                    <br />
                    <div style="display: inline-flex;">
                        <el-rate v-model="rating" disabled show-score score-template="Rating {value}" />
                        <a style="color:grey; padding-left: 3ex; padding-top: 0.4ex">({{ ratingCount }} rating)</a>
                    </div>

                    <br />
                    <a style="color:grey; padding-right: 3ex;">{{ date2str(onShelvesDate) }}</a>
                    <a style="color:grey;">{{ formatTimeSincePurchase(lastDealDate!) }}</a>
                    <el-divider border-style="dashed" />
                    <a style="font-size: larger;">{{ price2Str(price * amount) }}</a>
                    <a style="color: grey; padding: 3ex;">Can gain {{ Math.floor(pointRatio * price * amount) }}pt</a>
                    <el-button type="warning" @click="onBuy">Buy <el-icon>
                            <ShoppingBag />
                        </el-icon></el-button>
                    <el-button>Add to cart<el-icon>
                            <ShoppingCart />
                        </el-icon></el-button>
                </div>
            </div>

        </div>
        <el-divider content-position="left">Reviews</el-divider>

        <div style="display: flex;">
            <div
                style="width: 33%; margin-left: 5ex; background-color: #f1f1f1; padding: 2ex; border-radius: 1ex; border-style:groove;">
                <div style="display: flex; align-items: center;">
                    <el-icon>
                        <ArrowRight />
                    </el-icon>
                    <a style="font-size: larger; font-weight: bolder; padding-right: 3ex;">Write a comment</a>
                    <el-rate v-model="commentValue" /><br />
                </div>

                <br />
                <el-input v-model="text" maxlength="1000" placeholder="Discussing the product’s user experience"
                    show-word-limit :autosize="{ minRows: 2, maxRows: 6 }" type="textarea">
                </el-input><br />
                <div style="margin-top: 2ex; text-align: right;">
                    <el-button type="primary" @click="onSubmitReview">Submit</el-button>
                </div>
            </div>
            <div>
                <i class="slogen">
                    Your one-stop online superstore</i>
                <br />
                <i class="slogen">for all your shopping needs!</i>

            </div>
        </div>

        <div>
            <ul v-for="id in reviewIDList" :key="id">
                <ReviewCard :id="id"></ReviewCard>
            </ul>
        </div>
        <el-divider content-position="left">Best Sellers</el-divider>
        <div>
            <Showcase :list="recommendShowcase"></Showcase>
        </div>
    </div>

    <el-dialog v-model="paymentVisible" title="Purchase" width="40ex">
        <span style="display: flex; flex-direction: column; text-align: left;">
            <div style="align-items: center; display: flex;">
                <a>Scan QR Code or </a>
                <br /><br />
                <img style="width: 50px; padding-inline: 2ex;"
                    src="https://www.mastercard.us/content/dam/public/mastercardcom/na/us/en/homepage/Home/mc-logo-52.svg">
                <img style="width: 50px;" src="https://cdn.visa.com/v2/assets/images/logos/visa/blue/logo.png">
            </div>
            <div class="qrcode">
                <img src="https://qr.m.jd.com/show?appid=133&size=90" />
            </div>
            <el-divider></el-divider>
            Address:<el-input v-model="address" placeholder="Shopping address"></el-input>
            Number:<el-input v-model="contact_number" placeholder="Contact number"></el-input>

            <a style="font-size: 4ex; color:black; font-weight:900;">{{ price2Str(price * amount) }}</a>

        </span>
        <template #footer>
            <span class="dialog-footer">
                <el-button type="primary" @click="onPay">Done</el-button>
                <el-button type="danger" @click="() => { paymentVisible = false }">Back</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { ShoppingBag, ShoppingCart } from '@element-plus/icons-vue'
import Showcase from './showcase.vue'
import ReviewCard from './reviewCard.vue'
import axios from 'axios';
import { useGlobalStore } from '../pinia/globalStore'
import { storeToRefs } from 'pinia';

const globaleStore = useGlobalStore()

const { address, contact_number } = storeToRefs(globaleStore)

const images = ref<Array<String>>([])
const description = ref('')
const price = ref(0.00)
const rating = ref(0) //rating
const ratingCount = ref(0);
const stock = ref(1)
const onShelvesDate = ref(new Date())
const lastDealDate = ref(new Date())
const pointRatio = ref(0)
const reviewIDList = ref(new Array<number>(0))
const recommendShowcase = ref(new Array())
const is_recommended = ref(false)
const product_name = ref('')

const commentValue = ref(0) // 
const text = ref('') // text to comment


const amount = ref(1) // amount to buy
const paymentVisible = ref(false)

const props = defineProps({
    id: Number
})
props

onMounted(() => {
    fetchInfo()
})

const getProductInfoAPI = '/api/product'
const getProductReviewByProductID_API = '/api/review'
const imgAPI = '/api/image'

const fetchInfo = () => {
    // setTimeout(() => {
    //     description.value = 'Fetch information from the server'
    //     price.value = 100.89
    //     rating.value = 4.5
    //     images.value = [
    //         'https://sns-img-hw.xhscdn.net/1000g0081mu6a17oda0605ohaati8chd9dp7h3io?imageView2/2/w/1920/format/webp|imageMogr2/strip',
    //         'https://5b0988e595225.cdn.sohucs.com/images/20181202/c95d13af69a5498e8b9c840b13819f0b.jpeg'
    //     ]
    //     stock.value = 30
    //     ratingCount.value = 3;
    //     lastDealDate.value.setDate(lastDealDate.value.getDate() - 2);
    //     onShelvesDate.value = new Date()
    //     pointRatio.value = 10
    //     ratingStars.value = [1, 2, 3, 4, 5]
    //     reviewIDList.value = [1, 2, 3, 4, 5]
    //     recommendShowcase.value = [1, 2, 3, 4, 5, 6, 6, 7]
    // })
    axios.post(getProductInfoAPI, { product_id: props.id })
        .then((response) => {
            console.log(response.data);
            price.value = response.data.product_price
            description.value = response.data.product_description
            images.value = [imgAPI + '/' + response.data.product_image]
            stock.value = response.data.product_quantity
            rating.value = response.data.product_value | 4
            is_recommended.value = response.data.is_recommended
            onShelvesDate.value = new Date(response.data.on_shelf_date)
            pointRatio.value = response.data.point_ratio
            product_name.value = response.data.product_name
        })
        .catch(error => {
            console.error(error.message);
        })
    axios.post(getProductReviewByProductID_API, { product_id: props.id })
        .then((response) => {
            ratingCount.value = response.data.length;
            let arr = []
            for (let i in response.data) {
                arr.push(response.data[i].review_id)
            }
            reviewIDList.value = arr
        })
}

const buyAPI = '/api/buy'

const onPay = () => {
    axios.post('/api/buy-c', {
        product_id: props.id,
        count: amount.value,
    })
        .then(_ => {
            axios.post(buyAPI, {
                user_id: globaleStore.userID,
                product_id: props.id,
                address: address.value,
                contact_number: contact_number.value
            })
                .then((response) => {
                    const id = response.data.id;
                    window.location.href = '/#/shipping/' + id
                })
                .catch((error) => {
                    alert(error.message)
                })
        })
        .catch(err => {
            alert(err)
        })


}

const reviewSubmitAPI = '/api/write-review'

const onSubmitReview = () => {

    if (!text.value.length) {
        alert('You can not upload empty review')
    } else if (commentValue.value === 0) {
        alert('Please rate our products')
    } else {
        axios.post(reviewSubmitAPI, {
            user_id: globaleStore.userID,
            product_id: props.id?.valueOf(),
            content: text.value,
            star: commentValue.value
        })
            .then((response) => {
                if (response.status === 200) {
                    alert('successfully send your review, thanks!')
                    text.value = ''
                    commentValue.value = 0
                    window.location.reload()
                } else {
                    alert(response.data)
                }

            })

    }

}

const price2Str = (price: number) => {
    const formattedPrice = price.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD'
    });
    return formattedPrice;
};

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
        return "No purchases in the past week"
    } else if (days > 0) {
        return `${days} days ago last purchase`;
    } else if (hours > 0) {
        return `${hours} hours ago last purchase`;
    } else if (minutes > 0) {
        return `${minutes} minutes ago last purchase`;
    } else {
        return "Someone made a purchase just now";
    }
}
const onBuy = () => {
    paymentVisible.value = true;
}

const date2str = (date: Date): String => {
    return date.getFullYear() + '-' + date.getMonth() + '-'
        + date.getDay() + ' ' + date.getHours() + ':' + date.getSeconds()
}


</script>



<style scoped>
.detail-container {
    padding-left: 3vw;
}

.image-container {
    height: 300px;
    width: 300px;
    padding-right: 4ex;
}

.right-side {
    padding: 3ex;
    min-width: 400px;
}

.price-style {
    font-weight: bolder;
    color: darkorange;
    font-size: 4ex;
    font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
    padding-right: 1ex;
}

.page-container {
    max-width: 1280;
}

.slogen {
    font-size: 4ex;
    font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
    font-weight: 900;
    margin-left: 5ex;
    padding: 2ex;
    position: relative;
    top: 2ex;
}

.qrcode {}
</style>