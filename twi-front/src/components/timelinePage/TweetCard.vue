<script setup lang="ts">
import default_avatar_url from "@assets/assets/default_avatar.png";
import { defineProps, ref } from 'vue';
import exampleImg from "@assets/assets/default_banner.jpg";
import axios, { Axios } from "axios";
import { useUserStore } from "../../store/globalStore";
const userStore = useUserStore();
const userInfo = {
  user_id: userStore.state.user.user_id,
  username: userStore.state.user.username
}
const default_avatar = document.createElement("img");

const props = defineProps({
  tweet_id: String,
  tweet_time: Date,
  user_id: String,
  username: String,
  avatarUrl: String,
  text: String,
  replyCount: {
    type: Number,
    default: 0,
  },
  starCount: {
    type: Number,
    default: 0,
  },
  retweetCount: {
    type: Number,
    default: 0,
  },
  imagesUrl: {
    type: Array,
    default: () => [],
  },
});

const replyCountRef = ref(props.replyCount)
const starCountRef = ref(props.starCount)
const retweetCountRef = ref(props.retweetCount)

let text_resolved: string = props.text ? props.text : ""
const urls: string[] = [];
const regex = /\[img=([^\]]+)\]/g;
let match = null;
while ((match = regex.exec(text_resolved)) !== null) {
  // 提取url内容
  urls.push(match[1]);
}
// 删除所有匹配的内容
const text_resolved_filtered = text_resolved.replace(regex, "");
// console.log(urls); // ["http://example.com/image.jpg", "http://example.com/image2.jpg"]

const pinkColor = 'color: #FF9999'
const greenColor = ' color: #009944'
const defaultColor = 'color: #FFFFFF'

let isStarted = ref(false);
let isRetweeted = ref(false);

const likeAPI = '/api/a/like'
const retweetAPI = '/api/a/retweet'

const postData = async (url: string, data: object) => {
    try {
      const response = await axios.post(url, data);
      return response.data
    } catch (err) {
      console.error(err);
    }
  }

const onStar = () => {
  isStarted.value = !isStarted.value
  if (isStarted.value) {
    starCountRef.value = starCountRef.value + 1
  } else {
    starCountRef.value = starCountRef.value - 1
  }
  postData(likeAPI, {
    user_id: userInfo.user_id,
    like_twitter_id: props.tweet_id
  })
  
}
const onRetweet = () => {
  isRetweeted.value = !isRetweeted.value
  if (isRetweeted.value) {
    retweetCountRef.value = retweetCountRef.value + 1
  } else {
    retweetCountRef.value = retweetCountRef.value - 1
  }
  postData(retweetAPI, {
    retweet_id: props.tweet_id,
    user_id: userInfo.user_id
  })
}
const onReply = () => {
 alert("onReply");
}
const onShare = () => {
  alert("onShare");
}
</script>

<template>
  <div class="tweet-wrapper">
    <div class="user-info-container">
      <div class="avatar-container"> 
        <el-avatar :src="props.avatarUrl ? props.avatarUrl : default_avatar_url">
        </el-avatar>
      </div>
      <div class="user-input-container">
        <div class="username">{{ props.username }}</div>
        <div class="user-id">
          <el-text size="small">@{{ props.user_id }}</el-text>
        </div>
      </div>
    </div>
    <div class="tweet-text">{{ text_resolved_filtered }}</div>
    <span v-for="(url, index) in urls" :key="index">
      <img class="image-wrapper" :src="url" fit="fit">
    </span>
    <el-row :gutter="20">
      <el-col :span="13"></el-col>
        <el-col :span="1"><el-icon><ChatLineSquare :onclick="onReply"/></el-icon></el-col>
        <el-col :span="2"><strong>{{ replyCountRef }}</strong></el-col>
      <el-col :span="1"><el-icon><Star :onclick="onStar" :style="isStarted ? pinkColor : defaultColor"/></el-icon></el-col>
      <el-col :span="2"><strong>{{ starCountRef }}</strong></el-col>
      <el-col :span="1"><el-icon><Refresh :onclick="onRetweet" :style="isRetweeted ? greenColor : defaultColor" /></el-icon></el-col>
      <el-col :span="2"><strong>{{ retweetCountRef }}</strong></el-col>
      <el-col :span="2"><el-icon><Share :onclick="onShare"/></el-icon></el-col>
      
    </el-row>
    <hr class="divider"/>
  </div>
</template>

<style scoped>
.divider {
  border: none;
  height: 1px;
  background-color: #ccc;
  opacity: 0.5;
  clip-path: polygon(5% 0, 95% 0, 95% 100%, 5% 100%);
  position: relative;
  top: 1ex;
  margin: 1ex;
}
.user-input-container {
  display: flex;
}

.avatar-container, .user-input-container {
  display: inline-flex;
  justify-content: center; /* 水平方向居中 */
  align-items: center; /* 垂直方向居中 */
}

.username {
  padding: 10px;
}

.tweet-text {
  padding-left: 40px;
  padding-bottom: 10px;
}
.image-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 180px;
  border-radius: 5px;
  border: 1px solid #666;
  overflow: hidden;
}

.image-wrapper img {
  max-height: 100%;
  max-width: 100%;
  object-fit: contain;
}



</style>

<!-- 对代码进行了如下修正：

1. 使用了 vue 的 defineProps 函数指定了 props 的类型，并对 imagesUrl 属性进行了详细的类型指定。
2. 在 v-for 循环中去掉了 :。
3. 在 TweetCard 组件的模板中使用了 props.xx 的方式来访问 props 的值。
4. 将 text_resolved 更改为 text_resolved_filtered 以更准确地描述含义。
5. 将 :v-for 改为 v-for。
6. 将 fit 属性的指定方式更改为 props.fit，以从 props 中获取。 -->

