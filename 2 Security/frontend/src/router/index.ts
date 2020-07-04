import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "../views/Home.vue";
import About from "../views/About.vue";
import UserLogin from "../views/user/UserLogin.vue";
import UserLogout from "../views/user/UserLogout.vue";
import UserHome from "../views/user/UserHome.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/about",
    name: "About",
    component: About
  },
  {
    path: "/user/login",
    name: "UserLogin",
    component: UserLogin
  },
  {
    path: "/user/logout",
    name: "UserLogout",
    component: UserLogout
  },
  {
    path: "/user/home/:username",
    name: "UserHome",
    component: UserHome,
    props: true
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
