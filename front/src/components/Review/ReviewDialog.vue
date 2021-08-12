<template>
    <div>
        <v-dialog v-model="reviewDialog" width="500" persistent>
            <v-sheet
                elevation="2"
                background-color="white"
                class="d-flex flex-column"
            >
                <div class="mx-auto pt-7">
                    <span class="black--text">
                        방금 만난 상대는 어땠나요?
                    </span>
                </div>
                <div class="mx-auto pa-2">
                    <span class="grey--text">
                        상대방에 대한 평가를 남겨주세요.
                    </span>
                </div>
                <div class="mx-auto">
                    <v-rating
                        background-color="#CFD8DC"
                        color="accent" 
                        empty-icon="mdi-star-outline"
                        full-icon="mdi-star"
                        half-icon="mdi-star-half-full"
                        half-increments
                        hover
                        length="5"
                        :value="score"
                        size="55"
                        @input="setRating"
                    ></v-rating>
                </div>
                    <v-col>
                        <v-textarea
                            solo
                            no-resize
                            v-model="content"
                            :rules="rules"
                            maxlength="255"
                        ></v-textarea>
                    </v-col>
                <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn large color="primary" text @click="reviewSubmit(true)">
                    {{ $t("questiondialog_yes") }}
                </v-btn>
                <v-btn large color="grey" text @click="reviewSubmit(false)">
                    {{ $t("questiondialog_no") }}
                </v-btn>
                </v-card-actions>
            </v-sheet>
        </v-dialog>
    </div>
</template>

<script>
export default {
    name: "ReviewDialog",
    data() {
        return {
            score: 5,
            content: "",
            rules: [v => v.length < 255 || "최대 255글자까지 입력 가능합니다."],
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
        setRating(score) {
            this.score = score;
        },
        reviewSubmit(isReview) {
            this.$emit("onReviewSubmit", {
                isReview: isReview,
                content: this.content,
                score: this.score,
                to_user_id: this.reviewUserId,
            });
        },
    },
}
</script>

<style scoped>

</style>