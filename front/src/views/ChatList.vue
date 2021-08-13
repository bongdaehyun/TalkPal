<template>
    <v-container class="pa-0">
        <div class="d-flex flex-column my-12">
            <v-row justify="center" no-gutters>
                <v-sheet
                    color="white"
                    elevation="1"
                    height="500"
                    width="900">
                <v-row>
                    <v-col cols="4">
                        <UserList/>
                    </v-col>
                     <v-col cols="8">
                       
                        <ChatRoom/>
                    </v-col>
                </v-row>
                </v-sheet>
            </v-row>
        </div>
    </v-container>
</template>

<script>
import UserList from '@/components/ChatList/UserList';
import ChatRoom from '@/components/ChatList/ChatRoom';

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
        // TODO: 최근 메세지 순으로 정렬.. ChatRoom에 마지막 메세지 시간 속성 추가 必
        // TODO: 읽지 않은 채팅방 표시.. ............ 이것도 ChatRoom 속성..?
    },
    components:{
        UserList,
        ChatRoom,
    }
}
</script>

<style scoped>

</style>