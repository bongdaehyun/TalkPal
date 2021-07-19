import Cookies from "js-cookie";
import axios from 'axios';
import http from '@/util/http-common'

const getDefaultState = () => {
  return {
    accessToken: null,
    loginStatus: false,
  }
}

const userStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
    getAccessToken(state) {
      return state.accessToken;
    },
    getLoginStautus(state) {
      return state.loginStatus;
    }
  },

  mutations: {
    LOGIN(state, payload) {
      state.accessToken = payload;
      state.loginStatus = true;
    },
    LOGOUT(state) {
      Object.assign(state, getDefaultState());
    },
    SET_ACCESS_TOKEN(state) {
      state.accessToken = Cookies.get("accessToken");
    },
  },

  actions: {
    requestRegister(context, payload) {
      let body = payload
      return http.post("/users", body)
    },
    requestLogin(context, payload) {
      let body = payload
      return http.post('/auth/login', body)
    },
    login(context, payload) {
      let accessToken = payload;
      context.commit("LOGIN", accessToken)
    },
    logout(context) {
      context.commit("LOGOUT")
      axios.defaults.headers.common['Authorization'] = undefined
    },
    getDataByToken(context, payload) {
      let accessToken = payload;
      axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
      http.get("/users/me").then((response) => {
        context.commit("SET_ACCESS_TOKEN")
      })
    },

  },
}
export default userStore