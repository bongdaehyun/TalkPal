<template>
  <v-row justify="end">
      <v-col
        cols="6"
        sm="2"
        md="2.5"
      >
         <v-select
            :items="searchItem | searchi18n"
            :label="$t('search_classification')"
            item-text="name"
            item-value="value"
            v-model="category"
            return-object
            single-line
        ></v-select>
      </v-col>
      <v-col
          cols="12"
          sm="6"
          md="4"
        >
          <v-text-field
            append-icon="mdi-magnify"
            placeholder="Text"
            filled
            rounded
            dense
            v-model="searchText"
            clearable
            clear-icon="mdi-close-circle"
          ></v-text-field>
        </v-col>
    </v-row>
</template>

<script>
import i18n from "@/i18n.js";

export default {
    data(){
        return{
            searchText:"",
            category:"",
            searchItem: [
               "search_topic",
               "search_name",
            ],
        };
    },
    filters:{
        searchi18n(items){
             return items.map((item) => {
                return {
                name: i18n.t(item),
                value: item,
                };
            });
        }
    }
}
</script>