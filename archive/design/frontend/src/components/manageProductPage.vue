<template>
    <div style="padding: 5ex;">
        <el-descriptions class="margin-top" title="Profile" :column="1" border>
            <template #extra>
                <router-link to="/">
                    <el-button>Back</el-button>
                </router-link>
                <el-button type="primary" style="margin-inline: 2ex;" @click="onSubmit">Submit</el-button>

            </template>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <ArrowRight />
                        </el-icon>
                        ID
                    </div>
                </template>
                {{ product_id }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Memo />
                        </el-icon>
                        Name
                    </div>
                </template>
                <el-input v-model="product_name"></el-input>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Pear />
                        </el-icon>
                        Description
                    </div>
                </template>
                <el-input type="textarea" v-model="product_description"></el-input>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Sell />
                        </el-icon>
                        Price
                    </div>
                </template>
                <el-input-number v-model="product_price"></el-input-number>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <TakeawayBox />
                        </el-icon>
                        Quantity
                    </div>
                </template>
                <el-input-number v-model="product_quantity"></el-input-number>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Money />
                        </el-icon>
                        Pt ratio
                    </div>
                </template>
                <el-input-number step="0.1" v-model="point_ratio"></el-input-number>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Menu />
                        </el-icon>
                        Type
                    </div>
                </template>
                <div>
                    <el-radio-group v-model="product_type">
                        <el-radio-button label="Electronics" />
                        <el-radio-button label="Fashion & Beauty" />
                        <el-radio-button label="Home & Kitchen" />
                        <el-radio-button label="Sports & Outdoors" />
                        <el-radio-button label="Other" />
                    </el-radio-group>
                </div>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Star />
                        </el-icon>
                        Recommended
                    </div>
                </template>
                <div>
                    <el-radio-group v-model="is_recommended">
                        <el-radio-button label="true" />
                        <el-radio-button label="false" />
                    </el-radio-group>
                </div>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Calendar />
                        </el-icon>
                        On-shelf date
                    </div>
                </template>
                <div>
                    {{ date2str(on_shelf_date) }}
                </div>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Picture />
                        </el-icon>
                        Image
                    </div>
                </template>
                <div style="display: flex; align-items: center;">
                    <el-avatar shape="square" :src="imageAPI + product_image"> </el-avatar>
                    <el-upload style="margin-left: 10ex;" :action="uploadImageAPI" :limit="1"
                        :on-success="onUploadAvatarSuccess" :on-error="onUploadAvatarError"
                        name="image"
                        >
                    </el-upload>
                </div>
            </el-descriptions-item>
        </el-descriptions>
    </div>
</template>

<script setup lang="ts">
import { Calendar, Pear } from "@element-plus/icons-vue";
import { onMounted, ref } from "vue"
import { ElUpload } from "element-plus";
import axios from "axios";

const product_id = ref(0)
const product_name = ref('')
const product_quantity = ref(0)
const product_price = ref(0)
const product_type = ref('Other')
const product_description = ref('')
const product_image = ref('')
const is_recommended = ref(false)
const on_shelf_date = ref(new Date())
const point_ratio = ref(10)

const uploadImageAPI = '/api/upload-image' // TODO: add API
const getProductAPI = 'api/product'
const imageAPI = '/api/image/'

const props = defineProps({
    id: Number
})

onMounted(() => {
    fetchInfo()
})

const fetchInfo = () => {
    if (!props.id) return
    axios.post(getProductAPI, { product_id: props.id })
        .then((response) => {
            product_id.value = response.data.product_id
            product_name.value = response.data.product_name
            product_quantity.value = response.data.product_quantity
            product_price.value = response.data.product_price
            product_type.value = response.data.product_type
            product_description.value = response.data.product_description
            is_recommended.value = response.data.is_recommended ? true : false
            point_ratio.value = response.data.point_ratio
            product_image.value = response.data.product_image
        })
}



const onUploadAvatarSuccess = (response: any) => {
    product_image.value = response.image_uuid
}

const onUploadAvatarError = () => {
    alert('Error')
}

const onSubmit = () => {
    // ...
    // alert(props.id)
    axios.post(props.id != undefined ? "/api/insert/product" : '/api/insert-product', {
        product_name: product_name.value || '',
        product_quantity: product_quantity.value || '1',
        product_price: product_price.value || '1',
        product_type: product_type.value || 'Other',
        product_description: product_description.value || '',
        is_recommended: is_recommended.value ? 1 : 0,
        point_ratio: point_ratio.value || '10',
        product_image: product_image.value || '',
        product_id: props.id != undefined ? props.id : 0
    })
        .then(_ => {
            alert('Success')
            window.location.reload()
        })
        .catch(error => {
            console.error(error.message);
        })
}


const date2str = (date: Date): String => {
    return date.getFullYear() + '-' + date.getMonth() + '-'
        + date.getDay() + ' ' + date.getHours() + ':' + date.getSeconds()
}
</script>

<style scoped></style>