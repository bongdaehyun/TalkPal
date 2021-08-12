<template>
    <div>
        <v-dialog v-model="reviewDialog" width="500" persistent>
            {{ reviewUserId }} 
            <v-rating
                background-color="grey"
                color="blue"
                empty-icon="mdi-star-outline"
                full-icon="mdi-star"
                hover
                length="5"
                :value="score"
                size="65"
                @input="setRating"
            ></v-rating>
            <v-col md="12">
                <v-textarea
                    solo
                    no-resize
                    label="평가내용"
                    v-model="content"
                ></v-textarea>
            </v-col>
            <v-btn large @click="reviewSubmit(true)">
                SUBMIT
            </v-btn>
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
            score: 5,
            content: null,
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