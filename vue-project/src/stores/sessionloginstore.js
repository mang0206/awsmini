import { defineStore } from "pinia";
import { ref, computed } from "vue";


export const usesessionStore = defineStore("session", () => {
  const lgin = ref(true);

  function login() {
    lgin.value = false;
  }

  function logout() {
    lgin.value = true;
  }
  const getlgin = computed(() => lgin.value);

  return { lgin, login, logout, getlgin };
});
