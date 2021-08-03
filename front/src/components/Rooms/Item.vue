<template>
  <v-card class="mx-auto" max-width="400">
    <v-img
      class="white--text align-end"
      height="200px"
      src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
    >
      <v-card-title>{{ item.name }}</v-card-title>
    </v-img>
    <v-card-subtitle class="pb-0"> {{ $t(item.topic) }} </v-card-subtitle>
    <v-card-text class="text--primary">
      <div>{{ item.host_lang }}</div>
      <div>{{ item.guest_lang }}</div>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="orange" @click="goRoom" text>
        {{ $t("room_enter") }}
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "item",
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
  },
};
</script>

<style>
</style>