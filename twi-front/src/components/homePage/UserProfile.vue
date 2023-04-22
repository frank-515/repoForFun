<script setup lang="ts">
import defaultBannerURL from "@/assets/default_banner.jpg";
import defaultAvatarURL from "@/assets/default_avatar.png";

interface userProfileType {
  user_id: String;
  username: String;
  avatarUrl: String | undefined;
  bio: String;
  banner: String | undefined;
  following: Number;
  followers: Number;
}
const fit = "cover"
const props = defineProps({
  userProfile: {
    type: Object as () => userProfileType,
    default: () => ({
      user_id: "",
      username: "",
      avatarUrl: defaultAvatarURL,
      bio: "",
      banner: defaultBannerURL,
      following: 0,
      followers: 0,
    }),
  },
});
</script>

<template>
  <div class="outer-wrapper">
    <el-image
      class="banner"
      style="width: 100%; height: 180px;"
      fit="cover"
      :src="
        props.userProfile.banner ? props.userProfile.banner.toString() : defaultBannerURL
      "
    />
    <div class="user-card">
      <el-row>
        <el-col :span="4">
          <el-avatar
            class="avatar"
            :size="60"
            :src="props.userProfile.avatarUrl ? props.userProfile.avatarUrl.toString() : defaultAvatarURL"
          ></el-avatar>
        </el-col>
        <el-col :span="18" class="username-id">
          <span class="username">{{ props.userProfile.username }}</span>
          <el-text type="primary" style="padding: 1ex"
            >@{{ props.userProfile.user_id }}
          </el-text>
          <el-space></el-space>
        </el-col>
      </el-row>
      <el-row >
        <el-col :span="4">
        </el-col>
        <el-col :span="24">
          <span >
            <p class="bio-wrapper">{{ props.userProfile.bio }}</p>
          </span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <div>
            <p class="user-statistics">
              <strong>{{ props.userProfile.following }}</strong><span /> following
            </p>
          </div>
        </el-col>
        <el-col :span="8">
          <div>
            <p class="user-statistics">
              <strong>{{ props.userProfile.following }}</strong><span /> following
            </p>
          </div>
        </el-col>
        <el-col :span="8">
          <button class="btn-grad">Follow</button>
        </el-col>
      </el-row>
    </div>
    </div>

</template>

<style scoped>
.user-statistics {
  margin: 15px;
  padding: 2ex;
  text-align: left;
  position: relative;
  margin: 15px;
  padding-left: 6ex;
  bottom: 2ex;
  font: bolder;
}
.username-id {
  text-align: left;
  vertical-align: middle;
  display: inline-block;
}
.bio-wrapper {
  text-align: left;
  position: relative;
  margin: 15px;
  padding-left: 6ex;
  bottom: 2ex;
}
.username {
  font-size: large;
  font-weight: bold;
}
.banner {
  position: relative;
  z-index: 1;
}
.avatar {
  position: relative;
  z-index: 2;
  bottom: 4ex;
}
.user-card {
  padding: 2ex;
  position: relative;
  bottom: 10ex;
  z-index: 3;
  background: rgba(16, 16, 16, 0.8);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.18);
  top: -2ex;
}

         
.btn-grad {
  background-image: linear-gradient(to right, #00c6ff 0%, #0072ff  51%, #00c6ff  100%);
  margin: 10px;
  padding: 8px 20px;
  text-align: center;
  text-transform: uppercase;
  transition: 0.5s;
  background-size: 200% auto;
  color: white;            
  box-shadow: 0 0 20px #eee;
  border-radius: 10px;
  display: block;
  position: relative;
  top: 1ex;
}

.btn-grad:hover {
  background-position: right center; /* change the direction of the change here */
  color: #fff;
  text-decoration: none;
}
         
.outer-wrapper {
  width: 50vw;
  min-width: 320px;
}
</style>