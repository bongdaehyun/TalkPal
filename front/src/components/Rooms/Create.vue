<template>
  <v-dialog v-model="dialog" persistent :max-width="maxWidth">
    <template v-slot:activator="{ on, attrs }">
      <div class="mt-6" v-if="!isMobile">
        <v-btn
          width="100%"
          color="primary"
          outlined
          v-bind="attrs"
          v-on="on"
          tile
        >
          <span>
            {{ $t("room_create") }}
          </span>
        </v-btn>
      </div>
    </template>
    <v-card>
      <v-card-text>
        <v-container>
          <v-row align="center">
            <v-col class="d-flex" cols="12">
              <v-text-field
                :label="$t('create_name')"
                v-model="inputs.name"
              ></v-text-field>
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
</template>

<script>
import i18n from "@/i18n.js";
import langItems from "@/assets/data/lang.json";
import isMobile from "@/mixin/isMobile.js";

export default {
  name: "Create",
  mixins: [isMobile],
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
        host_lang: this.$store.getters["userStore/getLangId"],
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
  computed: {
    maxWidth() {
      if (this.isMobile) {
        return "90%";
      }
      return "30%";
    },
  },
  methods: {
    reqeustCreateRoom() {
      this.dialog = false;
      let hostId = this.$store.getters["userStore/getUserId"];
      this.inputs.hostId = hostId;
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