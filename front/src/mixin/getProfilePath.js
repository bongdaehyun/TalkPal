const getProfilePath = {
  methods: {
    getProfilePath(imgPath) {
      console.log("현재 경로 : " + window.location.pathname)
      const isProd = process.env.VUE_APP_PROD

      if (isProd === 'true') {
        let files = new Image;
        files.src = `/file/${imgPath}`;

        // NOTE: 파일 존재함
        if (files.complete) {
          const profilePath = `/file/${imgPath}`
          return profilePath
        }
        // NOTE: 파일 없음
        else {
          const profilePath = `/file/default_profileImg.png`
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
