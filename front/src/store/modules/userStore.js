import axios from 'axios';
import Cookies from 'js-cookie';
import jwt_decode from 'jwt-decode';

import http from '@/util/http-common';


const getDefaultState = () => {
  return {
    accessToken: null,
    loginStatus: false,
    locale: 'en',
  }
}

const userStore = {
  namespaced: true,
  state: getDefaultState(),
  mutations: {
    LOGIN(state, payload) {
      state.accessToken = payload;
      state.loginStatus = true;
      state.locale = 'ko';
    },
    LOGOUT(state) {
      Object.assign(state, getDefaultState());
    },
    SET_ACCESS_TOKEN(state) {
      state.accessToken = Cookies.get('accessToken');
    },
  },
  actions: {
    requestRegister(context, payload) {
      let body = payload
      return http.post('/users', body)
    },
    requestLogin(context, payload) {
      let body = payload
      return http.post('/auth/login', body)
    },
    login(context, accessToken) {
      // const decoded = jwt_decode(accessToken);
      // console.log(decoded);
      context.commit('LOGIN', accessToken)
    },
    logout(context) {
      context.commit('LOGOUT')
      axios.defaults.headers.common['Authorization'] = undefined
    },
    getDataByToken(context, accessToken) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
      http.get('/users/me').then((response) => {
        context.commit('SET_ACCESS_TOKEN')
      })
    },
  },
  getters: {
    getAccessToken(state) {
      return state.accessToken;
    },
    getLoginStautus(state) {
      return state.loginStatus;
    },
    getLocale(state) {
      return state.locale;
    }
  },
}
export default userStore