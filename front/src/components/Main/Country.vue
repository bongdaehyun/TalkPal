<template>
  <v-container>
    <v-img src="@/assets/image/logo.png"></v-img>
    <div class="d-flex justify-space-around">
      <v-sheet
        class="col-4 click ml-auto mr-6 pa-0"
        elevation="10"
        rounded="circle"
      >
        <v-img
          @click="SelectLang(`ko`)"
          width="100%"
          src="@/assets/image/flag/ko.png"
          value="ko"
        ></v-img>
      </v-sheet>
      <v-sheet
        class="col-4 click ml-auto mr-6 pa-0"
        elevation="10"
        rounded="circle"
      >
        <v-img
          @click="SelectLang(`en`)"
          width="100%"
          src="@/assets/image/flag/en.png"
          value="en"
        ></v-img>
      </v-sheet>
      <v-sheet class="col-4 click ml-auto pa-0" elevation="10" rounded="circle">
        <v-img
          @click="SelectLang(`jp`)"
          width="100%"
          src="@/assets/image/flag/jp.png"
          value="jp"
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
        { name: "japan", value: "jp" },
      ],
      lang: "",
    };
  },
  methods: {
    SelectLang(val) {
      this.lang = val;
      this.$emit("onSettingLang", val);
    },
  },
};
</script>
<style scoped>
.click {
  cursor: pointer;
}
</style>