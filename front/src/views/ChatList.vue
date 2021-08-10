<template>
    <div></div>
</template>

<script>
export default {
    data() {
        return {
            chatRooms: [],
        }
    },

    methods: {
        connect() {
            this.$store.dispatch("userStore/setWebSocket");
            this.ws = this.$store.getters["userStore/getWebSocket"];
            
            console.log(this.ws);
            this.ws.onmessage = (message) => {
                let parsedMessage = JSON.parse(message.data);
                this.$info(`[parsedMessage] : ${parsedMessage}`);
                switch (parsedMessage.id) {
                default:
                    this.$error("Unrecognized message", parsedMessage);
                }
            }
        }
    },

    created() {
        this.connect();
        // TODO: 채팅방 목록 불러오기
        // TODO: 무한 스크롤?
        // TODO: 최근 메세지 순으로 정렬.. ChatRoom에 속성 추가 必
        // TODO: 읽지 않은 채팅방 표시.. ............
    }
}
</script>

<style scoped>

</style>