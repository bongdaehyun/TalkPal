<template>
  <v-row :justify="isMobile ? 'center' : 'end'">
    <v-col cols="6" lg="2" md="3" sm="4">
      <v-select
        @change="search"
        :items="topicItems | searchi18n"
        :label="$t('create_topic')"
        item-text="name"
        item-value="value"
        v-model="input.topic"
        item-color="#a200e0"
        style="color: #a200e0"
      ></v-select>
    </v-col>
    <v-col cols="6" lg="2" md="3" sm="4">
      <v-select
        @change="search"
        :items="LangItems | searchi18n"
        :label="$t('search_lang')"
        item-text="name"
        item-value="value"
        v-model="input.lang"
      ></v-select>
    </v-col>
  </v-row>
</template>

<script>
import i18n from "@/i18n.js";
import isMobile from "@/mixin/isMobile.js";

export default {
  name: "Search",
  mixins: [isMobile],
  data() {
    return {
      input: {
        topic: "search_total",
        lang: "search_total",
      },
      LangItems: ["search_total", "ko", "en"],
      topicItems: [
        "search_total",
        "create_music",
        "create_movie",
        "create_game",
        "create_food",
        "create_free",
      ],
    };
  },
  methods: {
    search() {
      let topic = this.input.topic;
      let lang = this.input.lang;
      if (topic == "search_total") {
        topic = "";
      } else if (lang == "search_total") {
        lang = "";
      }
      let changeData = {
        topic: topic,
        lang: lang,
      };
      // this.$log("데이터 전달");
      // this.$log(changeData);
      this.$emit("setSearchData", changeData);
    },
  },
  filters: {
    searchi18n(items) {
      return items.map((item) => {
        return {
          name: i18n.t(item),
          value: item,
        };
      });
    },
  },
};
</script>
<style scoped>
</style>