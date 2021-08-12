const getProfilePath = {
  methods: {
    getProfilePath(imgPath) {
      const isProd = process.env.VUE_APP_PROD

      if (isProd === 'true') {
        // NOTE: 프로필 이미지 없음
        if (imgPath === null) {
          return `/file/default_profileImg.png`
        }
        let files = new Image;
        files.src = `/file/${imgPath}`;

        // NOTE: 파일 없음
        if (!files.complete) {
          return `/file/default_profileImg.png`
        }
        // NOTE: 파일 존재
        else {
          return `/file/${imgPath}`
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
