<script setup lang="ts">
// import TweetCard from './components/timelinePage/TweetCard.vue'
import TimelineList from './components/timelinePage/TimelineList.vue';
let tweets = [
  {user_id: 'user', text: 'test', username: 'frank515', imagesUrl: [''], avatarUrl: ''},
  {user_id: 'user', text: 'test', username: 'frank515', imagesUrl: [''], avatarUrl: ''},
]

</script>

<template>
  <div class="timeline-wrapper">
    <TimelineList :tweets="tweets"></TimelineList>
  </div>
</template>

<style scoped>

</style>
