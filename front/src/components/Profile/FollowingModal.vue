<template>
 
      <v-card>
        <v-card-title>Select Country</v-card-title>
        <v-divider></v-divider>
        <v-card-text style="height: 300px;">
         
            <Content/>
    
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

import Content from "@/components/Profile/Follow/followitems.vue";
  export default {
    data () {
      return {
        dialogm1: '',
        dialog: false,
        notifications:[],
      }
    },
    created() {
      console.log( this.$route.params.userId)
      let url = "userStore/listFollower"
      let followInfo ={
        userid : this.userid,
        pageno : this.pageno
        }
        this.$store.dispatch(url,followInfo)
        .then((res) => {
          console.log(res.data.followList)
          this.notifications = res.data.followList;
        })
    },
    components: {
    Content
    
  },
  }
</script>