<template>
<UserRegister></UserRegister>
<my-modal v-if="showModal" @close="showModal = false"></my-modal>
</template>
<script>
// @ is an alias to /src
import UserRegister from '@/components/UserRegister.vue'

export default {
  components: {
  UserRegister,
  }
}
</script>

<style>
.study {
  color: blue;
}
</style>
