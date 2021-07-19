<template>
  <v-tab-item>
    <v-container>
      <v-row>
        <v-text-field
          label="Email"
          prepend-inner-icon="mdi-email"
          v-model="credentials.email"
          type="email"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password"
          prepend-inner-icon="mdi-lock"
          v-model="credentials.pwd"
          type="password"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password Confirm"
          prepend-inner-icon="mdi-lock"
          v-model="credentials.pwdconfirm"
          type="password"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="NickName"
          prepend-inner-icon="mdi-text"
          v-model="credentials.nickname"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-select 
        v-model="credentials.lang"
        :items="items" 
        item-text="name"
        item-value="value"
        label="Language"></v-select>
      </v-row>
      <v-row class="text-right">
        <v-btn class="ma-2" color="primary" dark @click="register">
          {{ $t("main_register") }}
          <v-icon dark right> mdi-checkbox-marked-circle </v-icon>
        </v-btn>
      </v-row>
    </v-container>
  </v-tab-item>
</template>

<script>
export default {
  data() {
    return {
      credentials: {
        email: "",
        pwd: "",
        pwdconfirm: "",
        nickname: "",
        lang:"",
      },
      items: [ {name : "English",value : "en"},{name: "한국어",value:"ko"}],
    };
  },
  methods:{
    register(){
      this.$store.dispatch('requestSingup', { 
        email: this.credentials.email,
         password: this.credentials.pwd,
         nickname:this.credentials.nickname,
          lang:this.credentials.lang
       })
          .then(function (result) {
            //console.log("회원가입"+result)
            //alert('accessToken: ' + result.data.accessToken)
            alert("회원 가입 성공")
            //요것이 왜안되지????
            this.$router.push({ name: "Main" });
          })
          .catch(function (err) {
            alert(err)
          })
    }
  }
};
</script>

<style>
</style>