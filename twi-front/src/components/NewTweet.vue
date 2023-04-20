<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <el-row :gutter="20">
          <el-col :span="8">
            <span><p><strong>撰写推文</strong></p></span>
          </el-col>
        </el-row>
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
           
           发送推文
          </el-button>
        </div>
      </div>
    </template>
  </el-card>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { ElNotification } from 'element-plus'

const tweetText = ref("");
const maxLength = 144; // 推文最大长度
const picsUploadedUrl = ref("")
const onSend = () => {
  alert(tweetText.value + '发送啦')
}


</script>

<style scoped>
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

.text {
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