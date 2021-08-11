<template>
  <v-row class="tab-wrap" justify="center" align="center">
    <v-col class="col-sm-12 col-md-4">
      <v-card>
        <v-card-title>
          <h1>404</h1>
        </v-card-title>
        <v-card-text>
          <h3>
            {{ $t("error_404") }}
          </h3>
        </v-card-text>
      </v-card>
    </v-col>
    <v-col class="col-sm-12 col-md-4 d-none d-lg-block d-print-block">
      <v-img class="" src="@/assets/image/talking.png"> </v-img>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "ERROR",
};
</script>

<style scoped>
.tab-wrap {
  height: 100vh;
}
</style>