
<script setup lang="ts">
import TweetCard from "./TweetCard.vue";
import { defineProps } from 'vue';

interface Tweet {
  user_id: string;
  username: string;
  avatarUrl: string | undefined;
  text: string;
  imagesUrl: string[] | undefined;
}

const props = defineProps({
  tweets: {
    type: Array as () => Tweet[],
    default: () => [
      {
        user_id: '',
        username: '',
        avatarUrl: '',
        text: '',
        imagesUrl: [],
      },
    ],
  },
});

const onLoad = () => {
  console.log('Infinite scrolling');
};
</script>

<template>
  <div>
    <ul v-infinite-scroll="onLoad" v-for="item in props.tweets" :key="item.user_id">
      <span class="tweet-wrapper">
        <TweetCard :user_id="item.user_id" :username="item.username" :text="item.text" :avatarUrl="item.avatarUrl" />
      </span>
    </ul>
  </div>
</template>

<style scoped>
.tweet-wrapper {
  width: auto;
  min-width: 300px;
  max-width: 400px;
  padding: 5px;
  border-radius: 5px;
  text-align: left;
  word-wrap: break-word;
}
</style>