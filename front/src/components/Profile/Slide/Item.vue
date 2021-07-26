<template>
  <div>
    <v-card class="mx-auto" outlined>
      <v-list-item three-line>
        <v-list-item-content>
          <div class="text-overline mb-4">
            <v-rating
              v-model="rating"
              background-color="purple "
              color="red"
              :readonly="true"
              small
            >
            </v-rating>
          </div>
          <v-list-item-title class="text-h5 mb-1" v-if="category === `receive`">
            {{ this.item.from_user_nickname }}
          </v-list-item-title>
          <v-list-item-title class="text-h5 mb-1" v-if="category === `give`">
            {{ this.item.to_user_nickname }}
          </v-list-item-title>
          <v-list-item-subtitle class="cursor" @click="dialog = true">
            {{ this.item.content }}
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-avatar tile size="80" color="grey"></v-list-item-avatar>
      </v-list-item>
      <v-card-actions>
        <v-btn outlined rounded text> GO </v-btn>
      </v-card-actions>
    </v-card>
    <!-- NOTE: Desktop/Mobile width control -->
    <v-dialog v-model="dialog" :width="width">
      <v-card>
        <v-card-title class="subtitle-1 lighten-2">
          {{ this.item.content }}
        </v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="dialog = false"> 닫기 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
export default {
  name: "item",
  data() {
    return {
      rating: this.item.score,
      dialog: false,
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
    category: {
      type: String,
    },
  },
  methods: {
    openDialog() {
      console.log("openDialog");
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