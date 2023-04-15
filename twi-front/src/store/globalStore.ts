import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  const state = {
    user: {
      user_id: '',
      username: '',
      avatarUrl: '',
      bio: ''
    }
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

  return {
    // 返回可响应对象
    state,
    
    // 暴露修改状态的方法
    setUserId,
    setUsername,
    setAvatarUrl,
    setBio
  }
})