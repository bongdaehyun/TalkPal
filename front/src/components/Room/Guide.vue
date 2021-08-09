<template>
  <div
    class="d-flex flex-column"
    style="background-color: red"
    :style="{ height: height }"
  >
    <p>asjdkh</p>
    <p>asjdkh</p>
    <p>asjdkh</p>
    <p>asjdkh</p>
  </div>
</template>

<script>
export default {
  name: "Guide",
  props: {
    height: {
      type: String,
    },
  },
};
</script>

<style>
</style>