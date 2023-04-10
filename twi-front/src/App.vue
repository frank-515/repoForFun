<script setup lang="ts">
// import TweetCard from './components/timelinePage/TweetCard.vue'
import TimelineList from './components/timelinePage/TimelineList.vue';
const tweets = [
  {user_id: 'user', text: 'test', username: 'frank515'},
  {user_id: 'user', text: 'test', username: 'frank515'},
  {user_id: 'user', text: 'tessadss22weqsladqwasjdijasdijwqisjdiqwjasdoiqwjdsadoaisjdiojasoidjaiodjioasjdttessadss22weqsladqwasjdijasdijwqisjdiqwjasdoiqwjdsadoaisjdiojasoidjaiodjioasjdttessadss22weqsladqwasjdijasdijwqisjdiqwjasdoiqwjdsadoaisjdiojasoidjaiodjioasjdt', username: 'frank515'},
  {user_id: 'user', text: 'test', username: 'frank515'},
  {user_id: 'user', text: 'test', username: 'frank515'},
]
</script>

<template>
  <div class="timeline-wrapper">
    <TimelineList :tweets="tweets"></TimelineList>
  </div>
</template>

<style scoped>

</style>
