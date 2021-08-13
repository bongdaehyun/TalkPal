<template>
  <div>
    <v-dialog v-model="reviewDialog" width="500" persistent>
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
          <v-btn tile color="primary" @click="reviewSubmit">
            {{ $t("questiondialog_yes") }}
          </v-btn>
          <v-btn tile color="#F8F9FA" @click="closeReviewDialog">
            {{ $t("questiondialog_no") }}
          </v-btn>
        </v-card-actions>
      </v-sheet>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: "ReviewDialog",
  data() {
    return {
      score: 5,
      content: "",
      rules: [(v) => v.length < 255 || "최대 255글자까지 입력 가능합니다."],
    };
  },
  props: {
    reviewDialog: {
      type: Boolean,
    },
    reviewUserId: {
      type: String,
    },
  },
  methods: {
    setRating(score) {
      this.score = score;
    },
    reviewSubmit() {
      this.$emit("onReviewSubmit", {
        content: this.content,
        score: this.score,
        to_user_id: this.reviewUserId,
      });
    },
    closeReviewDialog() {
      this.$emit("closeReviewDialog");
    },
  },
};
</script>

<style scoped>
.title-text {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>