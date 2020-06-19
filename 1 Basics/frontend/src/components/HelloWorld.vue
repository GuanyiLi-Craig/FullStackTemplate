<template>
  <div class="hello">
    <h1>{{ body.msg }}</h1>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HelloWorld',
  data() {
    return {
      body: {
        msg: "abc"
      }
    }
  },
  async mounted() {
    console.log('Get data from backend');
    this.body.msg = (await axios.get(`http://localhost:8080/api/helloworld`)).data;
  }
}
</script>
