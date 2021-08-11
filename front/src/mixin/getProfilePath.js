const getProfilePath = {
  methods: {
    getProfilePath(imgPath) {
      const isProd = process.env.VUE_APP_PROD
      try {
        if (isProd === 'true') {
          const profilePath = `/file/${imgPath}`;
          return profilePath
        }
        else {
          const profilePath = require(`@/assets/image/profile/${imgPath}`);

          return profilePath
        }
      }
      catch {
        if (isProd === 'true') {
          const profilePath = `/file/default_profileImg`;

          return profilePath
        }
        else {
          const profilePath = require(`@/assets/image/default_profileImg.png`);

          return profilePath
        }
      }
    },
  },
}
export default getProfilePath
