<template>
  <div class="logout">
    <h3>Logout</h3>
  </div>
</template>

<script>
import { Utils } from "@/utils/utils";
import axios from "axios";

export default {
  name: "UserLogout",
  async mounted() {
    const url = Utils.getLogoutUrl();
    const res = await axios.get(url);
    console.log(res);
  }
};
</script>
