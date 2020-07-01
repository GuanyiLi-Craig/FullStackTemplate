<template>
  <div>
    <h3 class="title">User Home</h3>
    <p>user name: {{userInfo.username}}</p>
    <p>update time: {{userInfo.modifiedTime}}</p>
    <p>user group: {{userInfo.role}}</p>
    <button v-on:click="refresh()">Refresh</button>
  </div>
</template>

<script lang="ts">
import { Prop, Vue, Component } from 'vue-property-decorator';
import axios from "axios";
import { UserInfo } from "@/utils/user/interfaces"

@Component
export default class Home extends Vue {
  @Prop()
  private userInfo!: UserInfo;

  public async refresh() {
    console.log(this.userInfo);
    this.userInfo = await axios.get(`/api/user/getUserInfo/` + this.userInfo.username);
    console.log(this.userInfo);
  }
}
</script>

<style lang="scss">
</style>
