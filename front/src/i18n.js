import Vue from 'vue'
import VueI18n from 'vue-i18n'
import en from '@/locales/en.json'
import ko from '@/locales/ko.json'
import jp from '@/locales/jp.json'


Vue.use(VueI18n)


export default new VueI18n({
  locale: 'ko',
  fallbackLocale: 'ko',
  messages: { en, ko , jp }
})