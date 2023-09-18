import axios from 'axios'
import { defineStore } from 'pinia'
import { ref } from 'vue'

const userInfoAPI = '/api/user'

export const useGlobalStore = defineStore('global', () => {
  const username = ref('')
  const avatarURL = ref('')
  const level = ref(1)
  const isAdmin = ref(false)
  const isLogged = ref(false)
  const userID = ref(0)
  const address = ref('')
  const contact_number = ref('')

  const fetchUserInfo = (user_id: string) => {
    axios.post(userInfoAPI, { user_id: user_id })
      .then((response) => {
        if (response.data.username) {
          isLogged.value = true
          avatarURL.value = response.data.user_avatar
          isAdmin.value = response.data.is_admin
          level.value = response.data.user_level
          userID.value = response.data.user_id
          contact_number.value = response.data.contact_number
          address.value = response.data.default_address
        }

      })
  }

  const fetchUserInfoByName = (username: string) => {
    
    
    axios.post(userInfoAPI, { username: username })
      .then((response) => {
        if (response.data.username) {
          isLogged.value = true
          avatarURL.value = response.data.user_avatar
          isAdmin.value = response.data.is_admin
          level.value = response.data.user_level
          userID.value = response.data.user_id
          contact_number.value = response.data.contact_number
          address.value = response.data.default_address
        }

      })
  }

  const reset = () => {
    username.value = ''
    avatarURL.value = ''
    level.value = 1
    isAdmin.value = false
    isLogged.value = false
    userID.value = 0
  }

  return { username, avatarURL, level, isAdmin, isLogged, userID, contact_number, address, fetchUserInfo, reset, fetchUserInfoByName }
})