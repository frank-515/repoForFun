<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
                    
        <span v-if="props.replyToUserID.length !== 0" ><p><strong>Reply to @{{ props.replyToUserID }}</strong></p></span>
            <span v-if="props.replyToUserID.length === 0" ><p><strong>Write new tweet</strong></p></span>

      </div>
    </template>
    <template #default>
      <div class="text">
        <el-input
          id="input"
          v-model="tweetText"
          :rows="6"
          :autosize="{ minRows: 2, maxRows: 6 }"
          type="textarea"
					maxlength="maxLength"
          placeholder="分享新鲜事..."
        />
        <div class="status-bar">
          <div class="word-count"> {{ tweetText.length }} / {{ maxLength }} </div>
          <span style="flex: 1;"></span>
          <el-button class="pic-button" type="primary" round style="width: 15px;">🏞️</el-button>
          <el-button class="send-button"
           type="primary" 
           round :disabled="tweetText.length === 0"
           :onClick="onSend">
           Post
          </el-button>
          
        </div>
      </div>
    </template>
  </el-card>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import axios, { AxiosResponse } from 'axios'
import { useUserStore } from "../store/globalStore";
const userStore = useUserStore()

const props = defineProps({
  replyToTweet: {
    type: String,
    default: "",
  },
  replyToUserID: {
    type: String,
    default: ""
  }
});

const tweetText = ref("");
const maxLength = 144; // 推文最大长度
const picsUploadedUrl = ref("")
const onSend = () => {
  const sendAPI = '/api/a/post';
  console.log(tweetText.value);
  
  const postData = {
    tweet_text: tweetText.value,
    sender_id: userStore.state.user.user_id,
    prev_tweet_id: null
  }
  //给后端传输数据
  axios.post(sendAPI, postData)
    .then((response) => {
      if (response.status == 200) {
        alert('成功发送')
      }
    })
    .catch((error) => {
      alert(error.message)
    })

}


</script>

<style scoped>
.card-header {
  display: flex;

}
.word-count {
  margin: 20px;
  color: #888888;
  font-size: smaller;
  position: relative;
  top: 1ex;
}
.status-bar {
  display: flex;
}
.send-button {
  margin-top: 20px;
  margin-right: 20px;
}
.pic-button {
  margin-top: 20px;
}

.box-card {
  background: rgba(0, 0, 0, 0.5);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(9px);
  -webkit-backdrop-filter: blur(9px);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.18);
  width: 480px;
}
.box-card p {
  color: gainsboro;
}


.text p {
  color: gainsboro;
}
.button {
  background-color: dodgerblue;
  margin: 6px;
  padding: 8px 8px;
  text-align: center;
  display: block;
}
.input-field {
	background-color: #555555;
}
</style>