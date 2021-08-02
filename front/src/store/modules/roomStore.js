import Cookies from "js-cookie";
import axios from 'axios';
import http from '@/util/http-common'

const getDefaultState = () => {
  return {
    webSocket: null,
    socketUrl: process.env.VUE_APP_SOCKET_URL,
  }
}

const roomStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
    getWebSocket(state) {
      return state.webSocket
    },
    getSocketUrl(state) {
      return state.socketUrl
    },
  },

  mutations: {
    CONNECT_WEB_SOCKET(state) {
      state.webSocket = new WebSocket(state.socketUrl)
    }
  },

  actions: {
    requestRooms(context, page) {
      return http.get(`/rooms/${page}`)
    },
    requestCreate(context, payload) {
      return http.post('/rooms/create', payload)
    },
    connectWebSocket(context) {
      context.commit("CONNECT_WEB_SOCKET");
      return context.state.webSocket
    },
    requestSearch(context,payload){
      //console.log(payload)
     
      return http.get(`/rooms/search/${payload.page}`,{params:{
        topic:payload.topic,
        lang:payload.lang
      }})
    }
  },
}
export default roomStore