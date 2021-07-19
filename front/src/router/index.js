import Vue from "vue";
import VueRouter from "vue-router";
import Main from "@/views/Main.vue";
import Room from "@/views/Room.vue";
import Profile from "@/views/Profile.vue";



Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Main",
    component: Main,
  },
  {
    path: "/room",
    name: "Room",
    component: Room,
  },
  {
    path: "/profie",
    name: "Profile",
    component: Profile,
  },
];

const router = new VueRouter({
  routes,
});

export default router;
