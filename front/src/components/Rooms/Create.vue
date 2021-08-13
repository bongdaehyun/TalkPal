<template>
  <v-dialog v-model="dialog" persistent :max-width="maxWidth">
    <template v-slot:activator="{ on, attrs }">
      <div class="mt-6" v-if="!isMobile">
        <v-btn width="100%" color="primary" v-bind="attrs" v-on="on" tile>
          <span class="caption">
            {{ $t("room_create") }}
          </span>
        </v-btn>
      </div>
    </template>
    <v-sheet tile class="d-flex flex-column pa-6">
      <v-text-field
        :label="$t('create_name')"
        v-model="inputs.name"
      ></v-text-field>
      <v-select
        :items="langItems"
        :label="$t('create_guest')"
        item-text="name"
        item-value="value"
        v-model="inputs.guest_lang"
      ></v-select>
      <v-select
        :items="topicItems | topicsi18n"
        :label="$t('create_topic')"
        item-text="name"
        item-value="value"
        v-model="inputs.topic"
      ></v-select>
      <v-spacer></v-spacer>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" tile @click="reqeustCreateRoom">
          {{ $t("create_save") }}
        </v-btn>
        <v-btn tile color="#F8F9FA" @click="dialog = false">
          {{ $t("create_close") }}
        </v-btn>
      </v-card-actions>
    </v-sheet>
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
    };
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
      console.log(this.inputs);
      this.$store
        .dispatch("roomStore/requestCreate", this.inputs)
        .then((res) => {
          console.log(res);
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