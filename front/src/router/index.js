import Vue from "vue";
import VueRouter from "vue-router";
import Main from "@/views/Main.vue";
import Rooms from "@/views/Rooms.vue";
import Room from "@/views/Room.vue";
import Profile from "@/views/Profile.vue";
import ERROR404 from '@/views/ERROR404.vue';
import Chat from '@/views/Chat.vue';
import store from '@/store/index';
import i18n from '@/i18n'


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
    path: "/profile/:userId",
    name: "Profile",
    component: Profile,
    meta: { auth: true },
  },
  {
    path: "/chat",
    name: "Chat",
    component: Chat,
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
  i18n.locale = store.getters["userStore/getLocale"];

  store.dispatch("changeActiveMenu", to.name)

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
