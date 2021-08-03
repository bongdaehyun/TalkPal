<template>
  <v-row justify="end">
      <v-col
          cols="6"
          sm="3"
          md="2"
        >
          <v-select
          
          @change="search"
          :items="topicItems | searchi18n"
          item-text="name"
          item-value="value"
          item-color="#a200e0"
          style="color: #a200e0;"
          v-model="input.topic"
          :label="$t('create_topic')"
        ></v-select>
      </v-col>
      <v-col
          cols="6"
          sm="3"
          md="2">

          <v-select
          class="slt"
          @change="search"
          :items="LangItems | searchi18n"
          item-text="name"
          item-value="value"
          v-model="input.lang"
          :label="$t('search_lang')"
        ></v-select>
      </v-col>
    </v-row>
</template>

<script>
import i18n from "@/i18n.js";

export default {
    data(){
        return{
            input:{
              topic:"",
              lang:"",
            },
            LangItems: [
              "search_none",
               "search_ko",
               'search_en'
            ],
            topicItems: [
              "search_none",
              "create_music",
              "create_movie",
              "create_game",
              "create_food",
              "create_free",
            ],
           
        };
    },
    methods:{
        onChange(data){
         
        },
        search(){
          //console.log(this.input)
          //상위컴포넌트에 데이터 전달
          if(this.input.topic=="search_none" )
          {
            this.input.topic=""
          }else if(this.input.lang=="search_none"){
            this.input.lang=""
          }
          let changeData={
            topic:this.input.topic,
            lang:this.input.lang,
          }

          console.log('데이터 전달')
          console.log(changeData)
          this.$emit('setSearchData',changeData)
        },
    },
    filters:{
        searchi18n(items){
             return items.map((item) => {
                return {
                  name: i18n.t(item),
                  value: item,
                };
            });
        },
    }
}
</script>
<style scoped>

</style>