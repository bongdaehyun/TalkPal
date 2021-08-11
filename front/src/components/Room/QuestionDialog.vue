<template>
  <div class="text-center">
    <v-dialog v-model="joinQuestionDialog" width="500">
      <v-card>
        <v-card-title class="text-h6 success lighten-2">
          {{ timer }} {{$t('questiondialog')}}
        </v-card-title>
        <v-card-text>회원 정보

          
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn large color="primary" text @click="questionResponse(true)">
            {{$t('questiondialog_yes')}}
          </v-btn>
          <v-btn large color="primary" text @click="questionResponse(false)">
            {{$t('questiondialog_no')}}
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