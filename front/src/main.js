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
import Console from "@/util/console.js"
// NOTE: 요소 사이즈 변화 감지 모듈


Vue.config.productionTip = false;


Vue.prototype.$log = Console.log
Vue.prototype.$info = Console.info
Vue.prototype.$warn = Console.warn
Vue.prototype.$error = Console.error

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