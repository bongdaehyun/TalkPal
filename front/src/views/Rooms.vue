<template>
  <v-container>
    <!-- 방 생성 -->
    <Create @onCreateRoom="onCreateRoom" :ws="ws" />
    <!-- 방 조건 검색 -->
    <Search @setSearchData="setSearchData" />
    <!-- 방 목록 -->
    <v-row>
      <v-col v-for="item in rooms" :key="item.id" lg="4" md="3" sm="2" xs="1">
        <Item :item="item" @onEnterRoom="onEnterRoom" />
      </v-col>
    </v-row>
    <!-- 방조건 검색시 이벤트 처리 나누기 -->
    <span v-if="!flag">
      <infinite-loading @infinite="requestRooms"></infinite-loading
    ></span>
    <span v-else>
      <infinite-loading @infinite="requestSearchRooms"></infinite-loading
    ></span>
    <v-fab-transition>
      <v-btn
        bottom
        right
        fixed
        fab
        dark
        small
        v-show="btnShow"
        @click.prevent="$vuetify.goTo('.header')"
      >
        <v-icon>mdi-chevron-double-up</v-icon>
      </v-btn>
    </v-fab-transition>
  </v-container>
</template>

<script>
import Item from "@/components/Rooms/Item";
import Create from "@/components/Rooms/Create";
import Search from "@/components/Rooms/Search";
import InfiniteLoading from "vue-infinite-loading";

export default {
  name: "Room",
  data() {
    return {
      socketUrl: process.env.VUE_APP_SOCKET_URL,
      page: 1,
      rooms: [],
      ws: null,
      flag: false,
      search: {
        topic: "",
        lang: "",
        page: 1,
      },
      btnShow: false,
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
    // NOTE: 방 조건 검색 시 받아오는 데이터
    setSearchData(data) {
      console.log("방조건 검색", data);
      //search 데이터 초기화
      (this.search.topic = data.topic), (this.search.lang = data.lang);
      this.search.page = 1;

      this.$store
        .dispatch("roomStore/requestSearch", this.search)
        .then((res) => {
          //현재 rooms초기화
          this.rooms.length = 0;
          const data = res.data.roomResList;
          this.rooms.push(...data);
          this.search.page += 1;
          //조건 검색
          this.flag = true;
        });
    },
    requestSearchRooms($state) {
      this.$store
        .dispatch("roomStore/requestSearch", this.search)
        .then((res) => {
          const data = res.data.roomResList;
          if (data.length) {
            this.search.page += 1;
            this.rooms.push(...data);
            $state.loaded();
          } else {
            $state.complete();
          }
        });
    },
    handleScroll() {
      this.btnShow = window.scrollY > 400;
    },
    connect() {
      this.ws = new WebSocket(this.socketUrl);
      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        this.$info(`[parsedMessage] : ${parsedMessage}`);
        switch (parsedMessage.id) {
          case "joinAnswer":
            // NOTE: 방 입장 요청 수락/거절 여부
            this.onJoinAnswer(parsedMessage);
            break;
          default:
            this.$error("Unrecognized message", parsedMessage);
        }
      };
    },
    onEnterRoom(item) {
      // TODO: 입장 요청 하겠냐는 메세지 추가하기
      let message = {
        id: "joinRequest",
        uuid: item.uuid,
        requestUserId: this.$store.getters["userStore/getUserId"],
        hostId: item.hostId,
      };
      console.log(message);
      this.sendMessage(message);
    },
    onJoinAnswer(msg) {
      this.$log("getJoinAnswer");

      if (!msg.answer) {
        // NOTE: 입장 거절 시 거절되었다는 안내문 메세지만 출력
        alert("denied request");
      } else {
        // NOTE: 입장 수락 시 방 입장 요청 및 화면 이동
        // let message = {
        //   id: "joinRoom",
        //   userId: this.$store.getters["userStore/getUserId"],
        //   uuid: msg.uuid,
        // };
        // this.sendMessage(message);
        this.$router.push({ name: "Room", params: { UUID: msg.uuid } });
      }
    },
  },
  created() {
    // console.log(this.$store.getters["userStore/getLocale"]);
    // TODO: 언어 설정 다른 방식이 필요해보임
    this.$root.$i18n.locale = this.$store.getters["userStore/getLocale"];
    this.connect();
  },
  beforeMount() {
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
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