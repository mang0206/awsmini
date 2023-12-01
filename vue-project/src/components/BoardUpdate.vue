<template>
    <div class="container mt-5">
        <form @submit.prevent="updateBoard">
            <label for="title">제목:</label>
            <input v-model="title" type="text" class="form-control" required>

            <label for="content">내용:</label>
            <textarea v-model="content" class="form-control" rows="5" required></textarea>

            <button type="submit" class="btn btn-primary">수정 완료</button>
        </form>
        <div v-if="error" class="alert alert-danger mt-3">
            {{ error }}
        </div>
    </div>
</template>
  
<script>
import axios from 'axios';
import { useBoardListStore } from "@/stores/boardliststore.js";

export default {
    computed: {
        currentBoard() {
            // Vuex Store에서 현재 게시글의 상세 정보를 가져옵니다.
            return useBoardListStore().currentBoard;
        },
    },
    data() {
        return {
            title: '',
            content: '',
            error: null
        };
    },
    methods: {
        async updateBoard() {
            const updateData = {
                title: this.title,
                content: this.content
            };

            try {
                const boardId = this.$route.params.id;
                await axios.put(`http://localhost:8090/boards/update/${boardId}`, updateData, {
                    headers: {
                        'Authorization': sessionStorage.getItem("token")
                    }
                });

                this.$router.push(`/boards/type/${this.currentBoard.type}`);
            } catch (error) {
                console.error('게시글 수정 오류:', error);
                this.error = '게시글 수정 중 오류가 발생했습니다.';
            }
        },
        //기존 글 정보 가져오기. 
        async fetchBoardDetail() {
            const boardListStore = useBoardListStore();
            await boardListStore.fetchBoardDetail(this.$route.params.idx);

            this.title = this.currentBoard.title;
            this.content = this.currentBoard.content;
        },
    },
    async mounted() {
        await this.fetchBoardDetail();
    }
};
</script>
<style scoped>
@import "@/assets/css/boardWrite.css";
</style>