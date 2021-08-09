<template>
  <div>
    <v-card class="mx-auto" outlined>
      <v-list-item three-line>
        <v-list-item-content>
          <!-- NOTE: 만났던 사람들 표시
            프로필 사진 + 닉네임 만 보여주면 될거같음
           -->
          <!-- <v-img :src="item.image"> </v-img> -->
          <v-img src="@/assets/image/flag/en.png" width="45px" height="45px"></v-img>
          <v-list-item-title class="text-h5 mb-1">
            {{ this.item.nickname }}
          </v-list-item-title>
          
        </v-list-item-content>
      </v-list-item>
      <v-card-actions>
        <v-btn outlined rounded text @click.prevent="goProfile">{{$t('profile_go')}} </v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>
<script>
export default {
  name: "item",
  data() {
    return {
    };
  },
  computed: {
    width() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
          return "90vw";
        case "sm":
          return "90vw";
        default:
          return "30vw";
      }
    },
  },
  props: {
    item: {
      type: Object,
    },
  },
  methods: {
   goProfile(){
       console.log(this.item)
        this.$router.push({
        name: "Profile",
        params: { userId: this.item.userid },
      });
      this.$router.go();
   }
  },
  mounted() {},
};
</script>
<style>

</style>