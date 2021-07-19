import Vue from "vue";
import Vuex from "vuex";

import userStore from "@/store/modules/userStore.js";
import roomStore from "@/store/modules/roomStore.js";

import createPersistedState from "vuex-persistedstate";
import Cookies from 'js-cookie';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    namespaced: true,
    userStore,
    roomStore,
  },
  plugins: [createPersistedState({
    paths: ["userStore"],
    storage: {
      getItem: key => Cookies.get(key),
      // setItem: (key, value) => Cookies.set(key, value, { expires: 1, secure: true }),
      // expires : 유효기간, 설정안하면 브라우저 종료 시 삭제
      setItem: (key, value) => Cookies.set(key, value, { secure: true }),
      removeItem: key => Cookies.remove(key)
    }
  })],
});
