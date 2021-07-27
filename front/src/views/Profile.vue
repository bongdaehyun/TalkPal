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
        <h3>{{ receivedReviews.avgScore }}</h3>
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
          <!-- NOTE: 만난 사람들 탭 -->
          <v-tab-item>
            <Slide :isDesktop="isDesktop" />
          </v-tab-item>
          <!-- NOTE: 받은 평가 탭 -->
          <v-tab-item>
            <Slide
              :isDesktop="isDesktop"
              :items="receivedReviews.reviewList"
              :category="`receive`"
            />
          </v-tab-item>
          <!-- NOTE: 작성한 평가 탭 -->
          <v-tab-item>
            <Slide
              :isDesktop="isDesktop"
              :items="giveReviews.reviewList"
              :category="`give`"
            />
          </v-tab-item>
        </v-tabs-items>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import Slide from "../components/Profile/Slide.vue";

export default {
  name: "Profile",
  data() {
    return {
      user: null,
      tab: null,
      giveReviews: null,
      receivedReviews: null,
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
    // NOTE: 유저 정보 요청
    requestUserInfo(userId) {
      this.$store
        .dispatch("userStore/requestUserInfo", userId)
        .then((res) => {
          this.user = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // NOTE: 작성한 평가 요청
    requestGiveReviews(userId) {
      this.$store
        .dispatch("userStore/requestGiveReviews", userId)
        .then((res) => {
          this.giveReviews = res.data;
          // console.log(this.giveReviews);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // NOTE: 받은 평가 요청
    requestReceivedReviews(userId) {
      this.$store
        .dispatch("userStore/requestReceivedReviews", userId)
        .then((res) => {
          this.receivedReviews = res.data;
          // console.log(this.receivedReviews);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  created() {
    let userId = this.$route.params.userId;
    this.requestUserInfo(userId);
    this.requestGiveReviews(userId);
    this.requestReceivedReviews(userId);
  },
  components: {
    Slide,
  },
};
</script>

<style>
</style>