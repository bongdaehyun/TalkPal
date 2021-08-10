<template>
  <div
    class="d-flex flex-column"
    style="background-color: red"
    :style="{ height: height }"
  >
    <div id="range" class="demo">
      <select v-model="locale">
        <option>en</option>
        <option>ko</option>
      </select>
      <p v-for="n in 10" :key="n" @click="speech($t(`guide_${n}`))">
        {{ $t(`guide_${n}`) }}
      </p>
    </div>

    <!--<p @click="speech($t('guide_1'))">{{ $t("guide_1") }}</p> !-->
  </div>
</template>

<script>
export default {
  name: "Guide",
  data() {
    return {
      locale: this.$i18n.locale,
      n: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      isLoading: true,
      selectedVoice: 1,
      synth: window.speechSynthesis,
      voice: this.$t("voicelang"),
      //TTS API 문서
      //https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesisUtterance
      Speech: new window.SpeechSynthesisUtterance(),
    };
  },
  watch: {
    locale(val) {
      this.$i18n.locale = val;
      this.voice = this.$t("voicelang");
    },
  },
  props: {
    height: {
      type: String,
    },
  },
  mounted() {},

  methods: {
    speech(value) {
      // it should be 'craic', but it doesn't sound right
      console.log(value);
      // 읽어줄 문장
      this.Speech.text = value;
      //읽어줄 언어 (문장 언어 안맞으면 이상해요)
      this.Speech.lang = this.voice;

      this.synth.speak(this.Speech);
    },
  },
};
</script>

<style>
</style>