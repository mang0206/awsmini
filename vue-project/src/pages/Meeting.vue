<template>
    <div class="page-container">
        <Side @chage-category="chageCategory"></Side>

        <div class="contents-right">
            <div class="meeting-search">
                <form id="search" @submit.prevent="meetingForm"> <!--url 동적 변경-->
                    <div class="search-label">
                        <!-- <label><input id="search-submit" type="image" src="@/assets/images/icon-search.png" alt="검색"></label> -->
                        <label><input id="search-text" v-model="search" required /><br></label>
                    </div>
                </form>
                <button class="make-meeting-button" @click="goToInsertMet" >모임 만들기</button>
            </div>

            <Contain :category="getCategory"></Contain>
             <!--    <nav aria-label="Page navigation example">
                    <ul style="text-align: center;" class="pagination" >
                         <template v-for="index in countMeeting" :key="index">
                            <li class="page-item"><a class="page-link">{{ index }}</a></li>
                        </template> 
                        <template v-for="pageNumber in Math.ceil(countMeeting / itemsPerPage)" :key="pageNumber">
  <li class="page-item" :class="{ 'active': pageNumber === currentPage }">
    <a class="page-link" @click="changePage(pageNumber)">{{ pageNumber }}</a>
  </li>
</template>
                    </ul>
                </nav> -->
        </div> 
        
    </div>
</template>

<script setup>
    import Side from '@/components/MeetingSide.vue';
    import Contain from '@/components/MeetingContain.vue';
    import { ref, computed } from 'vue';
    import { api, cleardiv } from '@/public/common';
    import { makeMeetingBlock } from '@/public/makeBlock';
    import { useRouter } from 'vue-router';

    const router = useRouter();

    const goToInsertMet = () => {
      // router.push()를 사용하여 해당 경로로 이동합니다.
      router.push('/meeting/goInsertMet');
    };

    let category = ref("");
    let url = ref("http://localhost:8090/meetings");

    let search = "";

    const countMeeting = ref(0);
    const currentPage = ref(1);

    api(url.value, "get").then(meetings => {
        cleardiv()
        // countMeeting.value = Math.ceil(meetings.length / 4);
        // console.log(countMeeting.value);
        for(let o of meetings){
            makeMeetingBlock(o);
        }
    })

    function chageCategory(data){
        category.value = data;
        url.value = `http://localhost:8090/meetings?ctgr=${category.value}`;
    }

    const meetingForm = () => {
        cleardiv()
        const pageUrl = `${url.value}${category.value === '' ? '?' : '&'}search=${search}&page=${currentPage.value}`;
                api(pageUrl, 'get').then((meetings) => {
    // countMeeting.value = Math.ceil(meetings.length / itemsPerPage);
    // console.log(countMeeting.value);
            for(let o of meetings){
                makeMeetingBlock(o);
            }
        })
    }

    const changePage = (page) => {
        currentPage.value = page;
        meetingForm(); // Reload data for the selected page
    };

    const getCategory = computed(() => category.value);
</script>

<style >
    @import '@/assets/css/meeting.css';
    @import '@/assets/css/meetingBox.css';
    .page-container {
        display: flex !important;
        flex-direction: row !important;
        justify-content: center !important;
        /* background-color: aquamarine; */
        /* position: relative; */
        /* width: 1440px;
        height: 880px; */
    }
</style>
