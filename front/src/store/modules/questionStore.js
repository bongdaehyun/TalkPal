const getDefaultState = () => {
  return {
    dialog: false,
    answer: null,
    joinRequestUser: null,
    timer: null,
  }
}

const questionStore = {
  namespaced: true,
  state: getDefaultState(),

  getters: {
    getDialog(state) {
      return state.dialog
    },
    getAnswer(state) {
      return state.answer
    },
    getJoinRequestUser(state) {
      return state.joinRequestUser
    },
    getTimer(state) {
      return state.timer
    }
  },

  mutations: {
    OPEN_DIALOG(state, payload) {
      state.dialog = true
      state.answer = null
      state.joinRequestUser = payload.joinRequestUser
    },
    CLOSE_DIALOG(state) {
      state.dialog = false
      state.answer = null
      state.joinRequestUser = null
      state.timer = null
    },
    SET_ANSWER(state, payload) {
      state.answer = payload.answer
    },
    SET_TIMER(state, payload) {
      state.timer = payload.value
    },
    MINUS_TIMER(state, payload) {
      state.timer = state.timer - payload.value
    }
  },

  actions: {
    openDialog(context, payload) {
      context.commit("OPEN_DIALOG", payload);
    },
    closeDialog(context) {
      context.commit("CLOSE_DIALOG");
    },
    setAnswer(context, payload) {
      context.commit("SET_ANSWER", payload)
    },
    setTimer(context, payload) {
      context.commit("SET_TIMER", payload)
    },
    minusTimer(context, payload) {
      context.commit("MINUS_TIMER", payload)
    }
  },
}
export default questionStore