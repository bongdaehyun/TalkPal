import fs from 'fs';

const getProfilePath = {
  methods: {
    getProfilePath(imgPath) {
      const isProd = process.env.VUE_APP_PROD
      if (isProd === 'true') {
        try {
          const profilePath = `/file/${imgPath}`
          console.log(profilePath)
          console.log(fs.existsSync(profilePath))

          return profilePath
        }
        catch {
          const profilePath = `/file/default_profileImg.png`
          console.log(profilePath)
          console.log(fs.existsSync(profilePath))

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
