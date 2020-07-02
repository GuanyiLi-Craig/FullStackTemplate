<template>
  <div id="user-login-form">
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
  </div>
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
    const url = `/api/login?username=`
                + this.loginForm.username
                + `&password=`
                + this.loginForm.password;
    const res = await axios.post(url);
    console.log(res.data);
    this.$router.push({name: 'UserHome', params: {username: this.loginForm.username}});
  }
}
</script>

<style lang="scss">
</style>
