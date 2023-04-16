<script setup lang="ts">
// import TweetCard from './components/timelinePage/TweetCard.vue'
// import TimelineList from './components/timelinePage/TimelineList.vue';
import UserProfile from './components/homePage/UserProfile.vue';

</script>

<template>
  <div class="wrapper">
    <UserProfile></UserProfile>
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 50%;
  height: 100%;
}

</style>
