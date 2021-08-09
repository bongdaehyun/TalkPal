<template>
  <v-card>
    <v-card-title>{{ head }}</v-card-title>
    <v-divider></v-divider>
    <v-card-text style="height: 50vh; overflow-y: scroll">
      <div v-for="item in Item.list" :key="item.id">
        <Item :item="item" />
      </div>
      <infinite-loading
        v-if="Item.url != 'requestHistories'"
        @infinite="requestItems"
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
import Item from "@/components/Profile/UserListDialog/Item.vue";

export default {
  name: "FollowDialog",
  props: {
    head: {
      type: String,
    },
    profileId: {
      type: Number,
    },
    Item: {
      type: Object,
    },
  },
  methods: {
    requestItems($state) {
      let url = "userStore/" + this.Item.url;
      const param = {
        userId: this.profileId,
        page: this.Item.page + 1,
      };
      this.$store
        .dispatch(url, param)
        .then((res) => {
          console.log(res.data.userList);
          if (res.data.userList.length == 0) {
            $state.complete();
          } else {
            this.Item.list.push(...res.data.userList);
            this.Item.page += 1;
            $state.loaded();
          }
        })
        .catch((err) => {
          $state.complete();
        });
    },
    requestHistories() {
      let url = "userStore/" + this.Item.url;
      const param = {
        userId: this.profileId,
      };
      this.$store.dispatch(url, param).then((res) => {
        this.Item.list = res.data.userList;
        console.log(res.data.userList);
      });
    },
    closeDialog() {
      this.Item.dialog = false;
    },
  },
  created() {
    if (this.Item.url == "requestHistories") {
      this.requestHistories();
    }
  },

  components: {
    Item,
    InfiniteLoading,
  },
};
</script>