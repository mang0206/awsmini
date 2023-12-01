<template>
    <nav class="base-header-group" style="width: 100%;">
        <div class="logo">
            <img alt="" src="@/assets/images/meeting.png" class="logo-image">
            <span class="logo-text">UniMeeting</span>
        </div>
        <div id="navbar" class="frame-3" aria-expanded="false">
            <div class="nav_div">
                <router-link class="nav_text" id="nav_main" to="/">메인라운지</router-link>
            </div>
            <div class="nav_div">
                <router-link class="nav_text" to="/meetings">모임</router-link>
            </div>
            <div class="nav_div">
                <router-link class="nav_text" to="/boards/type/free">자유게시판</router-link>
            </div>
            <div class="nav_div">
                <router-link class="nav_text" to="/boards/type/notice" >공지사항</router-link>
            </div>

            <div v-if="!getIsLogin">
                <div class="nav-button">
                    <!-- <button class="base-button side-button" onclick="location.href='/user/register'">회원가입</button> -->
                    <router-link class="base-button side-button" to="/user/register">회원가입</router-link>
                    <!-- <button class="base-button" onclick="location.href='/user/login'">로그인</button> -->
                    <router-link class="base-button" to="/user/login">로그인</router-link>
                </div>
            </div>
            <div v-else>
                <div class="nav-button">
                    <!-- <button class="base-button  side-button" onclick="location.href='/mypage'">마이페이지</button> -->
                    <router-link class="base-button side-button" to="/mypage">마이페이지</router-link>
                    <button class="base-button" @click="logout">로그아웃</button>
                </div>
            </div>
            <!-- <div v-if="!getIsLogin">
                <div class="nav-button">
                    <router-link class="base-button side-button" to='/user/register'>회원가입</router-link>
                    <router-link class="base-button" to='/user/login'>로그인</router-link>
                </div>
            </div>
            <div v-else>
                <div class="nav-button">
                    <router-link class="base-button  side-button" to='/mypage'>마이페이지</router-link>
                    <router-link class="base-button" @click="logout">로그아웃</router-link>
                </div>
            </div> -->
        </div>
    </nav>
</template>

<script setup>
    import axios from 'axios';
    import {usesessionStore} from '@/stores/sessionloginstore'
    import { ref, computed, watch } from 'vue';

    let getIsLogin = computed(() => sessionStorage.getItem("token"))

    const logout = () => {      
      axios.get("http://localhost:8090/user/logout").then((res) => {
        if (res.headers['authorization'] == 'delete') {
          sessionStorage.removeItem("token");
          window.alert("로그아웃 되었습니다.");
          window.location.href = '/';
        }
        window.alert(res.data);
      }).catch(() => {
        window.alert("로그아웃을 수행하는 동안 오류가 발생하였습니다..");
      });
    }
    
</script>

<style scoped>
@import '@/assets/css/nav.css';
</style>