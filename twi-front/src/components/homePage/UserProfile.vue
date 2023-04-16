<script setup lang="ts">
import defaultBannerURL from "@/assets/default_banner.jpg"
import defaultAvatarURL from "@/assets/default_avatar.png"

interface userProfileType {
  user_id: String,
  username: String,
  avatarUrl: String | undefined,
  bio: String,
  banner: String | undefined,
  following: Number,
  followers: Number
}

const props = defineProps({
  userProfile: {
    type: Object as () => userProfileType,
    default: () => ({
      user_id: 'frank515',
      username: 'frank51515',
      avatarUrl: defaultAvatarURL,
      bio: '探索的乐趣',
      banner: defaultBannerURL,
      following: 0,
      followers: 0
    })
  }
});

</script>

<template>
  <div>
    <el-image class="banner" :src="props.userProfile.banner ? props.userProfile.banner : defaultBannerURL"/>
    <el-avatar class="avatar" :size="120" :src="props.userProfile.avatarUrl ? props.userProfile.avatarUrl : defaultAvatarURL"></el-avatar>
    <el-text class="bio"> {{ props.userProfile.bio }} </el-text>
    <el-text class="user-statistics">{{ props.userProfile.following }}正在关注</el-text>
    <el-text class="user-statistics">{{ props.userProfile.followers }}关注者</el-text>
  </div>
</template>

<style scoped>

</style>