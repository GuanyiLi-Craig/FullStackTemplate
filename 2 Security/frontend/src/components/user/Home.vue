<template>
  <div>
    <h3 class="title">User Home</h3>
    <p>user name: {{ userInfo.username }}</p>
    <p>update time: {{ userInfo.modifiedTime }}</p>
    <p>user group: {{ userInfo.role }}</p>
    <button v-on:click="refresh()">Refresh</button>
  </div>
</template>

<script lang="ts">
import { Prop, Vue, Component } from "vue-property-decorator";
import axios from "axios";
import { UserInfo } from "@/utils/user/interfaces";
import { Utils } from "@/utils/utils";

@Component
export default class Home extends Vue {
  @Prop()
  private userInfo!: UserInfo;

  public async refresh() {
    console.log(this.userInfo);
    const url = Utils.getUrl("user", "getUserInfo") + this.userInfo.username;
    const headers = Utils.getHeader(this.userInfo.username);
    console.log(headers);
    axios.defaults.withCredentials=true;
    this.userInfo = await axios.get(url);
    console.log(this.userInfo);
  }
}
</script>

<style lang="scss"></style>
