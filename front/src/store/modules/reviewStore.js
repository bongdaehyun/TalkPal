import http from '@/util/http-common'

const getDefaultState = () => {
  return {
    dialog: false,
    toUserId: null,
  }
}

const reviewStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
    getDialog(state) {
      return state.dialog
    },
    getToUserId(state) {
      return state.toUserId
    }
  },

  mutations: {
    OPEN_DIALOG(state) {
      state.dialog = true
    },
    SET_TO_USER_ID(state, payload) {
      state.toUserId = payload.toUserId
    },
    CLOSE_DIALOG(state) {
      state.dialog = false
      state.toUserId = null
    },
  },

  actions: {
    // NOTE: 작성한 리뷰 요청
    requestGiveReviews(context, payload) {
      return http.get(`/review/from/${payload.userId}/${payload.page}`)
    },
    // NOTE: 받은 리뷰 요청
    requestReceivedReviews(context, payload) {
      return http.get(`/review/to/${payload.userId}/${payload.page}`)
    },
    createReview(context, payload) {
      return http.post(`/review`, payload)
    },
    openDialog(context) {
      context.commit("OPEN_DIALOG");
    },
    closeDialog(context) {
      context.commit("CLOSE_DIALOG");
    },
    setToUserId(context, payload) {
      context.commit("SET_TO_USER_ID", payload);
    },
  },
}
export default reviewStore