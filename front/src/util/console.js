const Console = {
  log(meesage) {
    if (process.env.VUE_APP_CONSOLE === "true") {
      console.log(meesage)
    }
  },
  info(meesage) {
    if (process.env.VUE_APP_CONSOLE === "true") {
      console.info(meesage)
    }
  },
  error(meesage) {
    if (process.env.VUE_APP_CONSOLE === "true") {
      console.error(meesage)
    }
  },
  warn(message) {
    if (process.env.VUE_APP_CONSOLE === "true") {
      console.warn(message)
    }
  },
}
export default Console