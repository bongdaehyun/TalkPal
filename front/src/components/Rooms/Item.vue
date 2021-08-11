<template>
  <v-sheet elevation="5" shaped class="d-flex flex-column pa-3">
    <v-img
      :src="getTopicPath"
      class="d-flex"
      max-height="200px"
      contain
    ></v-img>
    <div class="d-flex justify-space-between px-6 mb-3">
      <span class="font-weight-black">{{ item.name }}</span>
      <span
        :class="[isHover ? 'primary--text font-weight-black' : 'grey--text']"
        >{{ $t(item.topic) }}</span
      >
    </div>
    <div class="px-6">
      <v-avatar size="32">
        <v-img :src="getHostPlagPath"> </v-img>
      </v-avatar>
      <span class="font-weight-black ms-2">{{ $t("item_host") }}</span>
    </div>
    <div class="px-6 mt-3">
      <v-avatar size="32">
        <v-img :src="getGuestPlagPath"> </v-img>
      </v-avatar>
      <span class="font-weight-black ms-2">{{ $t("item_guest") }}</span>
    </div>
    <div class="ml-auto px-6">
      <v-btn
        :class="[isHover ? 'hover-btn-size' : '']"
        @mouseover="overBtn"
        @mouseleave="leaveBtn"
        @click="goRoom"
        :color="btnColor"
        x-large
        icon
      >
        <v-icon x-large :class="[isHover ? 'hover-btn-size' : '']">
          {{ btnIcon }}
        </v-icon>
      </v-btn>
    </div>
  </v-sheet>
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
      isHover: false,
      btnIcon: "mdi-door-closed",
      btnColor: "grey",
    };
  },
  props: {
    item: {
      type: Object,
    },
  },
  computed: {
    getTopicPath() {
      return require(`@/assets/image/topic/${this.item.topic}.jpg`);
    },
    getHostPlagPath() {
      return require(`@/assets/image/flag/${this.item.host_lang}.png`);
    },
    getGuestPlagPath() {
      return require(`@/assets/image/flag/${this.item.guest_lang}.png`);
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
    overBtn() {
      this.isHover = true;
      this.btnIcon = "mdi-door-open";
      this.btnColor = "primary";
    },
    leaveBtn() {
      this.isHover = false;
      this.btnIcon = "mdi-door-closed";
      this.btnColor = "grey";
    },
  },

  components: {},
};
</script>


<style lang="scss" scoped>
.hover-btn-size {
  font-size: 80px !important;
}
</style>