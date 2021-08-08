<!-- NOTE: vue-awesome-swiper 활용 -->
<!-- NOTE: https://github.com/surmon-china/vue-awesome-swiper -->
<!-- NOTE: https://swiperjs.com/ -->
<template>
  <swiper v-if="histories" ref="mySwiper" :options="swiperOption">
    <swiper-slide v-for="item in histories" :key="item.id">
      <Item :item="item"/>
    </swiper-slide>
  </swiper>
</template>

<script>
import { Swiper, SwiperSlide } from "vue-awesome-swiper";
import "swiper/css/swiper.css";
import Item from "./HistorySlide/Item.vue";

export default {
  name: "HistorySlide",
  data() {
    return {
      swiperOption: {
        slidesPerView: this.isDesktop ? 3 : 1,
        spaceBetween: this.isDesktop ? 10 : 10,
        centeredSlides: true,
      },
    };
  },
  props: {
    isDesktop: {
      type: Boolean,
    },
    histories: {
      type: Array,
    },
  },
  methods: {},
  components: {
    Swiper,
    SwiperSlide,
    Item,
  },
};
</script>

<style>
</style>