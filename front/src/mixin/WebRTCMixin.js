import kurentoUtils from "kurento-utils";
import _ from "lodash";

const WebRTCMixin = {
  data() {
    return {
      ws: null,
      hostId: null,
      roomId: null,
      userId: this.$store.getters["userStore/getUserId"],
      userNickName: this.$store.getters["userStore/getNickName"],
      socketUrl: process.env.VUE_APP_SOCKET_URL,
      UUID: this.$route.params.UUID,
      participants: [],
      participantComponents: {},
      joinQuestionDialog: false,
      joinAnswer: null,
      timer: null,
    }
  },
  methods: {
    sendMessage(message) {
      if (this.ws.readyState !== this.ws.OPEN) {
        this.$log("[errMessage] Skip, WebSocket session isn't open" + message);
        return;
      }
      const jsonMessage = JSON.stringify(message);
      this.$log("[sendMessage] message: " + jsonMessage);
      this.ws.send(jsonMessage);
    },
    sendError(message) {
      this.$log("[errMessage] " + message);
      this.sendMessage({
        id: "ERROR",
        message: message,
      });
    },
    startVideo(video) {
      video.play();
    },
    questionResponse(response) {
      if (response === true) {
        this.joinAnswer = true;
      } else if (response === false) {
        this.joinAnswer = false;
      }
      this.joinQuestionDialog = false;
    },

    openQuestionDialog() {
      this.joinQuestionDialog = true;
      this.joinAnswer = null;
    },

    onJoinQuestion(request) {
      // NOTE: 다른 유저의 입장 요청 (uuid, requestUserId, hostId)

      // NOTE: 요청 수락/거부 Dialog OPEN
      this.openQuestionDialog();

      // NOTE: 요청 결과 0.1초 마다 확인
      const responseInterval = setInterval(() => {
        if (this.joinAnswer !== null) {
          let message = {
            id: "joinResponse",
            requestUserId: request.requestUserId,
            uuid: request.uuid,
            answer: this.joinAnswer,
          };
          this.sendMessage(message);
          this.joinAnswer = null;
          clearInterval(responseInterval);
          clearInterval(timerInterval);
        }
      }, 100);

      // NOTE: 제한시간(10초) 후 거절 강제 전송
      this.timer = 10;
      const timerInterval = setInterval(() => {
        this.timer = this.timer - 1;
        if (this.timer === 0) {
          let message = {
            id: "joinResponse",
            requestUserId: request.requestUserId,
            uuid: request.uuid,
            answer: false,
          };
          this.sendMessage(message);
          this.joinQuestionDialog = false;
          this.joinAnswer = null;
          clearInterval(timerInterval);
          clearInterval(responseInterval);
        }
      }, 1000);
    },
    onNewParticipant(request) {
      // NOTE: 새로 들어온 유저 만난 사람들에 추가
      this.$store
        .dispatch("userStore/addUserHistorie", {
          fromid: this.userId,
          toid: request.userId,
        })
        .then((res) => {
          this.$log(res);
        });
      this.receiveVideo(request.userId);
    },
    onParticipantLeft(request) {
      this.$log("Participant " + request.userId + " left");

      const index = _.findIndex(this.participants, (participant) => {
        return participant.userId == request.userId;
      });

      this.participants.splice(index, 1);

      let participantComponent = this.participantComponents[request.userId];
      participantComponent.dispose();

      delete this.participantComponents[request.userId];
    },
    receiveVideoResponse(result) {
      this.$log("receiveVideoResponse");
      this.$log(this.participantComponents[result.userId]);

      this.participantComponents[result.userId].rtcPeer.processAnswer(
        result.sdpAnswer,
        (error) => {
          if (error) return this.$error(error);
        }
      );
    },
    callResponse(message) {
      if (message.response != "accepted") {
        this.$log("Call not accepted by peer. Closing call");
        stop();
      } else {
        this.webRtcPeer.processAnswer(message.sdpAnswer, (error) => {
          if (error) return this.$error(error);
        });
      }
    },
    async receiveVideo(senderId) {
      this.$log(senderId);
      let res = await this.$store.dispatch(
        "userStore/requestUserInfo",
        senderId
      );

      const userInfo = { userId: Number(senderId), ...res.data };
      this.participants.push(userInfo);

      this.$nextTick(() => {
        let participant = this.$refs[`participant-${senderId}`][0];
        this.participantComponents[senderId] = participant;
        let video = participant.getVideoElement();
        let options = {
          remoteVideo: video,
          onicecandidate: participant.onIceCandidate.bind(participant),
        };

        participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(
          options,
          function (error) {
            if (error) {
              return this.$error(error);
            }
            this.generateOffer(
              participant.offerToReceiveVideo.bind(participant)
            );
          }
        );
      });
    },
    async onExistingParticipants(msg) {
      const userIds = msg.data;
      for (let toId of userIds) {
        // NOTE: 기존 방에 있던 유저 만난 사람들에 추가
        this.$store
          .dispatch("userStore/addUserHistorie", {
            fromid: this.userId,
            toid: toId,
          })
          .then((res) => {
            this.$log(res);
          });
      }

      const constraints = {
        audio: true,
        video: {
          mandatory: {
            maxFrameRate: 15,
            minFrameRate: 15,
          },
        },
      };
      this.$log(this.userId + " registered in room " + this.UUID);
      let res = await this.$store.dispatch(
        "userStore/requestUserInfo",
        this.userId
      );
      const userInfo = { userId: this.userId, ...res.data };
      // NOTE: 참가자 추가
      this.participants.push(userInfo);

      // NOTE: this.$nextTick - UI 작업이 끝나고 다음 작업을 실행함.
      this.$nextTick(() => {
        // NOTE: participant 컴포넌트
        let participant = this.$refs[`participant-${this.userId}`][0];

        //  NOTE: 컴포넌트 추가
        this.participantComponents[this.userId] = participant;

        // NOTE: 비디오 엘리먼트 가져오기
        let video = participant.getVideoElement();

        let options = {
          localVideo: video,
          mediaConstraints: constraints,
          onicecandidate: participant.onIceCandidate.bind(participant),
        };
        participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(
          options,
          function (error) {
            if (error) {
              return this.$error(error);
            }
            this.generateOffer(
              participant.offerToReceiveVideo.bind(participant)
            );
          }
        );
        // NOTE: 신규 참가자에게 기존 참가자들 비디오 표시
        msg.data.forEach(this.receiveVideo);
      });
    },
    join() {
      let payload = {
        uuid: this.UUID,
        num: 1,
      };
      this.$store.dispatch("roomStore/requestAddPerson", payload);

      let message = {
        id: "joinRoom",
        userId: this.userId,
        uuid: this.UUID,
      };
      this.sendMessage(message);
    },
    // TODO: 퇴장 시 퇴장 확인 다이얼로그 출력
    leaveRoom() {
      this.$log("leaveRoom");
      // 1. Host가 나갈 떄 Host => leaveHost 메세지 수신, Guest => leaveGeust 메세지 수신
      // 2. Guest가 혼자 나갈 때 => 메세지 수신 X
      let message = {
        id: "leaveRoom",
        hostId: this.hostId,
      };
      this.sendMessage(message);
      // this.exitRoom();
    },
    leaveHost() {
      this.$log("leaveHost");
      // 방 삭제 API 호출
      this.$store
        .dispatch("roomStore/requestDelete", {
          roomId: this.roomId,
          header: this.$store.getters["userStore/getHeader"],
        })
        .then((res) => {
          console.log(res);
          this.exitRoom();
        });
    },
    leaveGuest() {
      let payload = {
        uuid: this.UUID,
        num: -1,
      };
      this.$store.dispatch("roomStore/requestAddPerson", payload);
      this.$log("leaveGuest");
      // 방 나가기
      this.exitRoom();
    },
    exitRoom() {
      const video = document.querySelector("video");
      const mediaStream = video.srcObject;
      const tracks = mediaStream.getTracks();
      tracks[0].stop();
      tracks.forEach((track) => track.stop());

      this.$store.dispatch("roomStore/exitRoom");
      this.ws.close();
      this.$router.push({ name: "Rooms" });
    },
    submitMessage(inputMessage) {
      if (inputMessage.length === 0) return;
      this.sendMessage({
        id: "sendChat",
        senderId: this.userId,
        senderNickName: this.userNickName,
        sendMsg: inputMessage,
      });
    },
    onRecieveChat(msgInfo) {
      this.$log(msgInfo);
      let msg = {
        sender: msgInfo.senderId,
        nick : msgInfo.senderNickName,
        time: msgInfo.sendTime,
        content: msgInfo.sendMsg,
      };
      this.msgList.push(msg);
    },
    connect() {
      // TODO: ws
      // this.ws = this.$store.getters["userStore/getWS"];
      this.ws = new WebSocket(this.socketUrl);
      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        console.log("[parsedMessage]");
        console.log(parsedMessage);
        // this.$info(`[parsedMessage] : ${parsedMessage}`);
        switch (parsedMessage.id) {
          case "existingParticipants":
            this.onExistingParticipants(parsedMessage);
            break;
          case "newParticipantArrived":
            this.onNewParticipant(parsedMessage);
            break;
          case "participantLeft":
            this.onParticipantLeft(parsedMessage);
            break;
          case "receiveVideoAnswer":
            this.receiveVideoResponse(parsedMessage);
            break;
          // NOTE: Host 가 나갈 때, 방 삭제 API 호출 필요
          case "leaveHost":
            this.leaveHost();
            break;
          // NOTE: Host 가 나갈 때, Guest 강제 퇴실
          // NOTE: Guest 가 혼자 나갈 때
          case "leaveGuest":
            this.leaveGuest();
            break;
          // NOTE: 방 입장 요청들어왔을 때
          case "joinQuestion":
            this.onJoinQuestion(parsedMessage);
            break;
          // NOTE: 채팅 수신
          case "receiveChat":
            this.onRecieveChat(parsedMessage);
            break;
          case "iceCandidate":
            this.participantComponents[
              parsedMessage.userId
            ].rtcPeer.addIceCandidate(parsedMessage.candidate, (error) => {
              if (error) {
                this.$error("Error adding candidate: ");
                this.$error(error);
                return;
              }
            });
            break;
          default:
            this.$error("Unrecognized message");
            this.$error(parsedMessage);
        }
      };
      this.ws.onopen = () => {
        this.$log(this.ws);

        setTimeout(() => {
          this.join();
        }, 500);
      };
    },
  },
}
export default WebRTCMixin