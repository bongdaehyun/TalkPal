
<template>
  <v-row>
    <v-col>
      <div class="mt-3 message-me" @click="goProfile">
        {{ this.item.user_nickname }}
      </div>
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

  methods: {
    goProfile() {
      //console.log(this.item);
      this.$router.push({
        name: "Profile",
        params: { userId: this.item.user_id },
      });
      this.$router.go();
    },
  },
};
</script>
<style scoped>
.message-me {
  border: 1px solid #f7f4f0;
  border-radius: 20px 20px 0 20px;
  background: #f7f4f0;
  float: left;
}
</style>