<template>
  <form id="user-login-form" @submit.prevent="handleLogin">
    <h3 class="title">Login Form</h3>
    <input
      ref="username"
      v-model="loginForm.username"
      placeholder="Username"
      name="username"
      type="text"
    />
    <input
      ref="password"
      v-model="loginForm.password"
      placeholder="Password"
      name="password"
    />

    <button v-on:click="handleLogin()">Login</button>
  </form>
</template>

<script lang="ts">
import { Prop, Vue, Component } from 'vue-property-decorator';
import axios from "axios";
import { LoginForm } from "@/utils/user/interfaces"

@Component
export default class Login extends Vue {
  @Prop()
  private loginForm!: LoginForm;

  public async handleLogin() {
    console.log(this.loginForm);

    const res = await axios.post(`/api/login`, this.loginForm);
    console.log(res);
  }
}
</script>

<style lang="scss">
</style>
