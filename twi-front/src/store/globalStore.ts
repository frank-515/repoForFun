import axios from 'axios'
import { defineStore } from 'pinia'

// Generate By CharGPT
function getCookieValueByName(name: String) {
  let cookieArr = document.cookie.split(";"); // 将cookie字符串分割成一个数组
  for (let i = 0; i < cookieArr.length; i++) {
    let cookiePair = cookieArr[i].split("="); // 解析每个cookie的名值对
    if (name == cookiePair[0].trim()) { // 找到指定的cookie
      return decodeURIComponent(cookiePair[1]); // 返回对应的cookie属性值
    }
  }
  return null; // 如果没有找到对应的cookie，则返回null
}

export const useUserStore = defineStore('user', () => {
  const state = {
    isLoggedIn: false,
    user: {
      user_id: '',
      username: '',
      avatarUrl: '',
      bio: '',
      followers: 0,
      following: 0,
      banner: '',
      join_date: ''
    }
  }
  const setLoggedState = (logState: boolean) => {
    state.isLoggedIn = logState
  }
  const setUserId = (user_id: string) => {
    state.user.user_id = user_id
  }

  const setUsername = (username: string) => {
    state.user.username = username
  }

  const setAvatarUrl = (avatarUrl: string) => {
    state.user.avatarUrl = avatarUrl
  }

  const setBio = (bio: string) => {
    state.user.bio = bio
  }

  const setBanner = (bannerURL: string) => {
    state.user.banner = bannerURL
  }

  const fetchInfo = (cb: () => void) => {
    const apiAddress = '/api/u/' + getCookieValueByName('user_id');
    axios
      .get(apiAddress)
      .then((response) => {
        const userData = response.data;
        setLoggedState(true)
        setAvatarUrl(userData.avatar_url);
        setBio(userData.bio);
        setUsername(userData.username);
        setUserId(userData.user_id);
        state.user.followers = userData.followers_count;
        state.user.following = userData.following_count;
        // state.user.banner = userData.banner;
        state.user.join_date = userData.join_date;
        cb();
      })
      .catch((error) => {
        console.error(error);
      });
      
  }

  return {
    // 返回可响应对象
    state,
    
    // 暴露修改状态的方法
    setLoggedState,
    setUserId,
    setUsername,
    setAvatarUrl,
    setBio,
    setBanner,
    fetchInfo
  }
})