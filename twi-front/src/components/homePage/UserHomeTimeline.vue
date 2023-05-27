<template>
  <div>
    <el-empty v-if="!getTweetVal()[0]" 
      :image-size="200"
      description="Nothing there! Share your life!"/>
    <ul
      class="tweets-list"
      v-infinite-scroll="onLoad"
      infinite-scroll-delay="100"
      infinite-scroll-distance="300"
      infinite-scroll-immediate="true"
      :v-if="tweets.length > 0"
    >
    
      <li class="tweet-wrapper" v-for="item in getTweetVal()" :key="item.user_id">
        <TweetCard
          :user_id="item.user_id"
          :tweet_id="item.tweet_id"
          :username="item.username"
          :text="item.text"
          :time="item.time"
          :avatarUrl="item.avatarUrl"
          :starCount="item.starCount"
          :replyCount="item.replyCount"
          :retweetCount="item.retweetCount"
        />
        
      </li>
      
    </ul>
  </div>
</template>

<script setup lang="ts">
import TweetCard from "../timelinePage/TweetCard.vue";
import { createRenderer, defineProps, ref } from "vue";
import { useUserStore } from "../../store/globalStore";
import { getCookieValueByName } from "../../util";
import axios from "axios"

const props = defineProps({
  user_id: String
})

// let user_id = props.user_id ? props.user_id : ''
// const userStore = useUserStore();
// userStore.fetchInfo(() => {
//   if (!user_id) user_id = userStore.state.user.user_id;
// })



interface Tweet {
  user_id: string;
  tweet_id: string;
  time: Date;
  username: string;
  avatarUrl: string | undefined;
  text: string;
  imagesUrl: string[] | undefined;
  replyCount: number;
  starCount: number;
  retweetCount: number;
}



const tweets = ref<Tweet[]>([]);
const getTweetVal = () =>{
  return tweets.value;
}
let currPage :number = 1;
let hasNextPage :boolean = true;
const onLoad = async () => {
  // 发起异步请求获取新的 tweets 数据
  // 当有下一页时一直请求
  while (hasNextPage) {
    const newTweets = await fetchNewTweets();
  // 将新数据合并到现有的 tweets 数组中
    tweets.value.push(...newTweets);
    currPage++
  }
};


const fetchNewTweets = async (): Promise<Tweet[]> => {
  const user_id = () => {
    if (props.user_id == undefined || props.user_id == '' || props.user_id == 'undefined') {
      return getCookieValueByName("user_id")!
    } else {
      return props.user_id
    }
  }
  // 发起异步请求获取新的 tweets 数据
  return new Promise((resolve, reject) => {
    const timelineAPI = '/api/u/' + user_id() + '/tweets/' // full URI: '/api/u/:user_id/tweets/:page'
    
    axios.get(timelineAPI + currPage)
      .then((response) => {
        const data = response.data
        const tweetsData = data.tweets
        hasNextPage = data.hasNextPage
        resolve(tweetsData)
      })
      .catch((error) => {
        console.log(error);
      })
       
  });
};

</script>

<style scoped>

.tweet-wrapper {
  min-width: 300px;
  width: 50vw;
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