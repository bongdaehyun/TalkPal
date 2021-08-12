<template>
  <div class="text-center">
    <v-dialog v-model="dialog" :width="dialogWidth">
      <v-sheet class="d-flex flex-column" elevation="2">
        <div
          color="white"
          class="pa-3"
          :class="[isMobile ? 'col-12' : ' col-3']"
          elevation="2"
        >
          <v-img src="@/assets/image/logo.png"></v-img>
        </div>
        <div class="row no-gutters justify-space-around">
          <v-sheet
            class="d-flex flex-column align-center"
            :class="[isMobile ? 'col-10' : 'col-2']"
          >
            <v-img
              class="elevation-6 mb-2"
              src="@/assets/image/us/jeong.jpg"
            ></v-img>
            <span class="primary--text font-weight-bold">봉대현</span>
            <span class="grey--text font-weight-bold">Leader</span>
            <v-btn icon> <v-icon>mdi-github</v-icon> </v-btn>
          </v-sheet>
          <v-sheet
            class="d-flex flex-column align-center"
            :class="[isMobile ? 'col-10' : 'col-2']"
          >
            <v-img
              class="elevation-6 mb-2"
              src="@/assets/image/us/jeong.jpg"
            ></v-img>
            <span class="primary--text font-weight-bold">정우영</span>
            <span class="grey--text font-weight-bold">Frontend</span>
            <v-btn icon> <v-icon>mdi-github</v-icon> </v-btn>
          </v-sheet>
          <v-sheet
            class="d-flex flex-column align-center"
            :class="[isMobile ? 'col-10' : 'col-2']"
          >
            <v-img
              class="elevation-6 mb-2"
              src="@/assets/image/us/jeong.jpg"
            ></v-img>
            <span class="primary--text font-weight-bold">안광식</span>
            <span class="grey--text font-weight-bold">Web RTC</span>
            <v-btn icon> <v-icon>mdi-github</v-icon> </v-btn>
          </v-sheet>
          <v-sheet
            class="d-flex flex-column align-center"
            :class="[isMobile ? 'col-10' : 'col-2']"
          >
            <v-img
              class="elevation-6 mb-2"
              src="@/assets/image/us/jeong.jpg"
            ></v-img>
            <span class="primary--text font-weight-bold">윤경한</span>
            <span class="grey--text font-weight-bold">Backend</span>
            <v-btn icon> <v-icon>mdi-github</v-icon> </v-btn>
          </v-sheet>
        </div>
        <v-sheet class="d-flex justify-end pa-1">
          <v-btn color="primary" text @click="dialog = false">Close</v-btn>
        </v-sheet>
      </v-sheet>
    </v-dialog>
  </div>
</template>

<script>
import isMobile from "@/mixin/isMobile";
export default {
  name: "AboutUsDialog",
  mixins: [isMobile],
  data() {
    return {
      dialog: false,
    };
  },
  computed: {
    dialogWidth() {
      if (this.isMobile) {
        return "90vw";
      } else {
        return "50vw";
      }
    },
  },
};
</script>

<style scoped>
.title-text {
  font-size: 1.5rem;
}
.grayscale {
  -webkit-filter: grayscale(100%);
  filter: grayscale(100%);
}
</style>