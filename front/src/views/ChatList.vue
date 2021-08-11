<template>
    <div></div>
</template>

<script>
export default {
    data() {
        return {
            ws: null,
            socketUrl: process.env.VUE_APP_SOCKET_URL,
            userId: this.$store.getters["userStore/getUserId"],
            chatRooms: [],
        }
    },

    methods: {
        connect() {
            this.ws = new WebSocket(this.socketUrl);
            
            this.ws.onopen = () => {

            }

            this.ws.onmessage = (message) => {
                let parsedMessage = JSON.parse(message.data);
                console.log("[parsedMessage]");
                console.log(parsedMessage);
                switch (parsedMessage.id) {

                    default:
                }
            }
        }
    },

    created() {
        this.connect();
        // TODO: 채팅방 목록 불러오기
        // TODO: 최근 메세지 순으로 정렬.. ChatRoom에 속성 추가 必
        // TODO: 읽지 않은 채팅방 표시.. ............
    }
}
</script>

<style scoped>

</style>