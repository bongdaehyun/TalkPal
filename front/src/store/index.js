import Vue from "vue";
import Vuex from "vuex";

import userStore from "@/store/modules/userStore.js";
import roomStore from "@/store/modules/roomStore.js";
import reviewStore from "@/store/modules/reviewStore.js";
import questionStore from "@/store/modules/questionStore.js";
import chatStore from "@/store/modules/chatStore.js";

import createPersistedState from "vuex-persistedstate";
import Cookies from "js-cookie";


Vue.use(Vuex);

const getDefaultState = () => {
  return {
    snackbarText: null,
    snackbarStatus: false,
    snackbarColor: "primary",
    activeMenu: null,
  }
}

export default new Vuex.Store({
  modules: {
    namespaced: true,
    userStore,
    roomStore,
    reviewStore,
    questionStore,
    chatStore,
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
    },
    getActiveMenu(state) {
      return state.activeMenu
    }
  },
  mutations: {
    ON_SNACKBAR(state, payload) {
      state.snackbarStatus = true
      state.snackbarText = payload.text
      state.snackbarColor = payload.color
    },
    OFF_SNACKBAR(state) {
      state.snackbarText = null
      state.snackbarStatus = false
      state.snackbarColor = "primary"
    },
    CHANGE_ACTIVE_MENU(state, payload) {
      state.activeMenu = payload
    },
  },
  actions: {
    onSnackbar(context, payload) {
      context.commit("ON_SNACKBAR", payload);
      setTimeout(() => {
        context.commit("OFF_SNACKBAR", payload);
      }, 2000)
    },
    changeActiveMenu(context, payload) {
      context.commit("CHANGE_ACTIVE_MENU", payload);
    }
  },

  plugins: [
    createPersistedState({
      // paths: ["userStore", "roomStore"],
      storage: {
        getItem: (key) => Cookies.get(key),
        setItem: (key, value) => Cookies.set(key, value, { secure: false }),
        removeItem: (key) => Cookies.remove(key)
      }
    }),
  ],
});
