<script setup lang="ts">
import UserProfile from "./UserProfile.vue";
import UserHomeTimeline from "./UserHomeTimeline.vue";

const exampleTweets = [
	{user_id: "user", username: "user", text: "text here", avatarUrl: undefined, imagesUrl: undefined},
	{user_id: "user", username: "user", text: "text here", avatarUrl: undefined, imagesUrl: undefined},
	{user_id: "user", username: "user", text: "text here", avatarUrl: undefined, imagesUrl: undefined},
	{user_id: "user", username: "user", text: "text here", avatarUrl: undefined, imagesUrl: undefined}
]

</script>

<template>
  <div class="outer-container">
    <UserProfile></UserProfile>
    <div class="profile-style">
      <UserHomeTimeline></UserHomeTimeline>
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