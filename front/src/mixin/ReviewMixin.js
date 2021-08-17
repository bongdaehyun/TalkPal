import LanguageMixin from "@/mixin/LanguageMixin.js";

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
  mixins: [LanguageMixin],

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
      let url = "reviewStore/" + Item.url;
      this.$store
        .dispatch(url, {
          userId: this.profileId,
          page: Item.page,
        })
        .then((res) => {
          this.pushReviews(Item, res.data.reviewList);
        })
        .catch((err) => {
          console.error(err)
        });
    },

    // NOTE: 리뷰 작성
    createReview(response) {
      this.$store
        .dispatch("reviewStore/createReview", response)
        .then(() => {
          this.$store.dispatch("onSnackbar", {
            text: "리뷰 작성 완료",
            color: "success",
          })
        })
        .catch((err) => {
          console.error(err);
        });
      this.$store.dispatch("reviewStore/closeDialog");
    },
  },
}
export default ReviewMixin
