import Vue from "vue";
import VueRouter from "vue-router";
import Main from "@/views/Main.vue";
import Room from "@/views/Room.vue";


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
];

const router = new VueRouter({
  routes,
});

export default router;
