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
    const headers = Utils.getHeaders();
    const res = await axios.get(url, headers);
    localStorage.removeItem("Authorization");
    console.log(res);
  }
};
</script>
