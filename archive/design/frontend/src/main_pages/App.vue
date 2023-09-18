<template>
  <el-page-header :icon="IceCream">
    <template #breadcrumb v-if="isAdmin">
      <div
        style="background-color: yellow; border-style: dashed; padding: 1ex; align-items: center; display: flex; border-radius: 1ex;">
        <i style="font-weight: bolder;">You are logging in as an administrator account</i>
        <span style="flex: 1;"></span>
        <router-link to="/a/admin">
          <el-button>Manage</el-button>
        </router-link>

      </div>

    </template>

    <template #title>

      <router-link to="/">
        <el-tag round><b>AAAclub</b></el-tag>
      </router-link>

    </template>
    <template #content>
      <div class="flex-center">
        <router-link to="/user" style="text-decoration: none; font-weight: bold;">{{ getCookieValue('username') }}</router-link>
        <span style="padding-inline: 1ex;">
          <el-tag type="warning" round>{{ getUserLevelString(level) }}</el-tag>
        </span>
      </div>

    </template>

    <template #extra>

      <div class="flex-center">
        <el-input style="padding-inline: 2ex;" v-model="searchKeyword">
          <template #prepend>
            <el-button :icon="Search" @click="search"/>
          </template>
        </el-input>
        <el-button type="primary">
          <el-icon>
            <ShoppingCart />
          </el-icon>
        </el-button>

        <router-link to="/order">
          <el-button type="primary" style="margin-inline: 3ex;">
            <el-icon>
              <Goods />
            </el-icon>
          </el-button>
        </router-link>

        <el-button type="danger" style="padding-inline: 1ex;" @click="exitAccount()">Exit account</el-button>


        <router-link to="/user">
          <div style="padding-inline: 1ex;">
            <el-avatar :size="36" :src="'/api/image/' + globaleStore.avatarURL" />
          </div>
        </router-link>
      </div>


    </template>

    <!-- Lines after 2 -->
    <template #default>
      <router-view></router-view>
    </template>

  </el-page-header>
  <div style="text-align: center; color: #aaa; margin: 8ex;">
    AAAclub, All rights reserved, 2023, Copyright by Frank 515.
  </div>
  <div :v-if="!getCookieValue('user_id')">
    <LoginNRegister></LoginNRegister>
  </div>
</template>



<script setup lang="ts">

import { IceCream, ShoppingCart, Goods, Search } from '@element-plus/icons-vue'
import { useGlobalStore } from '../pinia/globalStore'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'
import LoginNRegister from '../components/loginNRegister.vue'

const globaleStore = useGlobalStore()

const { level, isAdmin } = storeToRefs(globaleStore)

const searchKeyword = ref('')
const search = () => {
  window.location.href = '/#/search/' + searchKeyword.value
}

onMounted(() => {

  if (getCookieValue('user_id')) {
    globaleStore.fetchUserInfo(getCookieValue('user_id')!)
  }

})

const getUserLevelString = (level: number) => {
  switch (level) {
    case 1: return 'v1';
    case 2: return 'v2'
    case 3: return 'v3'
    case 4: return 'v4'
    case 5: return 'v5'
    default:
      return 'v0'
  }
}

function clearAllCookies() {
  var cookies = document.cookie.split(";");

  for (var i = 0; i < cookies.length; i++) {
    var cookie = cookies[i];
    var eqPos = cookie.indexOf("=");
    var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/";
  }
}

const exitAccount = () => {
  window.location.reload()
  clearAllCookies()
  globaleStore.reset()
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