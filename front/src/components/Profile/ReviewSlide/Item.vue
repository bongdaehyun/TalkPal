<template>
  <div class="pa-3">
    <v-sheet color="white" elevation="5">
      <v-list-item three-line>
        <v-list-item-avatar tile size="64">
          <v-img :src="profilePath"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-rating
            v-model="rating"
            background-color="#CFD8DC"
            :readonly="true"
            color="accent"
          >
          </v-rating>
          <v-list-item-title class="text-h5 mb-1 font-weight-black">
            {{ profileNickname }}
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-card-text>
        <v-row>
          <v-col class="text-body-1" cols="11">
            <p>{{ item.content }}</p>
          </v-col>
        </v-row>
      </v-card-text>
    </v-sheet>
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
    openDialog() {},
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