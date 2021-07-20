<template>
  <div>
    <!-- 방 생성 -->
    <Create />
    <!-- 방 목록 -->
    <v-row>
      <v-col v-for="item in list" :key="item.id" lg="4" md="3" sm="2" xs="1">
        <Item :item="item" />
      </v-col>
    </v-row>
    <infinite-loading @infinite="infiniteHandler"></infinite-loading>
  </div>
</template>

<script>
import Item from "@/components/Room/Item";
import Create from "@/components/Room/Create";

import InfiniteLoading from "vue-infinite-loading";
import axios from "axios";

// 인피니티 스크롤 테스트 API 주소
let api = "https://api.instantwebtools.net/v1/passenger?size=20&page=";

export default {
  name: "Room",
  data() {
    return {
      page: 0,
      list: [],
    };
  },
  components: {
    Item,
    Create,
    InfiniteLoading,
  },
  computed: {
    listLength() {
      return this.list.length;
    },
  },
  methods: {
    infiniteHandler($state) {
      axios.get(api + this.page).then((res) => {
        const data = res.data.data;

        if (data.length) {
          this.page += 1;
          this.list.push(...data);
          $state.loaded();
        } else {
          $state.complete();
        }
      });
    },
  },
  created() {
    // console.log(this.$store.getters["userStore/getLocale"]);
    this.$root.$i18n.locale = this.$store.getters["userStore/getLocale"];
  },
};
</script>

<style>
</style>