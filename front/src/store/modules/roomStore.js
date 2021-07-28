import Cookies from "js-cookie";
import axios from 'axios';
import http from '@/util/http-common'

const getDefaultState = () => {
  return {
  }
}

const roomStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
  },

  mutations: {

  },

  actions: {
    requestRooms(context, page) {
      return http.get(`/rooms/${page}`)
    },
    requestCreate(context, payload) {
      return http.post('/rooms/create', payload)
    }
  },
}
export default roomStore