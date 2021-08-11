<template>
  <div>
    <v-card class="mx-auto" outlined>
      <v-card-title class="black--text">
        <v-row>
          <v-col cols="2">
             <v-avatar size="56">
              <v-img :src="profilePath"></v-img>
            </v-avatar>
          </v-col>
          <v-col cols="3">
            <v-rating
                v-model="rating"
                background-color="primary"
                :readonly="true"
                color="primary"
                small
            >
            </v-rating>
            <h5>
               {{ profileNickname }}
            </h5>
          </v-col>
          <v-col cols="6">
              
          </v-col>
        </v-row>
      </v-card-title>
      <v-card-text>
        <div>
          <h3>{{ item.content }}</h3>
        </div>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import isMobile from "@/mixin/isMobile.js";
import getProfilePath from "@/mixin/getProfilePath.js";

export default {
  name: "item",
  mixins: [isMobile, getProfilePath],

  data() {
    return {
      rating: this.item.score,
      dialog: false,
    };
  },
  props: {
    item: {
      type: Object,
    },
    category: {
      type: String,
    },
  },
  computed: {
    profilePath: {
      get() {
        if (this.category == "receive") {
          return this.getProfilePath(this.item.from_user_profile);
        } else {
          return this.getProfilePath(this.item.to_user_profile);
        }
      },
      set() {},
    },
    profileNickname: {
      get() {
        if (this.category == "receive") {
          return this.item.from_user_nickname;
        } else {
          return this.item.to_user_nickname;
        }
      },
      set() {},
    },
  },

  methods: {
    openDialog() {
      //console.log("openDialog");
    },
    goProfile() {
      //console.log(this.item);
      this.$router.push({
        name: "Profile",
        params: { userId: this.item.from_user_id },
      });
      this.$router.go();
    },
  },
  mounted() {
    console.log(this.item)
  },
};
</script>
<style>
.cursor {
  cursor: pointer;
}
.v-rating > .v-icon {
  padding: 0px !important;
}
h3{
  width: 200px;
  word-break:break-all;
  white-space:normal;
}
</style>