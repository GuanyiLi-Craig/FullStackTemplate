<template>
  <div class="userhome">
    <Home v-bind:userInfo="this.userInfo" />
  </div>
</template>

<script>
// @ is an alias to /src
import Home from "@/components/user/Home.vue";
import axios from "axios";

export default {
  name: "UserHome",
  data() {
    return {
      userInfo: {
        username: "",
        modifiedTime: "",
        role: ""
      }
    };
  },
  async mounted() {
    const username = this.$route.params.username;
    const url = `/api/user/getUserInfo/` + username;
    const res = await axios.get(url, {
      withCredentials: true
    });
    this.userInfo = {
      username: username,
      modifiedTime: res.data.modifiedTime,
      role: res.data.role
    };
  },
  components: {
    Home
  }
};
</script>
