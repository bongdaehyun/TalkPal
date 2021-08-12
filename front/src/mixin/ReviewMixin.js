const ReviewMixin = {
  data() {
    return {
      receivedReviews: {
        list: [],
        page: 1,
        category: "receive",
        url: "requestReceivedReviews",
        isEnd: false,
      },
      giveReviews: {
        list: [],
        page: 1,
        category: "give",
        url: "requestGiveReviews",
        isEnd: false,
      },
      reviewDialog: false,
      reviewUserId: null,
      opponentId: null,
    }
  },
  methods: {
    // NOTE: 응답 리뷰 목록 추가
    pushReviews(Item, res) {
      if (res.length) {
        Item.list.push(...res);
        Item.page = Item.page + 1;
      } else {
        Item.isEnd = true;
      }
      this.overlay = false;
    },
    // NOTE: 평가 목록 요청
    requestReviews(Item) {
      if (Item.isEnd === true) {
        return;
      }
      this.overlay = true;
      let url = "userStore/" + Item.url;
      this.$store
        .dispatch(url, {
          userId: this.profileId,
          page: Item.page,
        })
        .then((res) => {
          this.pushReviews(Item, res.data.reviewList);
        })
        .catch((err) => {
          this.$log(err);
        });
    },
    // NOTE: 유저 정보 요청

    // NOTE: 리뷰 작성 여부 상태 세팅
    checkReview() {
      let reviewInfo = this.$store.getters["roomStore/getIsReview"];
      this.reviewDialog = reviewInfo.reviewDialog;
      this.reviewUserId = reviewInfo.reviewUserId;
    },

    // NOTE: 호스트 리뷰창 띄우기
    openReviewDialog(reviewUserId) {
      this.reviewUserId = reviewUserId;
      this.reviewDialog = true;
      this.opponentId = null;
    },

    // NOTE: 리뷰 작성
    reviewSubmit(reviewInfo) {
      if (reviewInfo.isReview) {
        reviewInfo.from_user_id = this.$store.getters["userStore/getUserId"];
        this.$store
          .dispatch("userStore/submitReview", reviewInfo)
          .then(() => {
            this.$store.dispatch("onSnackbar", {
              text: "리뷰 작성 완료",
              color: "success",
            })
          })
          .catch((err) => {
            console.error(err);
          });
      }
      this.$store.dispatch("roomStore/setReviewFalse");
      this.reviewDialog = false;
      this.reviewUserId = null;
    },
  },
}
export default ReviewMixin
