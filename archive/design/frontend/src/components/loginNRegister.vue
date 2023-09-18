<template>
    <el-dialog v-model="visible" width="50%" :show-close="false" draggable="true" close-on-click-modal="false" close-on-press-escape="false">
        <template #header>
            <el-radio-group v-model="loginOrRegister" label="label position">
                <el-radio-button label="login">Login</el-radio-button>
                <el-radio-button label="register">Register</el-radio-button>
            </el-radio-group>
        </template>
        <span>
            <div v-if="loginOrRegister === 'login'">
                <el-form :model="registerForm" label-width="120px" label-position="top">
                    <el-form-item label="Username">
                        <el-input v-model="loginForm.username" />
                    </el-form-item>
                    <el-form-item label="Password">
                        <el-input type="password" v-model="loginForm.password" />
                    </el-form-item>
                </el-form>
            </div>
            <div v-if="loginOrRegister === 'register'">
                <el-form :model="registerForm" label-width="120px" label-position="left">
                    <el-form-item label="Username">
                        <el-input v-model="registerForm.username" />
                    </el-form-item>
                    <el-form-item label="Password">
                        <el-input type="password" v-model="registerForm.password" />
                    </el-form-item>
                    <el-form-item label="E-mail">
                        <el-input v-model="registerForm.email" />
                    </el-form-item>
                    <el-form-item label="Gender">
                        <el-radio-group v-model="registerForm.gender" label="label position">
                            <el-radio-button label="Male">Male</el-radio-button>
                            <el-radio-button label="Female">Female</el-radio-button>
                            <el-radio-button label="Other">Other</el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="Number">
                        <el-input v-model="registerForm.contact_number" />
                    </el-form-item>
                    <el-form-item label="Default address">
                        <el-input v-model="registerForm.default_address" />
                    </el-form-item>
                    <el-form-item label="Account type">
                        <el-radio-group v-model="registerForm.is_admin" label="label position">
                            <el-radio-button label="false">User</el-radio-button>
                            <el-radio-button label="true">Administrator</el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </div>
        </span>
        <template #footer>
            <span>
                <el-button type="primary" @click="loginOrRegister === 'login' ? onLogin() : onRegister()">
                    {{ loginOrRegister === 'login' ? 'Sign in' : 'Sign up' }}
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import axios from 'axios';
import { useGlobalStore } from '../pinia/globalStore'
import { storeToRefs } from 'pinia';

const globaleStore = useGlobalStore()
const { username } = storeToRefs(globaleStore)

const loginOrRegister = ref('login')
const registerForm = reactive({
    username: '',
    gender: '',
    email: '',
    contact_number: '',
    password: '',
    default_address: '',
    is_admin: false,
})

const loginForm = reactive({
    username: '',
    password: ''
})

const visible = ref(true)

onMounted(() => {
    getCookieValue('user_id')
})

const loginAPI = '/api/login'
const registerAPI = '/api/register'

function getCookieValue(cookieName: string) {
  var cookies = document.cookie.split(";");

  for (var i = 0; i < cookies.length; i++) {
    var cookie = cookies[i].trim();

    if (cookie.indexOf(cookieName + "=") === 0) {
        visible.value = false;
      return cookie.substring(cookieName.length + 1, cookie.length);
    }
  }

  return null;
}


const onLogin = () => {
    // ...
    axios.post(loginAPI, { username: loginForm.username, password: loginForm.password })
        .then((response) => {
            if (response.data.result === 'success') {
                
                visible.value = false
                username.value = loginForm.username
                globaleStore.fetchUserInfoByName(loginForm.username)
                alert('Login successful')
            } else {
                alert('Login failed')
            }
        })
        .catch((error) => {
            alert('Error: ' + error.message)
        })

}

const onRegister = () => {
    axios.post(registerAPI, {
        username: registerForm.username,
        password: registerForm.password,
        default_address: registerForm.default_address,
        contact_number: registerForm.contact_number,
        email: registerForm.email,
        gender: registerForm.gender,
        is_admin: registerForm.is_admin
    })
    .then((response) => {
        if (response.data.status === 'success') {
            alert('Registration successful')
            loginOrRegister.value = 'login'
            username.value = response.data.username
            globaleStore.fetchUserInfoByName(username.value)
        } else {
            alert('Registration failed')
        }
    })
    .catch((error) => {
        alert(error.message)
    })

}
</script>

<style scoped></style>