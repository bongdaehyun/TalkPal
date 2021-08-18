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
        <div class="d-flex flex-column align-center">
          <span style="font-weight: bold; font-size: 2rem">
            이미지 및 아이콘 라이센스 정보
          </span>
          <a href="http://www.freepik.com">Designed by Freepik</a>
          <a href="http://www.freepik.com">
            Designed by pikisuperstar / Freepik
          </a>
          <a href="http://www.freepik.com">Designed by stories / Freepik</a>
          <div>
            Icons made by
            <a href="https://www.freepik.com" title="Freepik">Freepik</a>
            from
            <a href="https://www.flaticon.com/" title="Flaticon">
              www.flaticon.com
            </a>
          </div>
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
  name: "LicenseDialog",
  mixins: [isMobile],
  data() {
    return {
      dialog: false,
    };
  },
  computed: {
    dialogWidth() {
      if (this.isMobile) {
        return "50vw";
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