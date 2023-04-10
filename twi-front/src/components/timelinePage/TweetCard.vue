<script setup lang="ts">
import default_avatar_url from "../../assets/default_avatar.png";
const default_avatar = document.createElement("img");

const props = defineProps({
  user_id: String,
  username: String,
  avatarUrl: String,
  text: String,
  imagesUrl: [String],
});

let text_resolve: string = props.text ? props.text : ""
const urls = [];
const regex = /\[img=([^\]]+)\]/g;
let match = null;
while ((match = regex.exec(text_resolve)) !== null) {
  // 提取url内容
  urls.push(match[1]);
}
// 删除所有匹配的内容
const text_resolved = text_resolve.replace(regex, "");
console.log(urls); // ["http://example.com/image.jpg", "http://example.com/image2.jpg"]
</script>

<template>
  <div class="tweet-wrapper">
    <div class="user-info-container">
      <div class="avatar-container"> 
        <el-avatar :src="props.avatarUrl ? props.avatarUrl : default_avatar_url">
        </el-avatar>
      </div>
      <div class="user-input-container">
        <div class="username">{{ username }}</div>
        <div class="user-id">
          <el-text size="small">@{{ user_id }}</el-text>
        </div>
      </div>
    </div>
    <div class="tweet-text">{{ text_resolved }}</div>
    <span :v-for="(url, index) in urls" :key="index">
      <el-image class="image-wrapper" :src="url" :fit="fit"></el-image>
    </span>
  </div>
</template>

<style scoped>
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

</style>