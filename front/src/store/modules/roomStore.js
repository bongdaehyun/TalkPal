import http from '@/util/http-common'

const getDefaultState = () => {
  return {
    isRoom: null,
    reviewDialog: false,
    reviewUserId: null,
  }
}

const roomStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
    getIsRoom(state) {
      return state.isRoom
    },
    getIsReview(state) {
      return {
        reviewDialog: state.reviewDialog,
        reviewUserId: state.reviewUserId,
      }
    },
  },

  mutations: {
    ENTER_ROOM(state) {
      state.isRoom = true
    },
    EXIT_ROOM(state) {
      state.isRoom = false
    },
    REVIEW_FALSE(state) {
      state.reviewUserId = null;
      state.reviewDialog = false;
    },
    REVIEW_TRUE(state, reviewUserId) {
      state.reviewUserId = reviewUserId;
      state.reviewDialog = true;
    },
  },

  actions: {
    requestRooms(context, payload) {
      return http.get(`/rooms/${payload.page}`, {
        params: {
          topic: payload.topic,
          lang: payload.lang,
        }
      })
    },
    requestCreate(context, payload) {
      return http.post('/rooms/create', payload)
    },
    requestDelete(context, payload) {
      return http({
        method: "DELETE",
        url: `/rooms/${payload.roomId}`,
        headers: payload.header,
      })
    },
    setReviewTrue(context, reviewUserId) {
      context.commit("REVIEW_TRUE", reviewUserId);
    },
    setReviewFalse(context) {
      context.commit("REVIEW_FALSE");
    },
    enterRoom(context) {
      context.commit("ENTER_ROOM");
    },
    exitRoom(context) {
      context.commit("EXIT_ROOM");
    },
    reqeustRoomInfo(context, payload) {
      return http.get(`/rooms/get/${payload.uuid}`)
    },
    connectWebSocket(context) {
      context.commit("CONNECT_WEB_SOCKET");
      return context.state.webSocket
    },
    requestCheckJoin(context, uuid) {
      return http.get(`/rooms/check/${uuid}`);
    },
    requestAddPerson(context, payload) {
      return http({
        method: "PUT",
        url: `/rooms/cal/${payload.uuid}?num=${payload.num}`
      })
    }
  },
}
export default roomStore