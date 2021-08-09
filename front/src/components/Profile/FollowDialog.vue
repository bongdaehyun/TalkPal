<template>
  <v-card>
    <v-card-title>{{ head }} </v-card-title>
    <v-divider></v-divider>
    <v-card-text style="height: 50vh; overflow-y: scroll">
      <div v-for="item in followItem.items" :key="item.id">
        <Item :item="item" />
      </div>
      <infinite-loading
        @infinite="requestItems"
        :identifier="infiniteId"
        ref="infiniteLoading"
        spinner="waveDots"
      >
        <span slot="no-more"></span>
      </infinite-loading>
    </v-card-text>
    <v-card-actions>
      <v-btn color="blue darken-1" text @click="closeDialog"> Close </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import InfiniteLoading from "vue-infinite-loading";
import Item from "@/components/Profile/FollowDialog/Item.vue";

export default {
  name: "FollowDialog",
  data() {
    return {
      infiniteId: +new Date(),
    };
  },
  props: {
    head: {
      type: String,
    },
    profileId: {
      type: Number,
    },
    followItem: {
      type: Object,
    },
  },
  methods: {
    requestItems($state) {
      let url = "userStore/" + this.followItem.url;
      this.$store
        .dispatch(url, {
          userId: this.profileId,
          page: this.followItem.page + 1,
        })
        .then((res) => {
          if (res.data.followList.length == 0) {
            $state.complete();
          } else {
            this.followItem.items.push(...res.data.followList);
            this.followItem.page += 1;
            $state.loaded();
          }
        })
        .catch((err) => {
          $state.complete();
        });
    },
    closeDialog() {
      this.followItem.dialog = false;
    },
  },

  components: {
    Item,
    InfiniteLoading,
  },
};
</script>