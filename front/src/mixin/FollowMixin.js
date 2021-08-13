import http from "@/util/http-common";

const FollowMixin = {
  data() {
    return {
      isFollow: null,
      follower: {
        list: [],
        url: "requestFollowers",
        page: 0,
        count: 0,
        dialog: false,
      },
      following: {
        list: [],
        url: "requestFollowings",
        page: 0,
        count: 0,
        dialog: false,
      },
    }
  },
  methods: {
    // NOTE : 팔로우 체크
    checkFollow() {
      let url = "userStore/checkFollow";
      let followInfo = {
        fromuserid: this.loginId,
        touserid: this.profileId,
      };
      this.$store.dispatch(url, followInfo).then((res) => {
        this.isFollow = res.data;
      });
    },
    // NOTE: 팔로우 제거
    delFollow() {
      let url = "userStore/deleteFollow";
      let followInfo = {
        fromuserid: this.loginId,
        touserid: this.profileId,
      };
      this.$store.dispatch(url, followInfo).then(() => {
        this.$store.dispatch("onSnackbar", {
          text: "팔로우 해제",
          color: "success",
        });
        this.$router.go();
      });
    },
    // NOTE: 팔로우 추가
    addFollow() {
      let url = "userStore/addFollow";
      let followInfo = {
        fromuserid: this.loginId,
        touserid: this.profileId,
      };
      this.$store
        .dispatch(url, followInfo)
        .then(() => {
          this.$store.dispatch("onSnackbar", {
            text: "팔로우 추가",
            color: "success",
          });
          this.$router.go();
        })
        .catch((err) => {
          console.error(err);
        });
    },

  }
}
export default FollowMixin
