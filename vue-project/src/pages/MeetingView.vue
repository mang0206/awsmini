<template>
    <MeetingDetail :meeting_idx="$route.params.idx"></MeetingDetail>
</template>

<script setup>
import MeetingDetail from '@/components/MeetingDetail.vue';
</script>
