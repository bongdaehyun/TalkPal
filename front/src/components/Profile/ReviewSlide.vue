<!-- NOTE: vue-awesome-swiper 활용 -->
<!-- NOTE: https://github.com/surmon-china/vue-awesome-swiper -->
<!-- NOTE: https://swiperjs.com/ -->
<template>
  <swiper
    v-if="Item"
    ref="mySwiper"
    :options="swiperOption"
    @reachEnd="slideEnd"
  >
    <swiper-slide v-for="item in Item.list" :key="item.id">
      <Item :item="item" :category="Item.category" />
    </swiper-slide>
  </swiper>
</template>

<script>
import { Swiper, SwiperSlide } from "vue-awesome-swiper";
import "swiper/css/swiper.css";
import Item from "./ReviewSlide/Item.vue";
import isMobile from "@/mixin/isMobile.js";

export default {
  name: "ReviewSlide",
  mixins: [isMobile],
  data() {
    return {
      swiperOption: {
        slidesPerView: this.isMobile ? 1 : 3,
        spaceBetween: this.isMobile ? 10 : 10,
        centeredSlides: true,
      },
    };
  },
  props: {
    Item: {
      type: Object,
    },
  },
  methods: {
    // NOTE: 마지막 리뷰일 때 다음 리뷰 페이지 불러오기
    slideEnd() {
      this.$emit("onSlideEnd", this.Item);
    },
  },
  created() {},
  components: {
    Swiper,
    SwiperSlide,
    Item,
  },
};
</script>

<style>
</style>