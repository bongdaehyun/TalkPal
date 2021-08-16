<template>
  <div
    class="d-flex"
    :class="isMobile ? 'pt-3 align-center' : 'flex-column align-end'"
  >
    <v-textarea
      class="text-uppercase"
      style="width: 100%"
      v-model="query"
      :label="`${$t(`${source}`)} ➡ ${$t(`${target}`)} ${$t('translate')} `"
      rows="2"
      outlined
      hide-details
      @input="translate"
    ></v-textarea>
    <v-textarea
      style="width: 100%"
      v-model="translateText"
      solo
      rows="2"
      auto-grow
      hide-details
      readonly
      flat
    ></v-textarea>
    <v-btn icon @click="speech">
      <v-icon color="primary">mdi-bullhorn-outline</v-icon>
    </v-btn>
  </div>
</template>
<script>
import LanguageMixin from "@/mixin/LanguageMixin.js";
import isMobile from "@/mixin/isMobile.js";

import axios from "axios";
export default {
  data() {
    return {
      query: "",
      translateText: "",
      synth: window.speechSynthesis,
      Speech: new window.SpeechSynthesisUtterance(),
    };
  },
  mixins: [LanguageMixin, isMobile],
  computed: {
    source() {
      if (this.isHost) {
        return this.hostLang;
      } else {
        return this.guestLang;
      }
    },
    target() {
      if (this.isHost) {
        return this.guestLang;
      } else {
        return this.hostLang;
      }
    },
  },
  methods: {
    speech() {
      if (!this.translateText) {
        return;
      }
      this.Speech.text = this.translateText;
      this.Speech.lang = this.target;
      this.synth.speak(this.Speech);
    },
    translate() {
      if (!this.query) {
        this.translateText = "";
        return;
      }
      const source = this.checkKo(this.source);
      const target = this.checkKo(this.target);

      axios({
        type: "GET",
        url:
          `https://dapi.kakao.com/v2/translation/translate?src_lang=${source}` +
          `&target_lang=${target}` +
          `&query=${this.query}`,
        dataType: "json",
        headers: {
          Authorization: "KakaoAK f60288777b84ebe97274cfc4cdffdd37",
        },
      }).then((res) => {
        const text = res.data.translated_text[0][0];
        this.translateText = text;
      });
    },

    checkKo(value) {
      if (value === "ko") {
        return "kr";
      }
      return value;
    },
  },
};
</script>

<style>
</style>