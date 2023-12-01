<template>
    <div class="contents-right">
        <div id="info_result"></div>
        <component :is="currentComponent" :userObj="user" v-if="currentComponent"></component>
    </div>
</template>

<script setup>
    import { defineProps, watch, computed, reactive  } from 'vue'; 
    
    import { api, cleardiv } from '@/public/common';
    import { makeMeetingBlock } from '@/public/makeBlock'
    import axios from 'axios';
    import MyInfo from '@/components/MypageMyInfo.vue';
    import Withdraw from '@/components/MypageWithDraw.vue';

    let url = "http://localhost:8090/mypage/meetings";
    let meetings = reactive([]);
    
    axios.get(url + '/participated', {
        headers:{'Authorization':sessionStorage.getItem("token")}
    }).then(response => {
        for(let o of response.data){
            makeMeetingBlock(o);
        }
        // console.log(response.data)
    })

    const p = defineProps({
        state : String
    });

    const getState = computed(() => p.state);
    watch(getState, (cur, prev) => {
        cleardiv()
        getMeeting(cur, url)
      }
    )

    // let user = reactive([]);

    // function chageUser(data){
    //     user[0] = data
    // }
    let user = computed("")

    axios.get('http://localhost:8090/user', {
        headers:{'Authorization':sessionStorage.getItem("token")}
    }).then(response => {user = response.data})
    console.log(user);

    let currentComponent = null;
    function getMeeting(s, url) {
        url = "http://localhost:8090/mypage/meetings";
        let flag = true
        currentComponent = null;
        cleardiv()
        switch(s){
            case "attend":
                url += "/participated";
                break;
            case "create":
                url += "/created";
                break;
            case "scrap":
                url += "/scraped"
                break;
            case "myInfo":
                flag = false;
                currentComponent = MyInfo;
                break;
            case "withDraw":
                currentComponent = Withdraw;
                flag = false;
        }

        if(flag) {
            axios.get(url, {
                headers:{'Authorization':sessionStorage.getItem("token")}
            }).then(response => {
                for(let o of response.data){
                    makeMeetingBlock(o);
                }
            })
        } else {

        }
    } 
</script>

<style scoped>
@import '@/assets/css/meetingBox.css';
@import '@/assets/css/mypage.css';
* {
    margin: 5px;
}
</style>