import axios from "axios";

// axios 객체 생성
export default axios.create({
  baseURL: "http://localhost:8080/api/v1",
  headers: {
    "Content-type": "application/json"
  }
});
