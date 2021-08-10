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
          console.log(res.data)
          this.pushReviews(Item, res.data.reviewList);
        })
        .catch((err) => {
          this.$log(err);
        });
    },
    // NOTE: 유저 정보 요청

  },
}
export default ReviewMixin
