const ReviewMixin = {
  data() {
    return {
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
    }
  },
  methods: {
    // NOTE: 평가 목록 요청
    requestReviews(reviews) {
      if (reviews.isEnd === true) {
        return;
      }
      this.overlay = true;
      let url = "userStore/" + reviews.url;
      this.$store
        .dispatch(url, {
          userId: this.profileId,
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
    
  },
}
export default ReviewMixin
