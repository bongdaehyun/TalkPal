<template>
  <v-container class="pa-0" :class="[isMobile ? '' : 'mt-12']">
    <div class="row no-gutters justify-center">
      <v-sheet
        elevation="2"
        class="sticky"
        :class="[isMobile ? 'sticky-mobile' : ' ']"
        tile
      >
        <!-- NOTE: 방 조건 검색 -->
        <Search
          @setSearchData="changeSearchData"
          @openCreateDialog="$refs.createDialog.dialog = true"
        />
        <!-- NOTE: 방 생성 -->
        <Create ref="createDialog" @onCreateRoom="onCreateRoom" />
      </v-sheet>
      <!-- NOTE: 방 목록 -->
      <div class="row justify-center" :class="[isMobile ? 'col-12 ' : 'col-8']">
        <v-col v-for="item in rooms" :key="item.id" lg="4" md="6" cols="12">
          <Item :item="item" @onEnterRoom="onEnterRoom" />
        </v-col>
        <infinite-loading :identifier="searchData" @infinite="requestRooms">
          <!-- NOTE: 방조건 검색시 이벤트 처리 나누기 -->
          <div slot="no-more"></div>
          <div slot="no-results">
            <v-col>
              <v-row justify="center">
                <v-icon x-large color="error">mdi-close-box</v-icon>
              </v-row>
              <v-row justify="center">
                <h1>{{ $t("alert_nolist_01") }}</h1>
              </v-row>
              <v-row justify="center">
                <h3>{{ $t("alert_nolist_02") }}</h3>
              </v-row>
            </v-col>
          </div>
        </infinite-loading>
      </div>
    </div>
    <!-- NOTE: 스크롤 올리는 버튼 -->
    <v-fab-transition>
      <v-btn
        v-show="btnShow"
        class="fab-position"
        @click.prevent="$vuetify.goTo('.header')"
        color="primary"
        right
        fixed
        fab
      >
        <v-icon>mdi-chevron-double-up</v-icon>
      </v-btn>
    </v-fab-transition>
    <v-overlay :value="loadingAnswer">
      <v-progress-circular indeterminate size="128">
        대기중...
      </v-progress-circular>
    </v-overlay>
    <ReviewDialog
      :reviewDialog="reviewDialog"
      :reviewUserId="reviewUserId"
      @onReviewSubmit="reviewSubmit"
    />
  </v-container>
</template>

<script>
import Item from "@/components/Rooms/Item";
import Create from "@/components/Rooms/Create";
import Search from "@/components/Rooms/Search";
import InfiniteLoading from "vue-infinite-loading";
import isMobile from "@/mixin/isMobile.js";
import ReviewDialog from "@/components/Review/ReviewDialog.vue";
import ReviewMixin from "@/mixin/ReviewMixin.js";

export default {
  name: "Room",
  mixins: [isMobile, ReviewMixin],
  data() {
    return {
      socketUrl: process.env.VUE_APP_SOCKET_URL,
      page: 1,
      rooms: [],
      ws: null,
      searchData: {
        topic: "",
        lang: "",
      },
      btnShow: false,
      loadingAnswer: false,
      // reviewDialog: false,
      // reviewUserId: null,
    };
  },
  methods: {
    // reviewSubmit(reviewInfo) {
    //   if (reviewInfo.isReview) {
    //     reviewInfo.from_user_id = this.$store.getters["userStore/getUserId"];
    //     this.$store
    //       .dispatch("userStore/submitReview", reviewInfo)
    //       .then(() => {
    //         this.$store.dispatch("onSnackbar", {
    //           text: "리뷰 작성 완료",
    //           color: "success",
    //         })
    //       })
    //       .catch((err) => {
    //         console.error(err);
    //       });
    //   }
    //   this.$store.dispatch("roomStore/setReviewFalse");
    //   this.reviewDialog = false;
    //   this.reviewUserId = null;
    // },
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
    sendMessage(message) {
      if (this.ws.readyState !== this.ws.OPEN) {
        this.$log("[errMessage] Skip, WebSocket session isn't open" + message);
        return;
      }
      const jsonMessage = JSON.stringify(message);
      this.$log("[sendMessage] message: " + jsonMessage);
      this.ws.send(jsonMessage);
    },
    /*
    NOTE: 방 목록 요청
    NOTE: https://www.npmjs.com/package/vue-infinite-loading
    NOTE: 페이지네이션 정보 - 5개씩
     */
    changeSearchData(changeData) {
      this.searchData = changeData;
      this.page = 1;
      this.rooms = [];
    },
    requestRooms($state) {
      this.$store
        .dispatch("roomStore/requestRooms", {
          topic: this.searchData.topic,
          lang: this.searchData.lang,
          page: this.page,
        })
        .then((res) => {
          const data = res.data.roomResList;
          if (data.length) {
            this.page += 1;
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

      // NOTE: 입장 요청 대기 시작
      this.loadingAnswer = true;

      // NOTE: 웹소켓 메세지 전송
      this.sendMessage(message);
    },
    onJoinAnswer(msg) {
      // NOTE: 입장 응답 대기 종료
      this.loadingAnswer = false;
      const answer = msg.answer;
      if (answer === "false") {
        // NOTE: 입장 거절 시 거절되었다는 안내문 메세지만 출력
        this.$store.dispatch("onSnackbar", {
          text: "입장 요청이 거부 됐습니다.",
          color: "error",
        });
      } else if (answer === "true") {
        // NOTE: 입장 수락 시 방 인원 초과 여부 확인 후 입장
        this.$store
          .dispatch("roomStore/requestCheckJoin", msg.uuid)
          .then((res) => {
            const flag = res.data;
            // NOTE: join ok인거 굳이 체크 안해도 됨
            if (flag === "join ok") {
              // NOTE: 입장 가능
              this.$store.dispatch("onSnackbar", {
                text: "입장 요청이 수락 됐습니다.",
                color: "primary",
              });
              this.$router.push({ name: "Room", params: { UUID: msg.uuid } });
            }
          })
          .catch((res) => {
            // NOTE: 인원 초과 메세지 출력
            this.$store.dispatch("onSnackbar", {
              text: "입장하려는 방의 인원이 가득 찼습니다.",
              color: "error",
            });
          });
      }
    },
  },
  created() {
    this.$store.dispatch("roomStore/exitRoom");
    this.checkReview();
    // let reviewInfo = this.$store.getters["roomStore/getIsReview"];
    // this.reviewDialog = reviewInfo.reviewDialog;
    // this.reviewUserId = reviewInfo.reviewUserId;
    this.connect();
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
    clearInterval(this.progressInterval);
    // this.ws.close();
  },
  components: {
    Item,
    Create,
    Search,
    InfiniteLoading,
    ReviewDialog,
  },
};
</script>

<style scoped>
.sticky {
  background-color: white;
  position: fixed;
  padding: 2rem !important;
  z-index: 2;
  left: 1vw;
  width: 15vw;
}
.sticky-mobile {
  left: 0 !important;
  width: 100%;
}
.fab-position {
  bottom: 56px;
}
</style>