import Cookies from 'js-cookie';
import jwt_decode from 'jwt-decode';
import http from '@/util/http-common';


const getDefaultState = () => {
  return {
    accessToken: null,
    loginStatus: false,
    lang: "ko",
    langId: null,
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
      state.langId = payload.langId;
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
    // NOTE: 로그인 상태 설정
    login(context, accessToken) {
      const decoded = jwt_decode(accessToken);
      const userInfo = decoded.userinfo;
      const payload = {
        userId: Number(userInfo.id),
        nickname: userInfo.nickname,
        lang: userInfo.lang,
        langId: userInfo.langId,
        accessToken
      }
      // root.$i18n.locale = userInfo.lang
      context.commit('LOGIN', payload)
    },
    // NOTE: 로그아웃
    logout(context) {
      context.commit('LOGOUT')
    },
    // NOTE: 유저 정보 요청
    requestUserInfo(context, userId) {
      return http.get('/users/' + userId)
    },
    // NOTE: 만난 사람들 요청
    requestUserHistory(context, userId) {
      return http.get()
    },
    // NOTE: 리뷰 작성
    submitReview(context, payload) {
      return http.post(`/review`, payload)
    },
    // NOTE: 작성한 리뷰 요청
    requestGiveReviews(context, payload) {
      return http.get(`/review/from/${payload.userId}/${payload.page}`)
    },
    // NOTE: 받은 리뷰 요청
    requestReceivedReviews(context, payload) {
      return http.get(`/review/to/${payload.userId}/${payload.page}`)
    },
    // NOTE: 만난 사람 추가
    addUserHistorie(context, payload) {
      return http.post('/history/add/', payload)
    },
    // NOTE: 만난 사람들 목록 요청
    requestHistories(context, payload) {
      return http.get(`/history/${payload.userId}`)
    },
    // NOTE: 팔로우 신청
    addFollow(context, payload) {
      return http.post(`/follow/${payload.fromuserid}/${payload.touserid}`)
    },
    //NOTE: 팔로우 해제
    deleteFollow(context, payload) {
      return http.delete(`/follow/${payload.fromuserid}/${payload.touserid}`)
    },
    // NOTE: 팔로우 확인
    checkFollow(context, payload) {
      return http.get(`/follow/checkFollowing/${payload.fromuserid}/${payload.touserid}`)
    },
    // NOTE: 팔로워 목록 가져오기
    requestFollowers(context, payload) {
      return http.get(`/follow/er/${payload.userId}/${payload.page}`)
    },
    // NOTE: 팔로잉 목록 가져오기
    requestFollowings(context, payload) {
      return http.get(`/follow/ing/${payload.userId}/${payload.page}`)
    },
    // NOTE: 리뷰 평점 가져오기
    requestAvgScore(context, payload) {
      return http.get(`review/avg/${payload}`)
    }
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
    getLangId(state) {
      return state.langId;
    },
    getUserId(state) {
      return state.userId;
    },
    getNickName(state) {
      return state.nickname;
    },
    getHeader(state) {
      return { Authorization: `Bearer ${state.accessToken}` }
    },
    getWebSocket(state) {
      return state.socket;
    }
  },
}
export default userStore