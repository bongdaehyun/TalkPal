const LanguageMixin = {
  computed: {
    userLang() {
      return this.$store.getters["userStore/getLocale"];
    },
    isHostGuide() {
      return this.$store.getters["roomStore/getIsHostGuide"];
    },
    isGuestGuide() {
      return this.$store.getters["roomStore/getIsGuestGuide"];
    },
    hostLang() {
      return this.$store.getters["roomStore/getHostLang"];
    },
    guestLang() {
      return this.$store.getters["roomStore/getGuestLang"];
    }
  },
  methods: {
    // NOTE: 리뷰 OR 입장 요청 다이얼로그 OPEN 시 유저 언어로 전환
    // NOTE: 가이드는 호스트 언어로 리셋
    setUserLocale() {
      this.$i18n.locale = this.userLang
      this.$store.dispatch("roomStore/resetGuideLocale")
    },
    // NOTE: 가이드 언어 변경

    setGuideLocale() {
      if (this.isHostGuide) {
        this.$i18n.locale = this.hostLang;
      } else {
        this.$i18n.locale = this.guestLang;
      }
    },
  },
}
export default LanguageMixin
