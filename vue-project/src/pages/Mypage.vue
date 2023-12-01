<template>
    <Side @chage-state="chageState"></Side>
    <Contain :state="getState"></Contain>
</template>

<script setup>
    import { ref, computed } from 'vue'
    import Side from '@/components/MypageSide.vue'
    import Contain from '@/components/MypageMeeting.vue'
    
    let state = ref("attend")

    function chageState(data){
        state.value = data
    }

    const getState = computed(() => state.value);

</script>
<style scoped>
@import '@/assets/css/mypage.css';
@import '@/assets/css/mypage_user_edit.css';

* {
    margin: 5px;
}
</style>
