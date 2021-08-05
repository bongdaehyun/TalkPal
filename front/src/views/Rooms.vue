<template>
  <v-container>
    <!-- 방 생성 -->
    <Create @onCreateRoom="onCreateRoom" :ws="ws" />
    <!-- 방 조건 검색 -->
    <Search @setSearchData="setSearchData" />
    <!-- 방 목록 -->
    <v-row v-if="rooms.length > 1">
      <v-col v-for="item in rooms" :key="item.id" lg="4" md="3" sm="2" xs="1">
        <Item :item="item" @onEnterRoom="onEnterRoom" />
      </v-col>
    </v-row>
    <v-row v-else>
      <v-col>
        <v-row justify="center">
          <v-icon x-large color="secondary">mdi-close-box</v-icon>
        </v-row>
        <v-row justify="center">
          <h1>{{ $t("alert_nolist_01") }}</h1>
        </v-row>
        <v-row justify="center">
          <h3>{{ $t("alert_nolist_02") }}</h3>
        </v-row>
      </v-col>
    </v-row>
    <!-- 방조건 검색시 이벤트 처리 나누기 -->

    <span v-if="!flag">
      <infinite-loading @infinite="requestRooms">
         </infinite-loading>
    </span>
    <span v-else>
      <infinite-loading @infinite="requestSearchRooms">
         </infinite-loading>
    </span>
    <v-fab-transition>
      <v-btn
        bottom
        right
        fixed
        fab
        color="secondary"
        small
        v-show="btnShow"
        @click.prevent="$vuetify.goTo('.header')"
      >
        <v-icon>mdi-chevron-double-up</v-icon>
      </v-btn>
    </v-fab-transition>
    <v-overlay :value="loadingAnswer">
      <v-progress-circular indeterminate size="128">
        대기중...
      </v-progress-circular>
    </v-overlay>
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
      loadingAnswer: false,
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
        this.$log(data);
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
      this.$log("방조건 검색", data);
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
          this.$log(data);
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
      let message = {
        id: "joinRequest",
        uuid: item.uuid,
        requestUserId: this.$store.getters["userStore/getUserId"],
        hostId: item.hostId,
      };
      this.$log(message);

      // NOTE: 입장 요청 대기 시작
      this.loadingAnswer = true;

      // NOTE: 웹소켓 메세지 전송
      this.sendMessage(message);
    },
    onJoinAnswer(msg) {
      this.$log("getJoinAnswer");
      this.$log(msg);

      // NOTE: 입장 응답 대기 종료
      this.loadingAnswer = false;
      const answer = msg.answer;
      if (answer === "false") {
        // NOTE: 입장 거절 시 거절되었다는 안내문 메세지만 출력
        console.log("입장 요청 거부");
        this.$store.dispatch("onSnackbar", {
          text: "입장 요청이 거부 됐습니다.",
          color: "red darken-1",
        });
      } else if (answer === "true") {
        // NOTE: 입장 수락 시 방 인원 초과 여부 확인 후 입장
        this.$store
          .dispatch("roomStore/requestCheckJoin", msg.uuid)
          .then((res) => {
            this.$log(res);
            const flag = res.data;
            // NOTE: join ok인거 굳이 체크 안해도 됨
            if (flag === "join ok") {
              // NOTE: 입장 가능
              this.$store.dispatch("onSnackbar", {
                text: "입장 요청이 수락 됐습니다.",
                color: "success",
              });
              this.ws.close();
              console.log("%%%%% WS CLOSE %%%%%%%%%%");
              this.$router.push({ name: "Room", params: { UUID: msg.uuid } });
            }
          })
          .catch((res) => {
            // NOTE: 인원 초과 메세지 출력
            this.$store.dispatch("onSnackbar", {
              text: "입장하려는 방의 인원이 가득 찼습니다.",
              color: "red darken-1",
            });
          });
      }
    },
  },
  created() {
    // this.$log(this.$store.getters["userStore/getLocale"]);
    // TODO: 언어 설정 다른 방식이 필요해보임
    this.$root.$i18n.locale = this.$store.getters["userStore/getLocale"];
    this.connect();
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
    clearInterval(this.progressInterval);
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