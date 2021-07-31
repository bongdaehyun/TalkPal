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
            v-model="input.category"
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
            v-model="input.text"
            clearable
            clear-icon="mdi-close-circle"
            @keyup.enter="search"
          ></v-text-field>
        </v-col>
    </v-row>
</template>

<script>
import i18n from "@/i18n.js";

export default {
    data(){
        return{
            input:{
              category:"",
              text:"",
            },
            searchItem: [
               "search_topic",
               "search_name",
               'search_lang'
            ],

        };
    },
    methods:{
        search(){
          //상위에 데이터 전달
          let changeData={
            category:this.input.category.name,
            text:this.input.text
          }
          console.log('데이터 전달')
          console.log(changeData)
          this.$emit('setSearchData',changeData)
        },
        showparent(){
          console.log(this.searchData)
        }
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