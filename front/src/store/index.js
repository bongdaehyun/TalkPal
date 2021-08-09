import Vue from "vue";
import Vuex from "vuex";

import userStore from "@/store/modules/userStore.js";
import roomStore from "@/store/modules/roomStore.js";

import createPersistedState from "vuex-persistedstate";
import Cookies from "js-cookie";


Vue.use(Vuex);

const getDefaultState = () => {
  return {
    snackbarText: null,
    snackbarStatus: false,
    snackbarColor: "primary",
  }
}

export default new Vuex.Store({
  modules: {
    namespaced: true,
    userStore,
    roomStore
  },
  state: getDefaultState(),
  getters: {
    getSnackbarText(state) {
      return state.snackbarText
    },
    getSnackbarStatus(state) {
      return state.snackbarStatus
    },
    getSnackbarColor(state) {
      return state.snackbarColor
    }
  },
  mutations: {
    ON_SNACKBAR(state, payload) {
      state.snackbarStatus = true
      state.snackbarText = payload.text
      state.snackbarColor = payload.color
    },
    OFF_SNACKBAR(state) {
      Object.assign(state, getDefaultState())
    },
  },
  actions: {
    onSnackbar(context, payload) {
      context.commit("ON_SNACKBAR", payload);
      setTimeout(() => {
        context.commit("OFF_SNACKBAR", payload);
      }, 2000)
    }
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
