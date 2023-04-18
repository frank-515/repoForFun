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
    <UserHomeTimeline></UserHomeTimeline>
  </div>
</template>

<style scoped>


</style>