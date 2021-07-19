<template>
  <div>
    <v-row no-gutters>
      <v-col v-for="item in list" :key="item.id" lg="4" md="3" sm="2" xs="1">
        <v-card class="mx-auto" max-width="400">
          <v-img
            class="white--text align-end"
            height="200px"
            src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
          >
            <v-card-title>{{ item.name }}</v-card-title>
          </v-img>
          <v-card-subtitle class="pb-0"> {{ item.name }} </v-card-subtitle>
          <v-card-text class="text--primary">
            <div>{{ item.name }}</div>
            <div>{{ item.name }}</div>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="orange" text> {{ $t("room_enter") }} </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
    <infinite-loading @infinite="infiniteHandler"></infinite-loading>
  </div>
</template>

<script>
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
};
</script>

<style>
</style>