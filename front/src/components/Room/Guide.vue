<template>
  <div
    class="d-flex flex-column"
    style="background-color: #ffffff"
    :class="isMobile ? 'pa-3' : ''"
    :style="{ height: height }"
  >
    <div class="d-flex justify-center align-center text-button">
      <v-icon>mdi-web</v-icon>
      <div class="d-flex justify-center align-center">
        <span
          class="ms-3 me-3"
          :class="[isHostGuide ? 'font-weight-black' : 'font-weight-thin']"
        >
          {{ hostLang }}
        </span>
        <v-btn icon @click="changeGuideLocale">
          <v-icon color="primary"> mdi-swap-horizontal-bold </v-icon>
        </v-btn>
        <span
          class="ms-3 me-3"
          :class="[isGuestGuide ? 'font-weight-black' : 'font-weight-thin']"
        >
          {{ guestLang }}
        </span>
      </div>
    </div>
    <v-divider></v-divider>
    <div style="overflow: auto; height: 100%">
      <div v-for="n in 10" :key="n">
        <v-sheet
          elevation="1"
          class="d-flex justify-space-between align-center pa-1"
        >
          <span>
            {{ $t(`guide_${n}`) }}
          </span>
          <div class="d-flex">
            <v-divider vertical class="mx-2"></v-divider>
            <v-btn icon @click="speech($t(`guide_${n}`))">
              <v-icon color="primary">mdi-bullhorn-outline</v-icon>
            </v-btn>
          </div>
        </v-sheet>
        <v-divider></v-divider>
      </div>
    </div>
    <div>
      <Trans />
    </div>
  </div>
</template>
<script>
import isMobile from "@/mixin/isMobile.js";
import LanguageMixin from "@/mixin/LanguageMixin.js";
import Trans from "./Guide/Trans.vue";

// NOTE: TTS API 문서
// https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesisUtterance
export default {
  name: "Guide",
  mixins: [isMobile, LanguageMixin],
  data() {
    return {
      n: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      isLoading: true,
      selectedVoice: 1,
      synth: window.speechSynthesis,
      Speech: new window.SpeechSynthesisUtterance(),
    };
  },
  computed: {
    locale() {
      return this.$i18n.locale;
    },
    voice() {
      return this.$t("voicelang");
    },
  },
  props: {
    height: {
      type: String,
    },
  },
  methods: {
    // NOTE: 가이드 언어 상태 변경 - 호스트 <-> 게스트
    changeGuideLocale() {
      this.$store.dispatch("roomStore/changeGuideLocale").then(() => {
        this.setGuideLocale();
      });
    },

    changeLocale() {
      if (this.isHost) {
        this.isHost = false;
        this.isGuest = true;
        this.locale = this.guestLang;
      } else {
        this.isHost = true;
        this.isGuest = false;
        this.locale = this.hostLang;
      }
    },

    speech(value) {
      console.log(value, this.voice);
      // it should be 'craic', but it doesn't sound right
      // console.log(value);
      // 읽어줄 문장
      this.Speech.text = value;
      //읽어줄 언어 (문장 언어 안맞으면 이상해요)
      this.Speech.lang = this.voice;
      this.synth.speak(this.Speech);
    },
  },
  created() {
    this.setGuideLocale();
  },
  components: {
    Trans,
  },
};
</script>

<style>
</style>