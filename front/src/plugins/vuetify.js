import Vue from "vue";
import Vuetify from "vuetify/lib/framework";

Vue.use(Vuetify);

const vuetify = new Vuetify({
  theme: {
    themes: {
      light: {
        primary: '#0288d1',
        secondary: '#553C80',
        accent: '#60CC75',
        error: '#FF0000',
      },
    },
  },
})

export default new Vuetify({});
