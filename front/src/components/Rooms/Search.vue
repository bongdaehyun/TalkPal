<template>
  <div :class="[isMobile ? 'd-flex align-end' : 'd-flex flex-column']">
    <v-select
      v-model="input.topic"
      :class="[isMobile ? 'me-3' : '']"
      @change="search"
      :items="topicItems | searchi18n"
      :label="$t('create_topic')"
      hide-details
      item-text="name"
      item-value="value"
    ></v-select>
    <v-select
      v-model="input.lang"
      :class="[isMobile ? 'ms-3' : '']"
      @change="search"
      :items="LangItems | searchi18n"
      :label="$t('search_lang')"
      hide-details
      item-text="name"
      item-value="value"
    ></v-select>
    <v-btn v-if="isMobile" @click="openCreateDialog" icon>
      <v-icon color="primary" x-large>mdi-plus</v-icon>
    </v-btn>
  </div>
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
    openCreateDialog() {
      this.$emit("openCreateDialog");
    },
    search() {
      let topic = this.input.topic;
      let lang = this.input.lang;
      if (topic == "search_total") {
        topic = "";
      }
      if (lang == "search_total") {
        lang = "";
      }
      let changeData = {
        topic: topic,
        lang: lang,
      };
      // this.$log("데이터 전달");
      this.$log(changeData);
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