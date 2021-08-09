import axios from "axios";

axios.defaults.baseURL = process.env.VUE_APP_SERVER_URL;
axios.defaults.headers.post['Content-Type'] = 'application/json';
// axios 객체 생성
export default axios
