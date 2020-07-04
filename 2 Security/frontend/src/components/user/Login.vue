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
import { Prop, Vue, Component } from "vue-property-decorator";
import axios from "axios";
import { LoginForm } from "@/utils/user/interfaces";
import { Utils } from "@/utils/utils";

@Component
export default class Login extends Vue {
  @Prop()
  private loginForm!: LoginForm;

  public async handleLogin() {
    console.log(this.loginForm);
    const url = Utils.getLoginUrl(
      this.loginForm.username,
      this.loginForm.password
    );
    const res = await axios.post(url);
    console.log(res);
    if (res.status === 200) {
      this.$router.push({
        name: "UserHome",
        params: { username: this.loginForm.username }
      });
    } else {
      this.$router.push({ name: "UserLogin" });
    }
  }
}
</script>

<style lang="scss"></style>
