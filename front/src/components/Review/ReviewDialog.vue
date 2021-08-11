<template>
    <div>
        <v-dialog v-model="reviewDialog" width="500">
            {{ reviewUserId }} 
            <div>
                <v-btn
                    @mouseover="overBtn($event)"
                    @mouseleave="leaveBtn($event)"
                    @click="setScore($event)"
                    :color="btnColor.btn1"
                    id="1"
                    icon
                >
                    <v-icon x-large>
                        {{ btnIcon }}
                    </v-icon>
                </v-btn>
                <v-btn
                    @mouseover="overBtn($event)"
                    @mouseleave="leaveBtn($event)"
                    @click="setScore($event)"
                    :color="btnColor.btn2"
                    id="2"
                    icon
                >
                    <v-icon x-large>
                        {{ btnIcon }}
                    </v-icon>
                </v-btn>
                <v-btn
                    @mouseover="overBtn($event)"
                    @mouseleave="leaveBtn($event)"
                    @click="setScore($event)"
                    :color="btnColor.btn3"
                    id="3"
                    icon
                >
                    <v-icon x-large>
                        {{ btnIcon }}
                    </v-icon>
                </v-btn>
                <v-btn
                    @mouseover="overBtn($event)"
                    @mouseleave="leaveBtn($event)"
                    @click="setScore($event)"
                    :color="btnColor.btn4"
                    id="4"
                    icon
                >
                    <v-icon x-large>
                        {{ btnIcon }}
                    </v-icon>
                </v-btn>
                <v-btn
                    @mouseover="overBtn($event)"
                    @mouseleave="leaveBtn($event)"
                    @click="setScore($event)"
                    :color="btnColor.btn5"
                    id="5"
                    icon
                >
                    <v-icon x-large>
                        {{ btnIcon }}
                    </v-icon>
                </v-btn>
            </div>
            <v-btn large @click="reviewSubmit(true)">
                SUBMIT
            </v-btn>
            <!-- TODO: 다이얼로그 바깥을 눌러 창이 닫혔을 때 처리 必.. 아예 못닫게 하는 방법? -->
            <v-btn large @click="reviewSubmit(false)">
                CLOSE
            </v-btn>
        </v-dialog>
    </div>
</template>

<script>
export default {
    name: "ReviewDialog",
    data() {
        return {
            score: null,
            btnIcon: "mdi-star",
            btnColor: {
                btn1: "blue",
                btn2: "blue",
                btn3: "blue",
                btn4: "blue",
                btn5: "blue",
            },
            hoverBtnColor: {
                btn1: "gray",
                btn2: "gray",
                btn3: "gray",
                btn4: "gray",
                btn5: "gray",
            },
            selectedBtnColor: {
                btn1: "blue",
                btn2: "blue",
                btn3: "blue",
                btn4: "blue",
                btn5: "blue",
            },
        }
    },
    props: {
        reviewDialog: {
            type: Boolean,
        },
        reviewUserId: {
            type: String,
        },
    },
    methods: {
        reviewSubmit(isReview) {
            this.$emit("onReviewSubmit", {
                isReview: isReview,
                score: this.score,
                reviewUserId: this.reviewUserId,
            });
        },
        overBtn(event) {
            console.log(event.currentTarget.id);
            let tmp = event.currentTarget.id;

            this.btnColor = this.hoverBtnColor;
        },
        leaveBtn(event) {


            this.btnColor = this.selectedBtnColor;
        },
        setScore(event) {
            
            this.selectedBtnColor;
        },
    },
}
</script>

<style scoped>

</style>