<template>
  <v-card>
    <v-card-title>유저추가</v-card-title>
    <v-divider></v-divider>
    <v-card-text style="height: 50vh; overflow-y: scroll">
      <div v-for="(item, index) in Item.list" :key="index">
        <Item :item="item" />
      </div>
      <infinite-loading
        @infinite="requestItems"
        ref="infiniteLoading"
        spinner="waveDots"
      >
        <span slot="no-more"></span>
      </infinite-loading>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" text @click="closeDialog"> Close </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import InfiniteLoading from "vue-infinite-loading";
import Item from "@/components/Chat/UserList/UserAddDialog/Item.vue";

export default {
  name: "UserListDialog",
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
    closeDialog() {
      this.$store.dispatch("chatStore/closeDialog");
    },
    requestItems($state) {
      let url = "userStore/" + this.Item.url;
      const param = {
        userId: this.profileId,
        page: this.Item.page + 1,
      };
      this.$store
        .dispatch(url, param)
        .then((res) => {
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
  },

  components: {
    Item,
    InfiniteLoading,
  },
};
</script>