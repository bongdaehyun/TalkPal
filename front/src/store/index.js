import Vue from "vue";
import Vuex from "vuex";

import userStore from "@/store/modules/userStore.js";
import roomStore from "@/store/modules/roomStore.js";

import createPersistedState from "vuex-persistedstate";
import Cookies from "js-cookie";


Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    namespaced: true,
    userStore,
    roomStore
  },
  
  plugins: [
    createPersistedState({
      // TODO: secure 값 문제인듯 확인해봐야함
      // paths: ["userStore"], 
      storage: {
        getItem: (key) => Cookies.get(key),
        setItem: (key, value) => Cookies.set(key, value, { secure: false }),
        removeItem: (key) => Cookies.remove(key)
      }
    }),
  ],
});
