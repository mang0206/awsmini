<template>
    <div class="meeting-nav-left" id="mypage_side">
        <div v-for="categroy in categories" class="meeting-menu-item">
            <div class="btn-menu-item">
                <button @click="handleCategory(categroy)" class ="nav-link"> {{ categroy }}</button>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { defineEmits, reactive } from 'vue';
    import { api } from '@/public/common'
    const emit = defineEmits(["chage-category"]);

    let categories = reactive([])
    api("http://localhost:8090/meetings/category","get").then(ctgr => { 
        for(let o of ctgr){
            categories.push(o)
        }
    })

    function handleCategory(category) {
        emit("chage-category", category)
    }

</script>

<style scoped>
</style>