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
                  :items="langItems"
                  :label="$t('create_host')"
                  item-text="name"
                  item-value="value"
                  v-model="inputs.host_lang"
                ></v-select>
              </v-col>
              <v-col class="d-flex" cols="12">
                <v-select
                  :items="langItems"
                  :label="$t('create_guest')"
                  item-text="name"
                  item-value="value"
                  v-model="inputs.guest_lang"
                ></v-select>
              </v-col>
              <v-col class="d-flex" cols="12">
                <v-select
                  :items="topicItems | topicsi18n"
                  :label="$t('create_topic')"
                  item-text="name"
                  item-value="value"
                  v-model="inputs.topic"
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
          <v-btn color="blue darken-1" text @click="reqeustCreateRoom">
            {{ $t("create_save") }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import i18n from "@/i18n.js";
import langItems from "@/assets/data/lang.json";
import topicItems from "@/assets/data/topic.json";

export default {
  name: "Create",
  data() {
    return {
      dialog: false,
      names: ["누구누구의 방"],
      langItems: langItems,
      topicItems: [
        "create_music",
        "create_movie",
        "create_game",
        "create_food",
        "create_free",
      ],
      inputs: {
        name: "",
        topic: "",
        hostId: "",
        host_lang: "",
        guest_lang: "",
        maxnum: 2,
        curnum: 0,
      },
      socketUrl: process.env.VUE_APP_SOCKET_URL,
    };
  },
  props: {
    ws: {
      type: WebSocket,
    },
  },
  methods: {
    reqeustCreateRoom() {
      this.dialog = false;
      let hostId = this.$store.getters["userStore/getUserId"];
      this.inputs.hostId = hostId;

      console.log(this.inputs);
      this.$store
        .dispatch("roomStore/requestCreate", this.inputs)
        .then((res) => {
          const uuid = res.data.uuid;
          this.$emit("onCreateRoom", uuid);
        });
    },
  },
  filters: {
    topicsi18n(items) {
      return items.map((item) => {
        return {
          name: i18n.t(item),
          value: item,
        };
      });
    },
  },
};
</script>

<style>
</style>