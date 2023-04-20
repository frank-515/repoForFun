<script setup lang="ts">
// import TweetCard from './components/timelinePage/TweetCard.vue'
// import TimelineList from './components/timelinePage/TimelineList.vue';
// import UserProfile from './components/homePage/UserProfile.vue';
// import UserHomePage from './components/homePage/UserHomePage.vue'
import NewTweet from './components/NewTweet.vue';
</script>

<template>
  <div class="wrapper">
    <NewTweet></NewTweet>
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  top: 0;
  left: 0;
  min-width: 640px;
  width: 100%;
  height: 100%;
  background-color: #1c1c1c;
  color: #ffffff;
}

</style>
