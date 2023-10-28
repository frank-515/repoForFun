<script setup lang="ts">
import UserProfile from "./UserProfile.vue";
import UserHomeTimeline from "./UserHomeTimeline.vue";
import { useRoute } from "vue-router";
import { useUserStore } from "../../store/globalStore";

const route = useRoute()

const user_id = String(route.params.user_id)


</script>

<template>
  <div class="outer-container">
    <UserProfile :user_id="user_id"></UserProfile>
    <div class="profile-style">
      <UserHomeTimeline :user_id="user_id"></UserHomeTimeline>
    </div>
    
  </div>
</template>

<style scoped>
.profile-style {
  display: flex;
  justify-content: center; /* 子元素水平居中 */
  align-items: center; /* 子元素垂直居中 */

}

</style>