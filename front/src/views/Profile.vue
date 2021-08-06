<template>
  <v-container
    v-if="user && receivedReviews && giveReviews && isFollow != null"
  >
    <!-- NOTE: 사용자 정보 -->
    <!-- TODO: 레이아웃 생각 중... -->
    <v-row class="mt-12 mb-5" justify="center">
      <v-col>
        <v-img :src="userImg"> </v-img>
        <v-file-input label="File input" @change="selectFile"></v-file-input>
        <v-btn @click="submitimg"></v-btn>
      </v-col>

      <v-col>
        <v-row>
          <v-col>
            <h1 v-if="!profile">{{ user.nickname }}</h1>
            <span v-else>
              <v-text-field
                v-model="user.nickname"
                :counter="10"
                label="Nickname"
                required
              ></v-text-field>
            </span>
          </v-col>
          <v-col
            class="mt-3"
            v-if="userId == $store.getters[`userStore/getUserId`] && !profile"
          >
            <v-icon @click="updateProfile">fas fa-cog</v-icon>
          </v-col>
        </v-row>
        <h3>{{ $t("profile_score") }}</h3>
        <div v-if="userId != $store.getters[`userStore/getUserId`]">
          <button v-if="isFollow" @click="addFollow">
            {{ $t("profile_follow_add") }}
          </button>
          <button v-else @click="delFollow">
            {{ $t("profile_follow_no") }}
          </button>
        </div>
      </v-col>
      <v-col>
        <v-dialog v-model="followerDialog" max-width="300px" scrollable>
          <FollowDialog
            :isDesktop="isDesktop"
            :follows="followers"
            @onCloseDialog="followerDialog = false"
          />
        </v-dialog>

        <button @click="followerDialog = true">
          <v-icon>mdi-heart</v-icon>{{ $t("profile_follower") }}
        </button>
        <span>
          {{ count.follower }}
        </span>
      </v-col>
      <v-col>
        <v-dialog v-model="followingDialog" max-width="300px" scrollable>
          <FollowDialog
            :isDesktop="isDesktop"
            :follows="followings"
            @onCloseDialog="followingDialog = false"
          />
        </v-dialog>
        <button @click="followingDialog = true">
          {{ $t("profile_following") }}
        </button>
        <span>
          {{ count.following }}
        </span>
      </v-col>
      <v-col>
        <v-row>
          <span>
            <h3>SNS</h3>
            <div v-if="!profile">
              {{ user.sns }}
            </div>
            <div v-else>
              <v-text-field
                v-model="user.sns"
                :counter="25"
                label="SNS"
              ></v-text-field>
            </div>
          </span>
        </v-row>
        <v-row>
          <span>
            <h3>{{ $t("profile_introduce") }}</h3>
            <div v-if="!profile">
              {{ user.introduction }}
            </div>
            <div v-else>
              <v-textarea
                name="input-7-1"
                filled
                auto-grow
                v-model="user.introduction"
              ></v-textarea>
            </div>
          </span>
        </v-row>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-btn color="secondary" @click="submit" v-if="profile"> 제출 </v-btn>
    </v-row>
    <!-- NOTE: 만난 사람들, 평가 내용 -->
    <v-row justify="center" class="mt-12">
      <v-col class="col-12 col-md-8">
        <!-- NOTE: 탭 메뉴 -->
        <v-tabs centered v-model="tab">
          <v-tab>{{ $t("profile_history") }}</v-tab>
          <v-tab>{{ $t("profile_receive_review") }}</v-tab>
          <v-tab>{{ $t("profile_written_review") }}</v-tab>
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
  </v-container>
</template>

<script>
import ReviewSlide from "../components/Profile/ReviewSlide.vue";
import HistorySlide from "../components/Profile/HistorySlide.vue";
import FollowDialog from "../components/Profile/FollowDialog.vue";
import http from "@/util/http-common";

export default {
  name: "Profile",
  data() {
    return {
      userImg: null,
      followers: {
        items: [],
        page: 0,
        url: "listFollower",
        isEnd: false,
      },
      followings: {
        items: [],
        page: 0,
        url: "listFollowing",
        isEnd: false,
      },
      followingDialog: false,
      followerDialog: false,
      isFollow: null,
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
      profile: false,
      count: {
        follower: 0,
        following: 0,
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
    // NOTE : 프로필 수정
    updateProfile() {
      this.profile = true;
    },
    submit() {
      console.log(this.user);
      let updateUser = {
        email: this.user.email,
        introduction: this.user.introduction,
        nickname: this.user.nickname,
        sns: this.user.sns,
      };
      http.put("/users", updateUser).then((res) => {
        console.log(res);
        if (res.data == "SUCCESS") {
          this.$store.dispatch("onSnackbar", {
            text: "수정 완료.",
            color: "success",
          });
          this.profile = false;
        }
      });
    },

    async submitimg() {
      const imgFile = new FormData();
      imgFile.append("imgFile", this.image);

      try {
        const { data } = await http.put(
          "/users/saveimg/" + this.userId,
          imgFile,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
        console.log(data);
        
      } catch (err) {
        console.log(err);
      }
    },

    selectFile(file) {
      this.image = file;
      console.log(this.image);
    },

    // NOTE : 팔로우 체크
    checkFollow() {
      // this.$log("팔로우 체크 실행");
      let url = "userStore/checkFollow";
      let followInfo = {
        fromuserid: this.$store.getters["userStore/getUserId"],
        touserid: this.userId,
      };
      this.$store.dispatch(url, followInfo).then((res) => {
        this.isFollow = res.data;
        // this.$log(res.data);
      });
    },
    // NOTE: 팔로우 제거
    delFollow() {
      let url = "userStore/deleteFollow";
      let followInfo = {
        fromuserid: this.$store.getters["userStore/getUserId"],
        touserid: this.userId,
      };
      this.$store.dispatch(url, followInfo).then(() => {
        this.$store.dispatch("onSnackbar", {
          text: "팔로우 해제",
          color: "success",
        });
        this.isFollow = true;
        // TODO: 팔로워 목록 다시 불러와야함.
        // this.$router.go();
      });
    },
    // NOTE: 팔로우 추가
    addFollow() {
      // 로그인한 유저
      // this.$log(this.$store.getters["userStore/getUserId"]);
      // 프로필 페이지 유저
      // this.$log(this.userId);
      let url = "userStore/addFollow";
      let followInfo = {
        fromuserid: this.$store.getters["userStore/getUserId"],
        touserid: this.userId,
      };
      this.$store
        .dispatch(url, followInfo)
        .then(() => {
          this.$store.dispatch("onSnackbar", {
            text: "팔로우 추가",
            color: "success",
          });
          this.isFollow = false;
          // TODO: 팔로워 목록 다시 불러와야함.
          // this.$router.go();
        })
        .catch((err) => {
          console.error(err);
          this.$log("이미 요청한 팔로우");
        });
    },
    // NOTE: 팔로워 개수
    countFollower() {
      http.get(`/follow/countfollower/${this.userId}`).then((res) => {
        console.log("팔로워", res);
        this.count.follower = res.data;
      });
    },
    // NOTE: 팔로잉 개수
    countFollowing() {
      http.get(`/follow/countfollowing/${this.userId}`).then((res) => {
        //console.log("팔로잉", res);
        this.count.following = res.data;
      });
    },
    // NOTE: 응답 리뷰 목록 추가
    pushReviews(reviews, res) {
      // this.$log(res);
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
          this.$log(err);
        });
    },
    // NOTE: 유저 정보 요청
    requestUserInfo() {
      this.$store
        .dispatch("userStore/requestUserInfo", this.userId)
        .then((res) => {
          this.user = res.data;
          this.userImg = require("@/assets/image/"+this.userId+"profileImg.jpg");
          console.log(this.user.imgPath);
        })
        .catch((err) => {
          this.$log(err);
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
        this.$log(error);
      }
    },
  },
  created() {
    this.requestUserInfo();
    this.requestReviews(this.receivedReviews);
    this.requestReviews(this.giveReviews);
    this.reuqestHistoryUser();
    this.checkFollow();
    this.countFollower();
    this.countFollowing();
  
  },
  components: {
    FollowDialog,
    ReviewSlide,
    HistorySlide,
  },
};
</script>

<style>
</style>