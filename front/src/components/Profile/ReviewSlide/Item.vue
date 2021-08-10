<template>
  <div>
    <v-card class="mx-auto" outlined>
      <v-list-item three-line>
        <v-list-item-avatar tile size="64">
          <v-img :src="profilePath"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title class="text-h5 mb-1">
            {{ profileNickname }}
          </v-list-item-title>
          <div class="text-overline">
            <v-rating
              v-model="rating"
              background-color="primary"
              :readonly="true"
              color="primary"
              small
            >
            </v-rating>
          </div>
          <v-list-item-subtitle class="cursor" @click="dialog = true">
            {{ item.content }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-card-actions>
        <v-btn outlined rounded text @click="goProfile">
          {{ $t("profile_go") }}
        </v-btn>
      </v-card-actions>
    </v-card>
    <!-- NOTE: Desktop/Mobile width control -->
    <v-dialog v-model="dialog">
      <v-card>
        <v-card-title class="subtitle-1 lighten-2">
          {{ item.content }}
        </v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="dialog = false">
            {{ $t("profile_close") }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
      console.log("openDialog");
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
  mounted() {},
};
</script>
<style>
.cursor {
  cursor: pointer;
}
.v-rating > .v-icon {
  padding: 0px !important;
}
</style>