
<template>
<!-- <v-col
    ref="notification-list"
    class="list"
    @scroll="handleNotificationListScroll"
  >
    <li
      v-for="(notification, index) in notifications"
      :key="`${notification.user_nickname}${index}`"
    >
      nick : {{ notification.user_nickname }}
      user_email:  {{ notification.user_id }}
    </li>
  </v-col>  -->
  <v-row>
    <v-col>
      <p>{{item.user_nickname}}</p>
    </v-col>
    <v-btn>
      팔로우 해제
    </v-btn>
  </v-row>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
    },
  },
   data() {
      return {
        userid : this.$route.params.userId,
        pageno : 1,
        notifications: [],
        isMore : true
      }
    },
    created() {
    // console.log( this.$route.params.userId)
    // let url = "userStore/listFollower"
    // let followInfo ={
    //    userid : this.userid,
    //    pageno : this.pageno
    //   }
    //   this.$store.dispatch(url,followInfo)
    //   .then((res) => {
    //     console.log(res.data.followList)
    //      this.notifications = res.data.followList;
    //    })
    },

  methods: {
    // 무한 스크롤 정의
    handleNotificationListScroll(e) {
    
      const { scrollHeight, scrollTop, clientHeight } = e.target;
      const isAtTheBottom = scrollHeight === scrollTop + clientHeight;
      // 일정 한도 밑으로 내려오면 함수 실행
      if (isAtTheBottom){ 
        console.log("스크롤바닥")
        this.handleLoadMore()};
    },

    //
    handleLoadMore() {
      let url = "userStore/listFollower"
      let followInfo ={
       userid : this.userid,
       pageno : this.pageno + 1
      } 
        if(this.isMore){
          this.$store.dispatch(url,followInfo)
          .then((res) => {
            this.notifications =this.notifications.concat(res.data.followList);
            this.pageno += 1
          console.log(this.pageno)
            if(res.data.followList.length ==0)
            {
              this.isMore =false
            }
          })
        }
    }
  }
};
</script>
<style>

</style>