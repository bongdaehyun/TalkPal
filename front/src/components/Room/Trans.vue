<template>
  <v-container>
    <v-textarea
      filled
      v-model="str"
      name="input-7-4"
      label="Filled textarea"
      :value="str"
    ></v-textarea>
    <div>
      <v-btn @click="ClickChange(str)"></v-btn>
    </div>
  </v-container>
</template>
<script>
export default {
  data() {
    return {
      str: "",
    };
  },

  methods: {
    ClickChange(trans) {
      this.$emit("onTranslate", this.str);
    },
  },
};
</script>

<style>
</style>