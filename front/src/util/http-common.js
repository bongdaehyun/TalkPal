import axios from "axios";

// axios 객체 생성
export default axios.create({
  // baseURL: "http://localhost:8080/api/v1",
  // baseURL: "http://192.168.0.2:8080/api/v1",
  // baseURL: "https://localhost:8443/api/v1",
  baseURL: "https://192.168.0.2:8443/api/v1",
  headers: {
    "Content-type": "application/json"
  }
});
