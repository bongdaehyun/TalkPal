<template>
  <div class="text-center">
    <v-dialog v-model="joinQuestionDialog" width="500">
      <v-card>
        <v-card-title class="text-h6 success lighten-2">
          {{ timer }} 입장 요청을 수락하시겠습니까?
        </v-card-title>
        <!-- <v-card-text>회원 정보</v-card-text> -->
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn large color="primary" text @click="questionResponse(true)">
            예
          </v-btn>
          <v-btn large color="primary" text @click="questionResponse(false)">
            아니오
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: "QuestionDialog",
  props: {
    joinQuestionDialog: {
      type: Boolean,
    },
    timer: {
      type: Number,
    },
  },
  methods: {
    questionResponse(response) {
      this.$emit("onQuestionResponse", response);
    },
  },
};
</script>

<style>
</style>