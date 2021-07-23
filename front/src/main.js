import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
// NOTE: 다국어 지원 모듈
import i18n from '@/i18n'
// NOTE: 무한스크롤 모듈
import InfiniteLoading from 'vue-infinite-loading';
// NOTE: 유효성 검사 묘듈
import Vuelidate from 'vuelidate'
// NOTE: Swiper 컴포넌트 모듈
import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/css/swiper.css'

Vue.config.productionTip = false;

new Vue({
  i18n,
  router,
  store,
  vuetify,
  InfiniteLoading,
  Vuelidate,
  VueAwesomeSwiper,
  render: (h) => h(App),
}).$mount("#app");



