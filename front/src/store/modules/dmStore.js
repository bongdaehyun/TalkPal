import http from '@/util/http-common'

const getDefaultState = () => {
  return {
    chatRooms: [],
  }
}
const dmStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
    getChatRooms(state) {
      return state.chatRooms;
    }
  },

  mutations: {
    SET_CHAT_ROOMS(state, payload) {
      state.chatRooms = payload;
    },
    // ADD_CHAT_ROOM(state, payload) {
    //   state.chatRooms.push(payload);
    // },
  },

  actions: {
    requestChatRoomList(context, payload) {
      return http.get(`/directMessage/getChatRoomList/${payload.userId}`);
    },
    requestChatMessageList(context, payload) {
      return http.get(`/directMessage/getChatMessageList/${payload.chatRoomId}`);
    },
    sendDirectMessage(context, payload) {
      return http.post(`/directMessage/sendDirectMessage`, payload);
    },
    requestOpponentId(context, payload) {
      return http.get(`/directMessage/getOpponentId/${payload.userId}/${payload.chatRoomId}`);
    },
    addChatUser(context, payload) {
      
    },
    createChatRoom(context, payload) {
      return http.post(`/directMessage/createChatRoom`, payload);
    },
  },
}

export default dmStore