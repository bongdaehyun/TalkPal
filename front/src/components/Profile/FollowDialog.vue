<template>
  <v-card>
    <v-card-title>Select Country</v-card-title>
    <v-divider></v-divider>
    <v-card-text style="height: 300px">
      <div
        v-for="item in follows.items"
        :key="item.id"
        lg="4"
        md="3"
        sm="2"
        xs="1"
      >
        <Item :item="item" />
      </div>
      <infinite-loading
        @infinite="scrollEnd"
        spinner="waveDots"
      ></infinite-loading>
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
  name: "item",
  props: {
    isDesktop: {
      type: Boolean,
    },
    follows: {
      type: Object,
    },
  },
  data() {
    return {
      page: this.follows.page,
      hasMore: this.follows.isEnd,
      userId: this.$route.params.userId,
      limit: 0,
    };
  },
  methods: {
    scrollEnd($state) {
      this.$log("scroll end");
      this.$log(this.follows.isEnd);
      this.$log(this.follows);
      let url = "userStore/" + this.follows.url;
      this.$store
        .dispatch(url, {
          userId: this.userId,
          page: this.page + 1,
        })
        .then((res) => {
          this.$log(res.data);
          if (res.data.followList.length == 0) {
            $state.complete();
            this.hasMore = false;
          } else {
            this.hasMore = true;
            this.follows.items.push(...res.data.followList);
            this.page += 1;
            $state.loaded();
          }
        })
        .catch((err) => {
          $state.complete();
          console.error(err);
          // this.$log(err);
        });
    },

    closeDialog() {
      this.$emit("onCloseDialog");
    },
  },

  components: {
    Item,
    InfiniteLoading,
  },
};
</script>