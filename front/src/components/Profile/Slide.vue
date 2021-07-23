<!-- NOTE: vue-awesome-swiper 활용 -->
<!-- NOTE: https://github.com/surmon-china/vue-awesome-swiper -->
<!-- NOTE: https://swiperjs.com/ -->
<template>
  <swiper class="swiper" :options="swiperOption">
    <swiper-slide v-for="item in items" :key="item.id">
      <Item :item="item" />
    </swiper-slide>
  </swiper>
</template>

<script>
import { Swiper, SwiperSlide } from "vue-awesome-swiper";
import "swiper/css/swiper.css";
import Item from "./Slide/Item.vue";

export default {
  name: "SlideDesktop",
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
    items: {
      type: Array,
    },
  },
  components: {
    Swiper,
    SwiperSlide,
    Item,
  },
  created() {},
};
</script>

<style>
</style>