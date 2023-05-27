<template>
  
<div class="outter-wrapper">
	<el-form :model="form" label-width="120px" size="large">
    <el-form-item label="User ID">
      <el-input v-model="form.user_id" :disabled="true">
      </el-input>
    </el-form-item>
    <el-form-item label="Join Date">
      <el-input v-model="form.join_date" :disabled="true">
      </el-input>
    </el-form-item>
    <el-form-item label="Username">
      <el-input v-model="form.username">
      </el-input>
    </el-form-item>
    <el-form-item label="Location">
      <el-input v-model="form.location">
      </el-input>
    </el-form-item>
    <el-form-item label="Birth">
      <el-date-picker
      v-model="form.birth_date"
      type="date"
      label="Pick a date"
      placeholder="Pick a date"
      style="width: 100%"
    />
    </el-form-item>
    <el-form-item label="URL">
      <el-input v-model="form.personal_url" type="textarea" />
    </el-form-item>
    <el-form-item label="Avatar">
      <el-upload 
      class="avatar-uploader"
      :action="uploadImageAPI"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      name="image"
      :data="{user_id: form.user_id}"
      >
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
    </el-form-item>
    <el-form-item label="Banner">
      <el-upload 
      class="avatar-uploader"
      :action="uploadImageAPI"
      :show-file-list="false"
      :on-success="handleBannarSuccess"
      name="image"
      :data="{user_id: form.user_id}"
      >
        <img v-if="form.bannerURL" :src="form.bannerURL" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit" style="align-items: center; display:flex;">Submit</el-button>
    </el-form-item>
  </el-form>
</div>
  
</template>

<script lang="ts" setup>

import { reactive, ref } from 'vue';
import { useUserStore } from '../../store/globalStore';
import { Plus } from '@element-plus/icons-vue';
import type { UploadProps } from 'element-plus'
import axios from 'axios';

const uploadImageAPI = '/api/a/upload-image'
const imageAPI = '/api/images/' // + :uuid
const setAvatarAPI = '/api/a/set-avatar' // image_name
const setBannerAPI = '/api/a/set-banner' // image_name
const setBioAPI = '/api/a/set-bio' // bio
const setUsernameAPI = '/api/a/set-username' // username
const setLocationAPI = '/api/a/set-location' // location
const setBirthAPI = '/api/a/set-birth' // birth
const setPersonalURLAPI = '/api/a/set-personal-url' // personal_url


const userStore = useUserStore();
const imageUrl = ref('');
const bannarUrl = ref('');
const form = reactive({
  user_id: '',
  username: '',
  avatarUrl: '',
  bio: '',
  bannerURL: '',
  following: 0,
  followers: 0,
	tweets: 0,
	likes: 0,
  join_date: '',
  location: '',
  personal_url: '',
  birth_date: '',

})



const handleAvatarSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile
) => {
  imageUrl.value = URL.createObjectURL(uploadFile.raw!)
  form.avatarUrl = imageAPI + response['uuid']
}

const handleBannarSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile
) => {
  bannarUrl.value = URL.createObjectURL(uploadFile.raw!)
  form.bannerURL = imageAPI + response['uuid']
}



const refreshUserInfo = () => {
  userStore.fetchInfo(() => {
    form.username = userStore.state.user.username;
    form.bannerURL = userStore.state.user.banner;
    form.bio = userStore.state.user.bio;
    form.followers = userStore.state.user.followers
    form.following = userStore.state.user.following
    form.user_id = userStore.state.user.user_id
    form.join_date = userStore.state.user.join_date
    form.avatarUrl = userStore.state.user.avatarUrl
    form.location = userStore.state.user.location
    form.personal_url = userStore.state.user.personal_url
    form.birth_date = userStore.state.user.birth_date
  });
}

refreshUserInfo()

const onSubmit = () => {
  axios.post(setUsernameAPI, {username: form.username})
  axios.post(setBioAPI, {bio: form.bio})
  axios.post(setBannerAPI, {image_name: form.bannerURL})
  axios.post(setAvatarAPI, {image_name: form.avatarUrl})
  axios.post(setLocationAPI, {location: form.location})
  axios.post(setBirthAPI, {birth: form.birth_date})
  axios.post(setPersonalURLAPI, {personal_url: form.personal_url})
  setTimeout(() => {
    location.reload()
  }, 100);
  
}

</script>

<style scoped>
.outter-wrapper {
  align-items: center;
  display: flex;
  justify-content: center;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
