
<template>
  <v-row>
    <v-col>
      <p>{{ item.user_nickname }}</p>
    </v-col>
  </v-row>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
    },
  },
  data() {
    return {};
  },
  created() {},

  methods: {},
};
</script>
<style>
</style>