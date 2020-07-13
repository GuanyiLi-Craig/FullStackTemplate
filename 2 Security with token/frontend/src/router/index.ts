import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "../views/Home.vue";
import About from "../views/About.vue";
import UserSignup from "../views/user/UserSignup.vue";
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
    path: "/user/signup",
    name: "UserSignup",
    component: UserSignup
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

router.beforeEach((to, from, next) => {
  if (to.path === "/user/login") {
    next();
  } else {
    const token = localStorage.getItem("Authorization");

    if (token === null || token === "") {
      next("/user/login");
    } else {
      next();
    }
  }
});

export default router;
