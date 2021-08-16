import http from '@/util/http-common'

const getDefaultState = () => {
    return {

    }
  }
  
  const dmStore = {
    namespaced: true,
    state: getDefaultState(),
  
    getters: {

    },
  
    mutations: {

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
    },
  }

export default dmStore