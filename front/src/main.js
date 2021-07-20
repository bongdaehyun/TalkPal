import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import i18n from '@/i18n'
import InfiniteLoading from 'vue-infinite-loading';
import Vuelidate from 'vuelidate'
Vue.config.productionTip = false;

new Vue({
  i18n,
  router,
  store,
  vuetify,
  InfiniteLoading,
  Vuelidate,
  render: (h) => h(App),
}).$mount("#app");



