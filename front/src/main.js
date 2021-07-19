import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import i18n from '@/i18n'
import InfiniteLoading from 'vue-infinite-loading';
import Cookies from "js-cookie";

// const savedToken = Cookies.get("accessToken")
// if(savedToken)
// {
//   store.dispatch('getDataByToken',savedToken)
// }


Vue.config.productionTip = false;

new Vue({
  i18n,
  router,
  store,
  vuetify,
  InfiniteLoading,
  render: (h) => h(App),
}).$mount("#app");



