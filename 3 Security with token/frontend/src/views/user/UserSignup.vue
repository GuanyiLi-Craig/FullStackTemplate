<template>
  <div class="signup">
    <Signup v-bind:signupForm="this.signupForm" />
  </div>
</template>

<script>
// @ is an alias to /src
import Signup from "@/components/user/Signup.vue";

export default {
  name: "UserSignup",
  data() {
    return {
      signupForm: {
        username: "",
        password: ""
      }
    };
  },
  components: {
    Signup
  }
};
</script>
