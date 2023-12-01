<template>
    <h1 style="font-size:40px">UniMeeting 커뮤니티에서 자유롭게 이야기 나눠보세요!</h1>
    <div class="inner">
        <form id="search" @submit.prevent="meetingForm" class="search">
            <input v-model="search" type = "search"  class="search-bar" placeholder="검색어를 입력해 주세요">
            <input type = "image" :src="icon" value = "검색" class="searching">
        </form>
    </div>
    <div class="list_container">
        <div class="form-select1">
        <select v-model="selectedOption" @change="sortmeeting" name="changeTest" class="form-select form-select-sm" aria-label=".form-select-sm example">
            <option selected>menu</option>
            <option value="no">기본</option>
            <option value="title">제목순</option>
            <option value="start">최신순</option>
        </select>
        </div>
            <div class="row row-cols-3 row-cols-md-3 g-4" >
                <div class="col" v-for="meeting in meetings.values">
                    <div class="card">
                        <router-link v-bind:to="`/meeting/${meeting.idx}`">
                            <img :src="'http://localhost:8090'+meeting.imageUrl" @error="src=bookImage" class="card-img-top">
                            <div class="card-body">
                                <h4 class="card-title" style="font-weight: bold">{{ meeting.title }}</h4>
                                <p class="card-text">{{ meeting.content }}</p>

                            </div>
                            <div class="card-date" style="color: gray" >{{ meeting.createdDatetime }}</div>
                        
                        </router-link>

                    </div>
                </div>
            </div>
        
    </div>

</template>

<script setup>
    import { api, cleardiv } from '@/public/common';
    import { onBeforeMount, reactive, ref } from 'vue';
    import icon from "../assets/images/search_icon.png";
    import bookImage from '@/assets/images/book.png';
    const meetings = reactive([]);
    let search = ""

    const url1 = () => {
    }
    let selectedOption = ref('기본');

    onBeforeMount( () => {
        api("http://localhost:8090/meetings", "get", {})
            .then(resp => {
                meetings.values = resp;
                console.log(meetings)
            })
    }
    
    )
    const sortmeeting = () => {
        console.log(selectedOption.value)
        if (selectedOption.value === 'title') {
            meetings.values.sort((a, b) => a.title.localeCompare(b.title));
        } else if (selectedOption.value === 'start') {
            meetings.values.sort((a, b) => new Date(a.createdDatetime) - new Date(b.createdDatetime)).reverse();
        }
        else {
            meetings.values.sort((a,b) => new Date(a.createdDatetime) - new Date(b.createdDatetime));
        }
        meetings.values = [...meetings.values];
    }

    const meetingForm = () => {
        api(`http://localhost:8090/meetings?search=${search}`, "get")
            .then(resp => {
                meetings.values = resp;
            })
        
    }
    
</script>

<style scoped>
@import '@/assets/css/main.css';

</style>