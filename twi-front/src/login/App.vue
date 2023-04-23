<template>
  <div class="login-container">
    <div class="login-form">
      <h2>Sign In</h2>
      <form>
        <div class="form-group">
          <input type="text" placeholder="Username" v-model="username" />
          <label></label>
        </div>
        <div class="form-group">
          <input type="password" placeholder="Password" v-model="password" />
          <label></label>
        </div>
        <button class="button primary" @click.prevent="login">Log In</button>
        <button class="button secondary" @click.prevent="register">Register</button>
      </form>
    </div>
    <div class="login-icons">
      <div class="icon-container">
        <i class="fas fa-user"></i>
        <p>Log In</p>
      </div>
      <div class="icon-container">
        <i class="fas fa-user-plus"></i>
        <p>Register</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "Login",
  data() {
    return {
      username: "",
      password: ""
    };
  },
  methods: {
    login() {
      // Handle login logic
    },
    register() {
      // Handle register logic
    }
  }
});
</script>

<style>
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
    width: 50%;
    padding: 40px;
    background-color: #f2f2f2;
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
    left: 0;
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
    width: 100%;
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