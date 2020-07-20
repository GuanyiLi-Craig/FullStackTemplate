<template>
  <div id="user-signup-form">
    <h3 class="title">Sign Up Form</h3>
    <input
      ref="username"
      v-model="signupForm.username"
      placeholder="Username"
      name="username"
      type="text"
    />
    <input
      ref="password"
      v-model="signupForm.password"
      placeholder="Password"
      name="password"
    />

    <button v-on:click="handleSignup()">Sign Up</button>
  </div>
</template>

<script lang="ts">
import { Prop, Vue, Component } from "vue-property-decorator";
import axios from "axios";
import { SignupForm } from "@/utils/user/interfaces";
import { Utils } from "@/utils/utils";

@Component
export default class Signpu extends Vue {
  @Prop()
  private signupForm!: SignupForm;

  public async handleSignup() {
    console.log(this.signupForm);
    const url = Utils.getSignupUrl();
    const data = {
      username: this.signupForm.username,
      password: this.signupForm.password
    };
    const res = await axios.post(url, data);
    console.log(res);
    if (res.status === 200) {
      this.$router.push({ name: "UserLogin" });
    } else {
      this.$router.push({ name: "UserSignup" });
    }
  }
}
</script>

<style lang="scss"></style>
