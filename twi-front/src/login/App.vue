<script lang="ts" setup>
import { defineComponent, ref } from "vue";
import axios from "axios";
// import { useUserStore } from '../store/globalStore'
const id = ref("");
const password = ref("");
// const userStore = useUserStore();

const login = () => {
  axios
    .post("/api/login", {
      password: password.value,
      user_id: id.value
    })
    .then(function (response) {
      if (response.data == 'success') {
        window.location.href = '/'
      } else {
        alert('登陆失败')
      }
    })
    .catch(function (error) {
      console.log(error);
      alert('登陆失败')
    });
};
const register = () => {
  axios
    .post("/api/register", {
      password: password.value,
      user_id: id.value
    })
    .then(function (response) {
      if (response.data == 'success') {        
        alert('注册成功，请点击登陆');
      } else {
        alert('注册失败')
      }
    })
    .catch(function (error) {
      console.log(error);
      alert('注册失败')
    });
};
</script>

<template>
  <div class="login-container">
    <span style="flex: 0.382"></span>
    <div class="login-form">
      <h2>Sign In 🍍Twine</h2>
      <form>
        <div class="input-wrapper">
          <div class="form-group">
            <input type="text" placeholder="ID" v-model="id" />
          </div>

          <div class="form-group">
            <input type="password" placeholder="Password" v-model="password" />
          </div>

          <div class="buttons-group">
            <button class="button primary" @click.prevent="login">
              Log In
            </button>
            <button class="button secondary" @click.prevent="register">
              Register
            </button>
          </div>
        </div>
      </form>
    </div>
    <span style="flex: 1"></span>
  </div>
</template>


<style>
.input-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.buttons-group {
  display: flex;
}
.buttons-group button {
  margin-inline: 2ex;
}
.login-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100vh;
}

.login-form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 20%;
  padding: 40px;
  backdrop-filter: blur(5px);
  background-color: rgba(188, 188, 188, 0.8);
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

h2 {
  font-size: 2rem;
  margin-bottom: 2rem;
  text-align: center;
}

form {
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-bottom: 2rem;
}

.form-group {
  position: relative;
  margin-bottom: 2rem;
  right: 1.5ex;
}

input {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 2px solid #0467db;
  font-size: 1.2rem;
  background-color: transparent;
}

input:focus {
  outline: none;
}

label {
  position: absolute;
  bottom: -1rem;
  font-size: 1.2rem;
  color: #999;
  transition: all 0.3s ease;
  pointer-events: none;
}

input:focus + label,
input:not(:placeholder-shown) + label {
  transform: translate(0, -50%);
  font-size: 1rem;
  color: #0467db;
  font-weight: bold;
}

button {
  padding: 10px;
  border-radius: 5px;
  font-size: 1.2rem;
  width: 10ex;
  transition: all 0.3s ease;
  margin-bottom: 2rem;
  cursor: pointer;
}

.primary {
  color: #fff;
  background-color: #0467db;
  border: none;
}

.secondary {
  color: #0467db;
  background-color: #fff;
  border: 2px solid #0467db;
}

.button:hover {
  opacity: 0.9;
}

.login-icons {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 50%;
}

.icon-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 2rem;
}

i {
  font-size: 4rem;
  color: #0467db;
  margin-bottom: 1rem;
}

p {
  font-size: 1.5rem;
  color: #0467db;
  font-weight: bold;
  margin: 0;
}
</style>