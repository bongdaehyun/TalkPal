<template>
  <v-card elevation="1" outlined>
    <v-img
      :aspect-ratio="16 / 9"
      class="white--text align-end"
      height="200px"
      src="@/assets/image/food.png"
    >
    </v-img>
    <v-card-subtitle class="pb-0"> {{ $t(item.topic) }} </v-card-subtitle>
    <v-card-text class="text--primary">
      <div>{{ item.host_lang }}</div>
      <div>{{ item.guest_lang }}</div>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="primary" @click="goRoom" text>
        {{ $t("room_enter") }}
      </v-btn>
    </v-card-actions>
  </v-card>

</template>

<script>
import getProfilePath from "@/mixin/getProfilePath";
export default {
  name: "item",
  mixins: [getProfilePath],
  data() {
    return {
      name: "",
      userInfo: null,
      profilePath: null,
      expend:false,
    };
  },
  props: {
    item: {
      type: Object,
    },
  },
  methods: {
    goRoom() {
      this.$emit("onEnterRoom", this.item);
      // this.$router.push({ name: "Room", params: { UUID: this.item.uuid } });
    },
    goProfile() {
      this.$router.push({
        name: "Profile",
        params: { userId: this.item.hostId },
      });
    },
  },
  mounted() {},
  components: {},
};
</script>

