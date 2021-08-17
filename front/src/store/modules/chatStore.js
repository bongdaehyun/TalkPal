import http from '@/util/http-common'

const getDefaultState = () => {
  return {
    chatRooms: [],
    selectChatRoomId: null,
    opponentId: null,
    dialog: false,
    msgList: [],
  }
}
const chatStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
    getDialog(state) {
      return state.dialog
    },
    getChatRooms(state) {
      return state.chatRooms;
    },
    getSelectChatRoomId(state) {
      return state.selectChatRoomId
    },
    getOpponentId(state) {
      return state.opponentId
    },
    getMsgList(state) {
      return state.msgList
    }
  },

  mutations: {
    SET_CHAT_ROOMS(state, payload) {
      state.chatRooms = payload.chatRooms;
    },
    OPEN_DIALOG(state) {
      state.dialog = true
    },
    CLOSE_DIALOG(state) {
      state.dialog = false
    },
    SET_SELECT_CHAT_ROOM_ID(state, payload) {
      state.selectChatRoomId = payload.selectChatRoomId
    },
    SET_OPPONENT_ID(state, payload) {
      state.opponentId = payload.opponentId
    },
    PUSH_MSG_LIST(state, payload) {
      state.msgList.push(payload.chatMessage)
    },
    CLEAR_MSG_LIST(state) {
      state.msgList = []
    },
    CLEAR_STATE(state) {
      Object.assign(state, getDefaultState());
    }
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
    setChatRooms(context, payload) {
      context.commit("SET_CHAT_ROOMS", payload);
    },
    createChatRoom(context, payload) {
      return http.post(`/directMessage/createChatRoom`, payload);
    },
    openDialog(context) {
      context.commit("OPEN_DIALOG")
    },
    closeDialog(context) {
      context.commit("CLOSE_DIALOG")
    },
    setSelectChatRoomId(context, payload) {
      context.commit("SET_SELECT_CHAT_ROOM_ID", payload)
    },
    setOpponentId(context, payload) {
      context.commit("SET_OPPONENT_ID", payload)
    },
    clearMsgList(context) {
      context.commit("CLEAR_MSG_LIST")
    },
    pushMsgList(context, payload) {
      context.commit("PUSH_MSG_LIST", payload)
    },
    clearState(context) {
      context.commit("CLEAR_STATE")
    }
  },
}

export default chatStore