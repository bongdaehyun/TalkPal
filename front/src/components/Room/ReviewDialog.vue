<template>
  <div>
    <v-dialog v-model="dialog" width="500" persistent>
      <v-sheet
        elevation="2"
        background-color="white"
        class="d-flex flex-column"
      >
        <div class="mx-auto pt-6">
          <span class="black--text title-text">
            {{ $t("review_create_title_1") }}
          </span>
        </div>
        <div class="mx-auto pa-3">
          <span class="grey--text">
            {{ $t("review_create_title_2") }}
          </span>
        </div>
        <div class="mx-auto">
          <v-rating
            background-color="#CFD8DC"
            empty-icon="mdi-star-outline"
            color="accent"
            full-icon="mdi-star"
            half-icon="mdi-star-half-full"
            half-increments
            hover
            length="5"
            :value="score"
            size="55"
            @input="setRating"
          ></v-rating>
        </div>
        <v-col>
          <v-textarea
            v-model="content"
            :rules="rules"
            maxlength="255"
            solo
            no-resize
            counter
            background-color="#F3F3F3"
          ></v-textarea>
        </v-col>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn tile color="primary" @click="onCreateReview()">
            {{ $t("questiondialog_yes") }}
          </v-btn>
          <v-btn tile color="#F8F9FA" @click="closeDialog()">
            {{ $t("questiondialog_no") }}
          </v-btn>
        </v-card-actions>
      </v-sheet>
    </v-dialog>
  </div>
</template>

<script>
import LanguageMixin from "@/mixin/LanguageMixin.js";
export default {
  name: "ReviewDialog",
  mixins: [LanguageMixin],
  data() {
    return {
      score: 5,
      content: "",
      rules: [(v) => v.length < 255 || "최대 255글자까지 입력 가능합니다."],
      locale: this.$store.getters["userStore/getLocale"],
    };
  },
  computed: {
    dialog() {
      return this.$store.getters["reviewStore/getDialog"];
    },
    toUserId() {
      return this.$store.getters["reviewStore/getToUserId"];
    },
    loginUserId() {
      return this.$store.getters["userStore/getUserId"];
    },
  },
  methods: {
    setRating(score) {
      this.score = score;
    },
    onCreateReview() {
      this.$emit("onCreateReview", {
        content: this.content,
        score: this.score,
        to_user_id: this.toUserId,
        from_user_id: this.loginUserId,
      });
    },
    closeDialog() {
      this.$store.dispatch("reviewStore/closeDialog");
    },
  },
  watch: {
    dialog(newValue, oldValue) {
      // NOTE: 다이얼로그 열렸을 때 유저 언어로 전환
      if (newValue) {
        this.setUserLocale();
      }
    },
  },
  created() {
    this.setUserLocale();
  },
};
</script>

<style scoped>
.title-text {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>