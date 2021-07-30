<!-- NOTE: vue-awesome-swiper 활용 -->
<!-- NOTE: https://github.com/surmon-china/vue-awesome-swiper -->
<!-- NOTE: https://swiperjs.com/ -->
<template>
  <swiper
    v-if="reviews"
    ref="mySwiper"
    :options="swiperOption"
    @reachEnd="slideEnd"
  >
    <swiper-slide v-for="item in reviews.items" :key="item.id">
      <Item :item="item" :category="reviews.category" />
    </swiper-slide>
  </swiper>
</template>

<script>
import { Swiper, SwiperSlide } from "vue-awesome-swiper";
import "swiper/css/swiper.css";
import Item from "./ReviewSlide/Item.vue";

export default {
  name: "ReviewSlide",
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
    reviews: {
      type: Object,
    },
  },
  methods: {
    // NOTE: 마지막 리뷰일 때 다음 리뷰 페이지 불러오기
    slideEnd() {
      this.$emit("onSlideEnd", this.reviews);
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