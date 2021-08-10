const getProfilePath = {
  methods: {
    getProfilePath(imgPath) {
      const isProd = process.env.VUE_APP_PROD
      try {
        if (isProd === 'true') {
          return `/file/${imgPath}`;
        }
        else {
          return require(`@/assets/image/profile/${imgPath}`);
        }
      }
      catch {
        if (isProd === 'true') {
          return `/file/default_profileImg`;
        }
        else {
          return require(`@/assets/image/profile/default_profileImg.png`);
        }
      }
    },
  },
}
export default getProfilePath
