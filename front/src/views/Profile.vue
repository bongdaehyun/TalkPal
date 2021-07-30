<template>
  <div v-if="user && receivedReviews && giveReviews">
    <!-- NOTE: 사용자 정보 -->
    <!-- TODO: 레이아웃 생각 중... -->
    <v-row justify="center">
      <v-col>
        <v-img src="@/assets/image/flag/en.png"> </v-img>
      </v-col>
      <v-col>
        <h1>{{ user.nickname }}</h1>
        <h3>평균 평점</h3>
      </v-col>
      <v-col> 팔로워</v-col>
      <v-col> 팔로우</v-col>
      <v-col>
        <h3>SNS</h3>
        <h3>자기소개</h3>
      </v-col>
    </v-row>
    <!-- NOTE: 만난 사람들, 평가 내용 -->
    <v-row justify="center">
      <v-col class="col-12 col-md-8">
        <!-- NOTE: 탭 메뉴 -->
        <v-tabs centered v-model="tab">
          <v-tab>만난 사람들</v-tab>
          <v-tab>받은 평가</v-tab>
          <v-tab>작성한 평가</v-tab>
        </v-tabs>
        <v-tabs-items v-model="tab" :touchless="true">
          <!-- NOTE: 만난 사람들 -->
          <v-tab-item>
            <HistorySlide :isDesktop="isDesktop" :histories="histories" />
          </v-tab-item>
          <!-- NOTE: 받은 평가 -->
          <v-tab-item>
            <ReviewSlide
              :isDesktop="isDesktop"
              :reviews="receivedReviews"
              @onSlideEnd="requestReviews"
            />
          </v-tab-item>
          <!-- NOTE: 작성한 평가 -->
          <v-tab-item>
            <ReviewSlide
              :isDesktop="isDesktop"
              :reviews="giveReviews"
              @onSlideEnd="requestReviews"
            />
          </v-tab-item>
        </v-tabs-items>
      </v-col>
      <v-overlay :value="overlay">
        <v-progress-circular indeterminate size="256"></v-progress-circular>
      </v-overlay>
    </v-row>
  </div>
</template>

<script>
import ReviewSlide from "../components/Profile/ReviewSlide.vue";
import HistorySlide from "../components/Profile/HistorySlide.vue";

export default {
  name: "Profile",
  data() {
    return {
      overlay: false,
      userId: this.$route.params.userId,
      user: null,
      tab: null,
      histories: [],
      giveReviews: {
        items: [],
        page: 1,
        category: "give",
        url: "requestGiveReviews",
        isEnd: false,
      },
      receivedReviews: {
        items: [],
        page: 1,
        category: "receive",
        url: "requestReceivedReviews",
        isEnd: false,
      },
    };
  },
  computed: {
    isDesktop() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
          return false;
        case "sm":
          return false;
        default:
          return true;
      }
    },
  },

  methods: {
    // NOTE: 응답 리뷰 목록 추가
    pushReviews(reviews, res) {
      // console.log(res);
      if (res.length) {
        reviews.items.push(...res);
        reviews.page = reviews.page + 1;
      } else {
        reviews.isEnd = true;
      }
      this.overlay = false;
    },
    // NOTE: 평가 목록 요청
    requestReviews(reviews) {
      if (reviews.isEnd === true) {
        return;
      }
      this.overlay = true;
      let url = "userStore/" + reviews.url;
      this.$store
        .dispatch(url, {
          userId: this.userId,
          page: reviews.page,
        })
        .then((res) => {
          this.pushReviews(reviews, res.data.reviewList);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // NOTE: 유저 정보 요청
    requestUserInfo() {
      this.$store
        .dispatch("userStore/requestUserInfo", this.userId)
        .then((res) => {
          this.user = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // NOTE: 만난 사람들 요청
    async reuqestHistoryUser() {
      try {
        const res = await this.$store.dispatch(
          "userStore/requestUserHistories",
          this.userId
        );
        this.histories = res.data.historyList;
      } catch (error) {
        console.log(error);
      }
    },
  },
  created() {
    this.requestUserInfo();
    this.requestReviews(this.receivedReviews);
    this.requestReviews(this.giveReviews);
    this.reuqestHistoryUser();
  },
  components: {
    ReviewSlide,
    HistorySlide,
  },
};
</script>

<style>
</style>