<template>
 
      <v-card>
        <v-card-title>Select Country</v-card-title>
        <v-divider></v-divider>
        <v-card-text style="height: 300px;">
         <div v-for="follow in follows" :key="follow.id" lg="4" md="3" sm="2" xs="1">
            <Content :item="follow"/>
          </div>
          <infinite-loading @infinite="getFollow"></infinite-loading>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-btn
            color="blue darken-1"
            text
            @click="dialog = false"
          >
            Close
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="dialog = false"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    
</template>

<script>
import InfiniteLoading from "vue-infinite-loading";
import Content from "@/components/Profile/Follow/followitems.vue";

  export default {
    props: {
      follow: {
        type: String,
      },
    },
    data () {
      return {
        dialog: false,
        follows:[],
        userid : this.$route.params.userId,
        pageno : 1,
      }
    },
    created() {
      console.log("실행",this.follow)
      console.log( this.$route.params.userId)
      let url = "userStore/listFollower"
      let followInfo ={
        userid : this.userid,
        pageno : this.pageno
        }
        this.$store.dispatch(url,followInfo)
        .then((res) => {
          this.follows = res.data.followList;
        })
    },
    methods:{
      getFollow($state){
           let url = "userStore/listFollower"
          let followInfo ={
            userid : this.userid,
            pageno : this.pageno
          }
        this.$store.dispatch(url,followInfo)
        .then((res) => {
          console.log("ㅇㅇㅇ",res.data.followList)
          const data = res.data.followList;
          if (data.length) {
            this.follows.push(...data);
            this.pageno += 1;
            $state.loaded();
          } else {
            $state.complete();
          }
        })
      }
    },
    components: {
      Content,
      InfiniteLoading,
  },
  }
</script>