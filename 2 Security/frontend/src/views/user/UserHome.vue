<template>
  <div class="userhome">
    <Home v-bind:userInfo="this.userInfo" />
  </div>
</template>

<script>
// @ is an alias to /src
import Home from "@/components/user/Home.vue";

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
  components: {
    Home
  }
};
</script>
