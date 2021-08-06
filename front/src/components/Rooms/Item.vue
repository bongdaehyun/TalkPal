<template>
  <v-card elevation="1" outlined>
    <v-img
      class="white--text align-end"
      height="200px"
      src="@/assets/image/food.png"
    >
      <v-card-title>
        <span class="white black--text bold">{{ item.name }}</span>
      </v-card-title>
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
  data() {
    return {
      name: "",
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
  },
  mounted() {
    const topic = this.item.topic;
    if (topic == "create_food") {
      this.name = "/src/assets/image/food.jpg";
      console.log(this.name);
    }
  },
};
</script>

<style>
</style>