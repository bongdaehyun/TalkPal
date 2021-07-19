import Vue from "vue";
import Vuex from "vuex";
import Cookies from "js-cookie";
import axios from 'axios';
import http from '@/util/http-common'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    accessToken: null,
  },
  getters:{
    getAccessToken(state){
      return state.accessToken;
    }
  },
  mutations: {
    LOGIN(state, payload) {
      Cookies.set("accessToken", payload);
      state.accessToken = payload;
      
    },
    LOGOUT(state) {
      Cookies.remove("accessToken");
      state.accessToken = null;
    },
    SET_ACCESS_TOKEN(state){
      state.accessToken=Cookies.get("accessToken");
    },

  },
  actions: {
    login(context,payload){
      
      let body = payload
      http.post( '/auth/login', body).then((res)=>{
        
        axios.defaults.headers.common['Authorization']="Bearer "+res.data.accessToken
        context.commit("LOGIN",res.data.accessToken)
        
      })
    },
    logout(context){
      context.commit("LOGOUT")
      axios.defaults.headers.common['Authorization']=undefined
    },
    getDataByToken(context,payload){
      axios.defaults.headers.common['Authorization']="Bearer "+payload
      http.get("/users/me").then((response)=>
      {
        context.commit("SET_ACCESS_TOKEN")
      })
    },
    requestSingup(state,payload){
      let body=payload
      console.log(body)
      return http.post("/users",body)
    }
  },
  modules: {},
});
