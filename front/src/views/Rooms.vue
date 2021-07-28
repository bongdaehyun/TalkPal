<template>
  <div>
    <!-- 방 생성 -->
    <Create />
    <!-- 방 목록 -->
    <v-row>
      <v-col v-for="item in rooms" :key="item.id" lg="4" md="3" sm="2" xs="1">
        <Item :item="item" />
      </v-col>
    </v-row>
    <infinite-loading @infinite="infiniteHandler"></infinite-loading>
  </div>
</template>

<script>
import Item from "@/components/Rooms/Item";
import Create from "@/components/Rooms/Create";

import InfiniteLoading from "vue-infinite-loading";
import axios from "axios";

// NOTE: 인피니티 스크롤 테스트 API 주소
let api = "https://api.instantwebtools.net/v1/passenger?size=20&page=";

export default {
  name: "Room",
  data() {
    return {
      page: 1,
      rooms: [],
    };
  },
  methods: {
    // NOTE: 방 목록 요청
    // requestRooms() {
    //   this.$store.dispatch("roomStore/requestRooms", this.page).then((res) => {
    //     console.log(res);
    //     this.list = res.data;
    //   });
    // },

    /*
    NOTE: 무한스크롤 처리 함수
    NOTE: https://www.npmjs.com/package/vue-infinite-loading
     */
    infiniteHandler($state) {
      this.$store.dispatch("roomStore/requestRooms", this.page).then((res) => {
        const data = res.data.roomResList;
        if (data.length) {
          this.rooms.push(...data);
          this.page += 1;
          $state.loaded();
        } else {
          $state.complete();
        }
      });
    },
  },
  created() {
    // console.log(this.$store.getters["userStore/getLocale"]);
    // TODO: 언어 설정 다른 방식이 필요해보임
    this.$root.$i18n.locale = this.$store.getters["userStore/getLocale"];
  },
  components: {
    Item,
    Create,
    InfiniteLoading,
  },
};
</script>

<style>
</style>