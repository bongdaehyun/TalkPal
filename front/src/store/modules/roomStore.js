import http from '@/util/http-common'

const getDefaultState = () => {
  return {
    isRoom: null,
    isHostGuide: true,
    isGuestGuide: false,
    hostLang: null,
    guestLang: null
  }
}

const roomStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
    getIsRoom(state) {
      return state.isRoom
    },
    getIsHostGuide(state) {
      return state.isHostGuide
    },
    getIsGuestGuide(state) {
      return state.isGuestGuide
    },
    getHostLang(state) {
      return state.hostLang
    },
    getGuestLang(state) {
      return state.guestLang
    }
  },

  mutations: {
    ENTER_ROOM(state) {
      state.isRoom = true
    },
    EXIT_ROOM(state) {
      state.isRoom = false
    },
    SET_HOST_GUEST_LANG(state, payload) {
      state.hostLang = payload.hostLang
      state.guestLang = payload.guestLang
    },
    CHANGE_GUIDE_LOCALE(state) {
      state.isHostGuide = !state.isHostGuide;
      state.isGuestGuide = !state.isGuestGuide;
    },
    RESET_GUIDE_LOCALE(state) {
      state.isHostGuide = true;
      state.isGuestGuide = false;
    }
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
    },
    // 가이드 언어 전환
    changeGuideLocale(context) {
      context.commit("CHANGE_GUIDE_LOCALE")
    },
    // 가이드 언어 초기화
    resetGuideLocale(context) {
      context.commit("RESET_GUIDE_LOCALE")
    },
    // 방 입장 시 호스트&게스트 언어 설정
    setHostGuestLang(context, payload) {
      context.commit("SET_HOST_GUEST_LANG", payload)
    },
  },
}
export default roomStore