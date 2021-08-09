<template>
  <v-container v-if="profileInfo && isFollow != null">
    <!-- NOTE: 사용자 정보 -->
    <div class="d-flex flex-column my-12">
      <v-row justify="center" no-gutters>
        <v-col cols="6" class="d-flex flex-column align-end pe-6">
          <!-- NOTE: 프로필 이미지 -->
          <v-avatar size="128">
            <v-hover v-slot="{ hover }">
              <v-img :src="profileImg">
                <div
                  v-if="hover && profileId == loginId"
                  class="d-flex justify-center align-center"
                  style="height: 100%; width: 100%"
                >
                  <!-- NOTE: [Hover] 프로필 이미지 수정 버튼 -->
                  <div v-show="false">
                    <input
                      ref="fileInput"
                      type="file"
                      @change="changeProfileImage"
                    />
                  </div>
                  <v-btn
                    class="white--text"
                    color="indigo"
                    @click="clickChangeImage"
                  >
                    {{ $t("profile_Updateimage") }}
                    <v-icon right dark> fas fa-cog </v-icon>
                  </v-btn>
                </div>
              </v-img>
            </v-hover>
          </v-avatar>
        </v-col>
        <v-col cols="6" md="3" class="d-flex flex-column justify-center ps-6">
          <!-- NOTE: 만난 사람들 목록 -->
          <div v-if="!update">
            <v-dialog
              v-model="histories.dialog"
              :max-width="dialogMaxWidth"
              scrollable
            >
              <UserListDialog
                :head="$t('profile_history')"
                :profileId="profileId"
                :Item="histories"
              />
            </v-dialog>
            <button @click="histories.dialog = true">
              {{ $t("profile_history") }}
              <span class="font-weight-bold text--black">
                {{ histories.count }}
              </span>
            </button>
          </div>
          <div v-if="!update" class="d-flex justify-start">
            <!-- NOTE: 팔로우 목록 -->
            <div class="pe-3">
              <v-dialog
                v-model="follower.dialog"
                :max-width="dialogMaxWidth"
                scrollable
              >
                <UserListDialog
                  :head="$t('profile_follower')"
                  :profileId="profileId"
                  :Item="follower"
                />
              </v-dialog>
              <button @click="follower.dialog = true">
                {{ $t("profile_follower") }}
                <span class="font-weight-bold text--black">
                  {{ profileInfo.cntFollower }}
                </span>
              </button>
            </div>
            <!-- NOTE: 팔로잉 목록 -->
            <div class="ps-3">
              <v-dialog
                v-model="following.dialog"
                :max-width="dialogMaxWidth"
                scrollable
              >
                <UserListDialog
                  :head="$t('profile_following')"
                  :profileId="profileId"
                  :Item="following"
                />
              </v-dialog>
              <button @click="following.dialog = true">
                {{ $t("profile_following") }}
                <span class="font-weight-bold text--black">
                  {{ profileInfo.cntFollowing }}
                </span>
              </button>
            </div>
          </div>
          <!-- NOTE: 닉네임 팔로우 자기소개 -->
          <div class="d-flex align-center">
            <div
              v-if="!update"
              class="font-weight-bold pe-1"
              style="font-size: 2rem"
            >
              <!-- NOTE: 닉네임 -->
              <span>
                {{ profileInfo.nickname }}
              </span>
            </div>
            <!-- NOTE: 닉네임 수정 -->
            <div v-else>
              <v-text-field
                v-model="nickname"
                label="Nickname"
                solo
                required
                full-width
                :counter="16"
                :error-messages="nicknameErrors"
                @input="$v.nickname.$touch()"
                @blur="$v.nickname.$touch()"
              ></v-text-field>
            </div>
            <!-- NOTE: 정보 수정 전환 버튼-->
            <div v-if="profileId == loginId && !update" class="ms-1 pb-1 ps-1">
              <v-icon
                @click="
                  () => {
                    this.nickname = this.profileInfo.nickname;
                    update = true;
                  }
                "
                >fas fa-cog</v-icon
              >
            </div>
            <!--  NOTE: 팔로우 버튼 -->
            <div
              v-if="profileId != loginId && isFollow != null"
              class="ms-1 pb-1 ps-1"
            >
              <button v-if="isFollow" dark @click="addFollow">
                <v-icon color="purple">mdi-link-variant</v-icon>
              </button>
              <button v-else dark @click="delFollow">
                <v-icon color="purple">mdi-link-variant-off</v-icon>
              </button>
            </div>
          </div>
          <!-- NOTE: 자기소개 -->
          <div v-if="!update">
            {{ profileInfo.introduction }}
          </div>
          <!-- NOTE: 자기소개 수정 -->
          <div v-else>
            <v-textarea
              rows="2"
              outlined
              hide-details
              v-model="profileInfo.introduction"
            ></v-textarea>
          </div>
          <div v-if="update" class="align-self-end">
            <!-- NOTE: 수정 완료 버튼 -->
            <v-btn
              class="mt-3"
              outlined
              color="primary"
              @click="submitUpdateButton"
            >
              {{ $t("profile_submit") }}
            </v-btn>
          </div>
        </v-col>
        <v-col></v-col>
      </v-row>
    </div>
    <!-- NOTE: 만난 사람들, 평가 내용 -->
    <v-row justify="center" class="mt-12">
      <v-col class="col-12 col-md-8">
        <!-- NOTE: 탭 메뉴 -->
        <v-tabs centered v-model="tab">
          <!-- <v-tab>{{ $t("profile_history") }}</v-tab> -->
          <v-tab>{{ $t("profile_receive_review") }}</v-tab>
          <v-tab>{{ $t("profile_written_review") }}</v-tab>
        </v-tabs>
        <v-tabs-items v-model="tab" :touchless="true">
          <v-tab-item>
            <ReviewSlide
              :reviews="receivedReviews"
              @onSlideEnd="requestReviews"
            />
          </v-tab-item>
          <!-- NOTE: 작성한 평가 -->
          <v-tab-item>
            <ReviewSlide :reviews="giveReviews" @onSlideEnd="requestReviews" />
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
import UserListDialog from "../components/Profile/UserListDialog.vue";
import http from "@/util/http-common";
import { validationMixin } from "vuelidate";
import {
  required,
  minLength,
  maxLength,
  alphaNum,
} from "vuelidate/lib/validators";
import i18n from "@/i18n.js";
import isMobile from "@/mixin/isMobile.js";
import ReviewMixin from "@/mixin/ReviewMixin.js";
import FollowMixin from "@/mixin/FollowMixin.js";

export default {
  name: "Profile",
  mixins: [isMobile, ReviewMixin, FollowMixin, validationMixin],

  data() {
    return {
      profileId: Number(this.$route.params.userId),
      loginId: this.$store.getters[`userStore/getUserId`],
      profileInfo: {},
      profileImg: null,
      nickname: null,
      overlay: false,
      tab: null,
      histories: {
        list: [],
        url: "requestHistories",
        count: 0,
        dialog: false,
      },
      update: false,
    };
  },
  computed: {
    dialogMaxWidth() {
      if (this.isMobile) {
        return "90%";
      }
      return "15%";
    },
    nicknameErrors() {
      const errors = [];
      if (!this.$v.nickname.$dirty) return errors;
      !this.$v.nickname.required && errors.push(i18n.t("register_required"));
      (!this.$v.nickname.minLength || !this.$v.nickname.maxLength) &&
        errors.push(i18n.t("register_error_nick_len"));
      !this.$v.nickname.alphaNum &&
        errors.push(i18n.t("register_error_nick_alpha"));
      !this.$v.nickname.isUnique &&
        errors.push(i18n.t("register_error_nick_same"));
      return errors;
    },
  },
  validations: {
    nickname: {
      required,
      minLength: minLength(2),
      maxLength: maxLength(16),
      alphaNum,
      async isUnique(nickname) {
        if (nickname === "") return true;
        // 현재 닉네임과 동일한지
        if (nickname == this.profileInfo.nickname) {
          return true;
        }
        try {
          // 중복 검사 통과
          const res = await http.get(`/users/checknick/${nickname}`);
          return true;
        } catch (error) {
          // 중복 검사 실패
          return false;
        }
      },
    },
  },
  methods: {
    // NOTE: 프로필 유저 정보 요청
    requestProfileInfo() {
      this.$store
        .dispatch("userStore/requestUserInfo", this.profileId)
        .then((res) => {
          this.profileInfo = res.data;
          this.follower.count = res.data.cntFollower;
          this.following.count = res.data.cntFollowing;
          this.histories.count = res.data.cntHistories;
          const imgPath = this.profileInfo.imgPath;
          // NOTE: 프로필 유저 정보 배포된 서버 설정 필요
          if (imgPath) {
            this.profileImg = require(`@/assets/image/profile/${imgPath}`);
          } else {
            this.profileImg = require(`@/assets/image/profile/default_profileImg.png`);
          }
          /*
          
          */
        })
        .catch((err) => {
          this.$log(err);
        });
    },
    // NOTE: 수정 완료
    submitUpdateButton() {
      this.$v.$touch();
      // NOTE: 유효성 검증
      if (!this.$v.$invalid) {
        let updateUser = {
          email: this.profileInfo.email,
          introduction: this.profileInfo.introduction,
          nickname: this.nickname,
          sns: this.profileInfo.sns,
        };
        http.put("/users", updateUser).then((res) => {
          if (res.data == "SUCCESS") {
            this.profileInfo.nickname = this.nickname;
            this.$store.dispatch("onSnackbar", {
              text: `{{$t('profile_submit')}}`,
              color: "success",
            });
            this.update = false;
          }
        });
      }
    },
    // NOTE: 이미지 수정 버튼 클릭
    clickChangeImage() {
      this.$refs.fileInput.click();
    },
    // NOTE: 프로필 이미지 수정
    async changeProfileImage(e) {
      this.image = e.target.files[0];
      const imgFile = new FormData();
      imgFile.append("imgFile", this.image);

      try {
        const { data } = await http.put(
          "/users/saveimg/" + this.profileId,
          imgFile,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
        this.loadingButtonImage = false;
      } catch (err) {
        console.log(err);
      }
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
    // NOTE: 만난 사람들 요청
    async reuqestHistoryUser() {
      try {
        const res = await this.$store.dispatch(
          "userStore/requestUserHistories",
          this.profileId
        );
        this.histories = res.data.historyList;
      } catch (error) {
        this.$log(error);
      }
    },
  },
  created() {
    this.requestProfileInfo();
    this.requestReviews(this.receivedReviews);
    this.requestReviews(this.giveReviews);
    this.checkFollow();
  },
  components: {
    UserListDialog,
    ReviewSlide,
  },
};
</script>

<style>
</style>