<template>
  <v-container>
    <!-- <v-select
      v-model="lang"
      :items="items"
      item-text="name"
      item-value="value"
      label="Select your language"
      outlined
    ></v-select> -->
    <v-row>
      <v-col>
        <v-img src="@/assets/image/logo.png"></v-img>
      </v-col>

      <v-col cols="6">
        <v-img
          @click="SelectLang(`ko`)"
          width="100%"
          src="@/assets/image/flag/ko.png"
          value="ko"
        ></v-img>
      </v-col>
      <v-col cols="6">
        <v-img
          @click="SelectLang(`en`)"
          width="100%"
          src="@/assets/image/flag/enb.png"
          value="en"
        ></v-img>
      </v-col>
    </v-row>
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