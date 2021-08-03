import axios from "axios";

// axios 객체 생성
export default axios.create({
  // baseURL: "/api/v1",
  baseURL: process.env.VUE_APP_SERVER_URL,
  headers: {
    "Content-type": "application/json"
  }
});
