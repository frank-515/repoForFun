<script setup lang="ts">
import TimelineList from "./timelinePage/TimelineList.vue";
import { useUserStore } from "../store/globalStore";
import defaultAvatarUrl from "../assets/default_avatar.png";
import { Files, Plus, House, Search, Picture  } from "@element-plus/icons-vue";
import NewTweet from "./NewTweet.vue";
const userStore = useUserStore();
const username = userStore.state.user.username;
const avatarUrl = userStore.state.user.avatarUrl;

const toHome = () => {
  alert("To Home");
};
const toProfile = () => {
  alert("To Profile");
};
const newTweet = () => {
  alert("New Tweet");
};
</script>

<template>
  <div class="common-layout">
    <div class="ui-floating-label">
      <el-page-header @back="toHome" class="header-style">
        <template #content>
          <div class="header-layout" @click="toProfile">
            <el-avatar
              class="avatar-style"
              :src="avatarUrl ? avatarUrl : defaultAvatarUrl"
            >
            </el-avatar>
            <p>{{ username }}</p>
          </div>
        </template>
        <template #icon>
          <p class="home-logo" style="position: relative; bottom: 5px">🍍</p>
        </template>
        <template #title><div class="home-logo">Twine</div></template>
      </el-page-header>
      <div class="sidebar-wrapper">
        <el-menu
          default-active="timeline"
          :collapse="true"
          class="sidebar-style"
          :router="true"
        >
          <el-menu-item index="timeline"> <Files color="#fff" /></el-menu-item>
          <el-menu-item index="write"> <Plus color="#fff" /></el-menu-item>
          <el-menu-item index="home"> <House color="#fff" /></el-menu-item>
          <el-menu-item index="search"> <Search color="#fff" /> </el-menu-item>
          <el-menu-item index="picture"> <Picture color="#fff" /> </el-menu-item>
        </el-menu>
      </div>
    </div>
    <div class="content-under-floating">
      <div class="timeline-wrapper">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>



<style scoped>
.content-under-floating {
  position: relative;
  z-index: 1;
}
.sidebar-wrapper {
  position: fixed;
  left: 0px;
  top: 10ex;
}
.sidebar-style {
  backdrop-filter: blur(5px);
  background-color: rgba(255, 255, 255, 0.2);
}

.timeline-wrapper {
  z-index: 2;
  padding-top: 8ex;
  display: flex;
  justify-content: center; /* 子元素水平居中 */
  align-items: center; /* 子元素垂直居中 */
}
.home-logo {
  font-size: 30px;
  display: inline-flex;
  font-weight: 500;
}
.avatar-style {
  top: 8px;
  position: relative;
  padding-left: 1ex;
  padding-right: 1ex;
}
.header-style {
  backdrop-filter: blur(5px);
  background-color: rgba(255, 255, 255, 0.2);
  position: fixed;
  top: 0px;
  left: 0px;
}
.header-layout {
  width: 100vw;
  display: inline-flex;
  padding: 1ex;
}
.ui-floating-label {
  position: relative;
  z-index: 999;
}

.common-layout {
  height: 100vh;
  width: 100vw;
  background-color: #1c1c1c;
  color: #ffffff;
}
.common-layout p {
  color: #ffffff;
  padding-left: 1ex;
  padding-right: 1ex;
}
</style>