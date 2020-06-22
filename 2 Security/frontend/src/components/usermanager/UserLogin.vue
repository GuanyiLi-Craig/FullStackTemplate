<template>
  <div>
    <input type="text" v-model="loginForm.username" placeholder="username"/>
    <input type="text" v-model="loginForm.password" placeholder="password"/>
    <button @click="login">Login</button>
  </div>
</template>


<script>
import { mapMutations } from 'vuex';
export default {
  data () {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      userToken: ''
    };
  },
 
  methods: {
    ...mapMutations(['changeLogin']),
    login () {
      let _this = this;
      if (this.loginForm.username === '' || this.loginForm.password === '') {
        alert('User name or password cannot be empty');
      } else {
        this.axios({
          method: 'post',
          url: 'http://localhost:8080/api/user/login',
          data: _this.loginForm
        }).then(res => {
          console.log(res.data);
          _this.userToken = 'Bearer ' + res.data.data.body.token;
          // store token to the local data
          _this.changeLogin({ Authorization: _this.userToken });
          _this.$router.push('/home');
          alert('Login Success');
        }).catch(error => {
          alert('Bad credentials');
          console.log(error);
        });
      }
    }
  }
};
</script>
