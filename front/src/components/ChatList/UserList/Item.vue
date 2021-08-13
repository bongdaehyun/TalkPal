<template>
  <v-list dense>
    <v-list-item-group color="primary">
      <v-list-item>
        <v-list-item-icon>
          <v-icon v-text="item.icon"></v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title v-text="item.text"></v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list-item-group>
  </v-list>
</template>

<script>
export default {
  name: "item",
  props: {
    item: {
      type: Object,
    },
  },
};
</script>

<style>
</style>