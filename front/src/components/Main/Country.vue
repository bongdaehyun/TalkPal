<template>
  <v-container>
    <v-img src="@/assets/image/logo.png"></v-img>
    <div class="d-flex justify-space-around">
      <v-sheet class="col-5 click" elevation="3" rounded="xl">
        <v-img
          @click="SelectLang(`ko`)"
          width="100%"
          src="@/assets/image/flag/ko.png"
          value="ko"
        ></v-img>
      </v-sheet>
      <v-sheet class="col-5 click" elevation="3" rounded="xl">
        <v-img
          @click="SelectLang(`en`)"
          width="100%"
          src="@/assets/image/flag/en.png"
          value="en"
        ></v-img>
      </v-sheet>
    </div>
  </v-container>
</template>


<script>
export default {
  data() {
    return {
      items: [
        { name: "한국어", value: "ko" },
        { name: "english", value: "en" },
      ],
      lang: "",
    };
  },
  methods: {
    SelectLang(val) {
      this.lang = val;
      this.$emit("onSettingLang", val);
      console.log(val);
    },
  },
  // watch: {
  //   lang(val) {
  //     this.lang = val;
  //     this.$emit("onSettingLang", val);
  //     console.log(val);
  //   },
  // },
};
</script>
<style scoped>
.click {
  cursor: pointer;
}
</style>