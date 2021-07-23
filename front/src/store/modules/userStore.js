import axios from 'axios';
import Cookies from 'js-cookie';
import jwt_decode from 'jwt-decode';

import http from '@/util/http-common';


const getDefaultState = () => {
  return {
    accessToken: null,
    loginStatus: false,
    lang: 'en',
    userId: null,
    nickname: null,
  }
}

const userStore = {
  namespaced: true,
  state: getDefaultState(),
  mutations: {
    LOGIN(state, payload) {
      state.accessToken = payload.accessToken;
      state.loginStatus = true;
      state.lang = payload.lang;
      state.userId = payload.userId;
      state.nickname = payload.nickname;
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
      const decoded = jwt_decode(accessToken);
      const userInfo = decoded.userinfo;
      const payload = {
        userId: Number(userInfo.id),
        nickname: userInfo.nickname,
        lang: userInfo.lang,
        accessToken
      }
      console.log(decoded);
      context.commit('LOGIN', payload)
    },
    logout(context) {
      context.commit('LOGOUT')
      axios.defaults.headers.common['Authorization'] = undefined
    },
    requestUserInfo(context, userId) {
      return http.get('/users/' + userId)
    },
    requestReceivedReviews(context, userId) {
      return http.get('/review/' + userId)
    },
  },
  getters: {
    getAccessToken(state) {
      return state.accessToken;
    },
    getLoginStatus(state) {
      return state.loginStatus;
    },
    getLocale(state) {
      return state.lang;
    },
    getUserId(state) {
      return state.userId;
    }
  },
}
export default userStore