
<template>
    <div>
      <ul class="tweets-list" v-infinite-scroll="onLoad" infinite-scroll-delay="1000" infinite-scroll-distance="300">
        <span class="tweet-wrapper" v-for="item in tweets" :key="item.user_id">
          <TweetCard :user_id="item.user_id" :username="item.username" :text="item.text" :avatarUrl="item.avatarUrl" />
        </span>
      </ul>
    </div>
  </template>
  
  <script setup lang="ts">
  import TweetCard from "../timelinePage/TweetCard.vue";
  import { defineProps, ref } from 'vue';
  
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
  
  const tweets = ref(props.tweets);
  
  const onLoad = async () => {
    // 发起异步请求获取新的 tweets 数据
    const newTweets = await fetchNewTweets();
    
    // 将新数据合并到现有的 tweets 数组中
    tweets.value.push(...newTweets);
  };
  //修改这个获取新推文，高耦合警告⚠️
  const fetchNewTweets = async(): Promise<Tweet[]> => {
    // 发起异步请求获取新的 tweets 数据
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        resolve([
          {
            user_id: 'user',
            username: 'frank515',
            text: 'test',
            avatarUrl: '',
            imagesUrl: ['']
          },
        ]);
      }, 1000);
    })
  };
  
  </script>
  
  <style scoped>
  .tweet-wrapper {
    width: auto;
    min-width: 300px;
    padding: 5px;
    border-radius: 5px;
    text-align: left;
    word-wrap: break-word;
  }
  
  .tweets-list {
    list-style: none;
    margin: 0;
    padding: 0;
  }

  
  </style>