import Vue from "vue";
import VueRouter from "vue-router";
import Main from "@/views/Main.vue";
import Rooms from "@/views/Rooms.vue";
import Room from "@/views/Room.vue";
import Profile from "@/views/Profile.vue";
import ERROR404 from '@/views/ERROR404.vue';

import store from '@/store/index';


Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Main",
    component: Main,
    meta: { auth: false },

  },
  {
    path: "/rooms",
    name: "Rooms",
    component: Rooms,
    meta: { auth: true },
  },
  {
    path: "/room/:UUID",
    name: "Room",
    component: Room,
    meta: { auth: true },
  },
  {
    path: "/profie/:userId",
    name: "Profile",
    component: Profile,
    meta: { auth: true },
  },
  {
    path: '*',
    name: 'ERROR404',
    component: ERROR404
  },
];

const router = new VueRouter({
  mode: 'history',
  routes,
});

router.beforeEach((to, from, next) => {
  const loginStatus = store.getters["userStore/getLoginStatus"]
  if (to.meta.auth && !loginStatus) {
    // TODO: Toast 처리
    console.log('로그인이 필요합니다');
    next('/');
  }
  if (!to.meta.auth && loginStatus) {
    // TODO: Toast 처리
    console.log('로그인한 상태입니다.');
    next('/rooms');
  }

  next()

});

export default router;
