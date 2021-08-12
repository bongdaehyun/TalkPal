<template>
    <div></div>
</template>

<script>
export default {
    data() {
        return {
            chatRoomId: null,
            msgList: [],
            ws: this.$store.getters["userStore/getWebSocket"],
        }
    },
    methods: {
        submitDirectMsg(msg) {
            // this.$$store
            //     .dispatch("", data)
            //     .then((res) => {
            //         this.$emit("", data2);
            //     });
            
        },
        onReceiveDM(newMsg) {

        },
        connect() {
            this.$store.dispatch("userStore/setWebSocket");
            this.ws = this.$store.getters["userStore/getWebSocket"];
            
            console.log(this.ws);
            this.ws.onmessage = (message) => {
                let parsedMessage = JSON.parse(message.data);
                this.$info(`[parsedMessage] : ${parsedMessage}`);
                switch (parsedMessage.id) {
                case "receiveDM":
                    // NOTE: 새로운 메세지 수신 시 붙여주기
                    this.onReceiveDM(parsedMessage);
                    break;
                default:
                    this.$error("Unrecognized message", parsedMessage);
                }
            }
        }
    },

    created() {
        // TODO: 채팅 내역 불러오기
    }
}
</script>

<style scoped>

</style>