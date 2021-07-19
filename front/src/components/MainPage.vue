<template>
    <v-container>
        <v-row>
            <v-col>
                <v-tabs
                    fixed-tabs
                    background-color="indigo"
                    dark
                    v-model="tab"
                >
                    <v-tab>
                        {{$t('mainpage_login')}}
                    </v-tab>
                    <v-tab>
                        {{$t('mainpage_register')}}
                    </v-tab>
                </v-tabs>
                <v-tabs-items v-model="tab">
                    <v-tab-item>
                        <v-container>
                            <v-row>
                                <v-text-field
                                    label="Email"
                                    prepend-inner-icon="mdi-email"
                                     v-model="login_email"
                                ></v-text-field>
                            </v-row>
                            <v-row>
                                <v-text-field
                                    label="Password"
                                    prepend-inner-icon="mdi-lock"
                                    v-model="login_pwd"
                                ></v-text-field>
                            </v-row>
                            <v-row class="text-right">
                                <v-btn
                                    class="ma-2"
                                    color="primary"
                                    dark
                                    
                                >
                                    로그인
                                    <v-icon
                                    dark
                                    right
                                    >
                                    mdi-checkbox-marked-circle
                                    </v-icon>
                                </v-btn>
                            </v-row>
                        </v-container>
                    </v-tab-item>
                    <v-tab-item>
                        <v-container>
                        <v-row>
                            <v-text-field
                                label="Email"
                                prepend-inner-icon="mdi-email"
                                
                            ></v-text-field>
                        </v-row>
                        <v-row>
                            <v-text-field
                                label="Password"
                                prepend-inner-icon="mdi-lock"
                               
                            ></v-text-field>
                        </v-row>
                        <v-row>
                            <v-text-field
                                label="Password Confirm"
                                prepend-inner-icon="mdi-lock"
                            ></v-text-field>
                        </v-row>
                        <v-row>
                            <v-text-field
                                label="Nick Name"
                                prepend-inner-icon="mdi-text"
                            ></v-text-field>
                        </v-row>
                        <v-row>
                            <v-select
                            :items="items"
                            label="Language"
                            ></v-select>
                        </v-row>
                        <v-row class="text-right">
                            <v-btn
                                class="ma-2"
                                color="primary"
                                dark
                            >
                                회원 가입
                                <v-icon
                                dark
                                right
                                >
                                mdi-checkbox-marked-circle
                                </v-icon>
                            </v-btn>
                        </v-row>
                        </v-container>
                    </v-tab-item>
                </v-tabs-items>
               
            </v-col>
            <v-col>
                <v-img
                lazy-src="https://picsum.photos/id/11/10/6"
                max-height=""
                max-width="500"
                src="https://picsum.photos/id/11/500/300"
                ></v-img>
            </v-col>
        </v-row>
        
    </v-container>
</template>
<script>
export default {
    data(){
        return{
            tab:null,
            email:"",
            login_email:"",
            login_pwd:"",
            items:['English', 'Korean'],
        }
    },
}
</script>
<style>

</style>