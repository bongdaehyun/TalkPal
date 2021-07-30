import Vue from "vue";
import Vuetify from "vuetify/lib/framework";

Vue.use(Vuetify);

const vuetify = new Vuetify({
  theme: {
    themes: {
      light: {
        primary: '#9575CD',
        secondary: '#553C80',
        accent: '#60CC75',
        error: '#CCAD89',
      },
    },
  },
})

export default new Vuetify({});
