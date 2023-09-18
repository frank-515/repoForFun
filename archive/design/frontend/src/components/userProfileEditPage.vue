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
                            <user />
                        </el-icon>
                        Username
                    </div>
                </template>
                <el-input v-model="username"></el-input>
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
                <el-input v-model="contactNumber"></el-input>
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
                <el-input v-model="defaultAddress"></el-input>
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
                <el-input v-model="email"></el-input>
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
                <div>
                    <el-radio-group v-model="gender">
                        <el-radio-button label="Male" />
                        <el-radio-button label="Female" />
                        <el-radio-button label="Other" />
                    </el-radio-group>
                </div>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Avatar />
                        </el-icon>
                        Gender
                    </div>
                </template>
                <div style="display: flex; align-items: center;">
                    <el-avatar shape="square" :src="'/api/image/' + avatarURL"> </el-avatar>
                    <el-upload style="margin-left: 10ex;" :action="uploadImageAPI" :limit="1" name="image"
                        :on-success="onUploadAvatarSuccess" :on-error="onUploadAvatarError">
                    </el-upload>
                </div>
            </el-descriptions-item>
        </el-descriptions>
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { ElRadioGroup, ElRadioButton, ElInput, ElUpload } from 'element-plus';
import axios from 'axios';

const username = ref('')
const gender = ref('')
const email = ref('')
const contactNumber = ref('')
const avatarURL = ref('')
const defaultAddress = ref('')

const uploadImageAPI = "/api/upload-image"

onMounted(() => {
    fetchInfo()
})
const user_id = getCookieValue('user_id')
const fetchInfo = () => {
    // setTimeout(() => {
    //     username.value = 'frank515'
    //     gender.value = 'Other'
    //     email.value = 'example@example.com'
    //     contactNumber.value = '+99 213881323'
    //     avatarURL.value = 'https://5b0988e595225.cdn.sohucs.com/images/20181202/c95d13af69a5498e8b9c840b13819f0b.jpeg'
    //     defaultAddress.value = 'ZhongNan sea, Beijing, China'
    // }, 200);
    const getUserInfoAPI = '/api/user'
    axios.post(getUserInfoAPI, { user_id: user_id })
        .then((response) => {
            username.value = response.data.username
            gender.value = response.data.gender
            email.value = response.data.email
            contactNumber.value = response.data.contact_number
            avatarURL.value = response.data.user_avatar
            defaultAddress.value = response.data.default_address
        })
        .catch((error) => {
            console.error(error.message);

        })
}

const onUploadAvatarSuccess = (response: any) => {
    avatarURL.value = response.image_uuid
}

const onUploadAvatarError = () => {
    alert('Error')
}

// /api/update-user-info
const updateAPI = '/api/update-user-info'
const onSubmit = () => {
    // ...
    axios.post(updateAPI, {
        user_id: getCookieValue('user_id'),
        username: username.value,
        gender: gender.value,
        email: email.value,
        contact_number: contactNumber.value,
        default_address: defaultAddress.value,
        user_avatar: avatarURL.value || ''
    }).catch(error => {
        console.error(error.message);

    }).then(() => {
        alert('Success')
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

<style scoped></style>