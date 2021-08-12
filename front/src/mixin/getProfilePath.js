const getProfilePath = {
  methods: {
    getProfilePath(imgPath) {
      console.log("현재 경로 : " + window.location.pathname)
      const isProd = process.env.VUE_APP_PROD
      if (isProd === 'true') {
        try {
          const profilePath = `/file/${imgPath}`
          console.log("try : " + profilePath)

          return profilePath
        }
        catch {
          const profilePath = `/file/default_profileImg.png`
          console.log("catch : " + profilePath)

          return profilePath
        }
      }
      else {
        try {
          const profilePath = require(`@/assets/image/profile/${imgPath}`);

          return profilePath
        }
        catch {
          const profilePath = require(`@/assets/image/default_profileImg.png`);

          return profilePath
        }
      }
    },
  },
}
export default getProfilePath
