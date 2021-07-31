<template>
  <v-container>
    <!-- 방 생성 -->
    <Create @onCreateRoom="onCreateRoom" :ws="ws" />
    <!-- 방 조건 검색 -->
    <Search @setSearchData="setSearchData"/>
    <!-- 방 목록 -->
    <v-row>
      <v-col v-for="item in rooms" :key="item.id" lg="4" md="3" sm="2" xs="1">
        <Item :item="item" />
      </v-col>
    </v-row>
    <infinite-loading @infinite="requestRooms"></infinite-loading>
  </v-container>
</template>

<script>
import Item from "@/components/Rooms/Item";
import Create from "@/components/Rooms/Create";
import Search from "@/components/Rooms/Search";
import InfiniteLoading from "vue-infinite-loading";
import http from "@/util/http-common";

export default {
  name: "Room",
  data() {
    return {
      socketUrl: process.env.VUE_APP_SOCKET_URL,
      page: 1,
      rooms: [],
      ws: null,
    };
  },
  methods: {
    // NOTE: 방 생성 및 방 이동
    onCreateRoom(uuid) {
      let message = {
        id: "createRoom",
        userId: this.$store.getters["userStore/getUserId"],
        uuid,
      };
      this.sendMessage(message);
      this.$router.push({ name: "Room", params: { UUID: uuid } });
    },
    /*
    NOTE: 방 목록 요청
    NOTE: https://www.npmjs.com/package/vue-infinite-loading
     */
    requestRooms($state) {
      this.$store.dispatch("roomStore/requestRooms", this.page).then((res) => {
        const data = res.data.roomResList;
        console.log(data);

        if (data.length) {
          this.rooms.push(...data);
          this.page += 1;
          $state.loaded();
        } else {
          $state.complete();
        }
      });
    },
    sendMessage(message) {
      if (this.ws.readyState !== this.ws.OPEN) {
        this.$log("[errMessage] Skip, WebSocket session isn't open" + message);
        return;
      }
      const jsonMessage = JSON.stringify(message);
      this.$log("[sendMessage] message: " + jsonMessage);
      this.ws.send(jsonMessage);
    },
    // NOTE: 방 조건 검색 시
    setSearchData (data) {
      //검색을 하고 난뒤
      http.get("/rooms/search", {params:{
        condition:data.category,
        text:data.text
      }}).then((res)=>{
        //console.log(res)
        const data = res.data.roomResList;
        let room =[]
        room.push(...data)
        console.log(room)
        this.rooms=room
      }).catch((err)=>console.log(err))
    }
  },
  created() {
    // console.log(this.$store.getters["userStore/getLocale"]);
    // TODO: 언어 설정 다른 방식이 필요해보임
    this.$root.$i18n.locale = this.$store.getters["userStore/getLocale"];
    this.ws = new WebSocket(this.socketUrl);
  },
  components: {
    Item,
    Create,
    Search,
    InfiniteLoading,
  },
};
</script>

<style>
</style>