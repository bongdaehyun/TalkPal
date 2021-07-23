<template>
  <v-row justify="start">
    <v-dialog v-model="dialog" persistent max-width="30%">
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          class="mt-12 mb-12"
          color="primary"
          dark
          v-bind="attrs"
          v-on="on"
          x-large
        >
          {{ $t("room_create") }}
        </v-btn>
      </template>
      <v-card>
        <v-card-text>
          <v-container>
            <v-row align="center">
              <v-col class="d-flex" cols="12">
                <v-select
                  :items="names"
                  :label="$t('create_name')"
                  v-model="inputs.name"
                ></v-select>
              </v-col>
              <v-col class="d-flex" cols="12">
                <v-select
                  :items="hosts"
                  :label="$t('create_host')"
                  v-model="inputs.host"
                ></v-select>
              </v-col>
              <v-col class="d-flex" cols="12">
                <v-select
                  :items="guests"
                  :label="$t('create_guest')"
                  v-model="inputs.guest"
                ></v-select>
              </v-col>
              <v-col class="d-flex" cols="12">
                <v-select
                  :items="subjects | subjectsi18n"
                  :label="$t('create_subject')"
                  v-model="inputs.subject"
                ></v-select>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialog = false">
            {{ $t("create_close") }}
          </v-btn>
          <v-btn color="blue darken-1" text @click="onCreateRoom">
            {{ $t("create_save") }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import i18n from "@/i18n.js";

export default {
  name: "Create",
  data() {
    return {
      dialog: false,
      names: ["누구누구의 방"],
      hosts: ["한국어", "English"],
      guests: ["한국어", "English"],
      subjects: ["create_music", "create_movie"],
      inputs: { name: "", subject: "", host: "", guest: "" },
    };
  },
  methods: {
    onCreateRoom() {
      this.dialog = false;
      console.log(this.inputs);
    },
  },
  filters: {
    subjectsi18n(values) {
      return values.map((value) => i18n.t(value));
    },
  },
};
</script>

<style>
</style>