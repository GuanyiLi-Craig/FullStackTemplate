<template>
  <div class="login">
    <Login v-bind:loginForm="this.loginForm"/>
  </div>
</template>

<script>
// @ is an alias to /src
import Login from "@/components/user/Login.vue";

export default {
  name: "UserLogin",
  data() {
      return {
            loginForm: {
                username: '',
                password: '',
            },
      };
  },
  components: {
    Login
  }
};
</script>
