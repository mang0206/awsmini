<template>
    <form @submit.prevent="updateUser" class='form_group'>
        <input type="hidden" name='idx' value="userObj.idx">
        <span class='span_text'>아이디</span> <input type='text' name='userId' class='input_box' disabled :value="userObj.userId"> <br>
        <span class='span_text'>비밀번호</span> <input class='input_box input_chane_box' type='password' v-model="formData.password" @input="checkPWD" id='pwd' placeholder = '변경할 비밀번호 입력' required> <br>
        <span class='span_text'>비밀번호 확인</span> <input class='input_box input_chane_box' type='password' v-model="check_pwd" @input="checkPWD" id='c_pwd' placeholder = '비밀번호 확인' required> <br>
        <span class='span_text'>닉네임</span> <input class='input_box input_chane_box' type='text' v-model='formData.nickname' placeholder = '변경할 닉네임 입력' required> <br>
        <div class='edit-category'>
            <div class="span-height-div">
                <span class='span_text'>관심 카테고리</span>
            </div>
            <div class="custom-checkbox">
                <label>
                    <input v-model="formData.category" type="checkbox" class="custom-checkbox-input" name="category" value="운동">
                    <div class="custom-checkbox-text">운동</div>
                </label>
                <label>
                    <input v-model="formData.category" type="checkbox" class="custom-checkbox-input" name="category" value="코딩">
                    <div class="custom-checkbox-text">코딩</div>
                </label>
                <label>
                    <input v-model="formData.category" type="checkbox" class="custom-checkbox-input" name="category" value="요리">
                    <div class="custom-checkbox-text">요리</div>
                </label>
                <label>
                    <input v-model="formData.category" type="checkbox" class="custom-checkbox-input" name="category" value="게임">
                    <div class="custom-checkbox-text">게임</div>
                </label>
                <label>
                    <input v-model="formData.category" type="checkbox" class="custom-checkbox-input" name="category" value="애니">
                    <div class="custom-checkbox-text">애니</div>
                </label>
                <label>
                    <input v-model="formData.category" type="checkbox" class="custom-checkbox-input" name="category" value="영화">
                    <div class="custom-checkbox-text">영화</div>
                </label>
            </div> 
        </div>
        <span class='span_text'>이메일</span> <input class='input_box' type='text' name='email' disabled :value='userObj.email'> <br>
        <span class='span_text'>휴대폰 번호</span> <input class='input_box' type='text' name='phoneNumber' disabled :value='userObj.phoneNumber'> <br>
        <div class='submit_btn'>
            <input type='submit' value='정 보 변 경' id='submit_button' :disabled="!showBtn">
        </div> 
    </form>
</template>

<script setup>
    import { defineProps, ref, reactive } from 'vue'; 
    import axios from 'axios';
    import { useRouter } from 'vue-router';

    const router = useRouter();
    const p = defineProps({
        userObj : Object
    });

    let formData = reactive({
        password: '',
        nickname: '',
        category: []
    })

    let showBtn = ref(false)
    let check_pwd = ref("")

    const checkPWD = () => {
        if (formData.password != "")
            showBtn.value = formData.password === check_pwd.value;
    }

    const updateUser = () => {
        let f_data = JSON.parse(JSON.stringify(formData))
        f_data.category = f_data.category.toString()
        axios.put("http://localhost:8090/mypage/user", f_data, {
                headers:{'Authorization': sessionStorage.getItem("token")}
            }).then(response => {
                window.alert(response.data.message);
                sessionStorage.removeItem("token")
                // window.location.href = "/";
                router.push("/");
            })
    }
</script>