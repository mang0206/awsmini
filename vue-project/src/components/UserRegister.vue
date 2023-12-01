<template>
    <body class="bg-light">
        
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">Modal title</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <p v-if="registrationResult === 'success'">회원 가입이 성공적으로 완료되었습니다.</p>
        <p v-if="registrationResult === 'failure'">회원 가입에 실패했습니다. 다시 시도해주세요.</p>ㄴ
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" @click="redirectToLogin">확인</button>
      </div>
    </div>
  </div>
</div>

    <div id="register_container" class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header text-center">
                        <h2 id="title_text">회원가입</h2>
                    </div>
                    <div class="card-body">
                        <form @submit.prevent="submitForm" action="http://localhost:8090/user/register" method="post">


                            <div class="form-group">
                                <label for="userId">아이디</label>
                                <div class="input-group">
                                    <input v-model="formData.userId" type="text" class="form-control" id="userId"
                                        placeholder="아이디를 입력해 주세요" required>
                                    <div class="input-group-append">
                                        <button type="button" class="btn btn-primary" @click="checkUserId">중복확인</button>
                                    </div>
                                </div>


                                <small id="availability-message" class="form-text text-info">{{ availabilityMessage
                                }}</small>
                            </div>

                            <div class="form-group">
                                <label for="password">비밀번호</label>
                                <input v-model="formData.password" type="password" class="form-control" id="password"
                                    placeholder="비밀번호를 입력해 주세요" required>
                            </div>

                            <div class="form-group">
                                <label for="nickname">닉네임</label>
                                <div class="input-group">
                                    <input v-model="formData.nickname" type="text" class="form-control" id="nickname"
                                        placeholder="닉네임을 입력해 주세요" required>
                                    <div class="input-group-append">
                                        <button type="button" class="btn btn-primary" @click="checkNickname">중복확인</button>
                                    </div>
                                </div>
                                <small id="nickname-availability-message" class="form-text text-info">{{
                                    nicknameAvailabilityMessage }}</small>
                            </div>

                            <div class="form-group">
                                <label for="email">이메일</label>
                                <div class="input-group">
                                    <input v-model="formData.email" type="email" class="form-control" id="email" placeholder="이메일을 입력해 주세요" required>
                                    <div class="input-group-append">
                                        <button type="button" class="btn btn-primary" @click="checkEmail">중복확인</button>
                                    </div>
                                </div>
                                <small id="email-availability-message" class="form-text text-info">{{
                                    emailAvailabilityMessage }}</small>
                            </div>

                            <div class="form-group">
                                <label>관심 카테고리</label>
                                <div id="category_33">
                                    <div class="col-sm-6">
                                        <div id="category_row1">
                                            <div class="form-check form-check-inline">
                                                <input v-model="formData.category" class="btn-check" type="checkbox" name="category" id="cat1" value="운동">
                                                <label class="btn btn-outline-primary" for="cat1"> 운동</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input v-model="formData.category" class="btn-check" type="checkbox" name="category" id="cat2" value="게임">
                                                <label class="btn btn-outline-primary" for="cat2">게임</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input v-model="formData.category" class="btn-check" type="checkbox" id="cat3" name="categories" value="애니">
                                                <label class="btn btn-outline-primary" for="cat3">애니</label>
                                            </div>
                                        </div>
                                        <div id="category_row2">
                                            <div class="form-check form-check-inline">
                                                <input v-model="formData.category" class="btn-check" id="cat4" type="checkbox" name="category" value="영화">
                                                <label class="btn btn-outline-primary" for="cat4">영화</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input v-model="formData.category" class="btn-check" id="cat5" type="checkbox" name="category" value="요리">
                                                <label class="btn btn-outline-primary" for="cat5">요리</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input v-model="formData.category" class="btn-check" id="cat6" type="checkbox" name="category" value="코딩">
                                                <label class="btn btn-outline-primary" for="cat6">코딩</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group"><label for="phoneNumber">휴대폰 번호</label>
                                    <input v-model="formData.phoneNumber" type="text" class="form-control"
                                        id="phoneNumber" placeholder="휴대폰 번호를 입력해 주세요" required>
                            </div>

                            <div class="form-group row">
                                <div class="col-sm-10">
                                    <button type="submit" router-link to="/user/login" class="btn btn-primary btn-block">회원가입</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </body>
</template>
<script>
import axios from 'axios';
import {api} from '@/common'
import { useRouter } from 'vue-router';

const router = useRouter();

export default {
    data() {
        return {
            showModal:false,
            formData: {
                userId: '',
                password: '',
                nickname: '',
                email: '',
                category: [],
                phoneNumber: '',
            },
            availabilityMessage: '',
            nicknameAvailabilityMessage: '',
            emailAvailabilityMessage: '',
        };
    },
    methods: {
        async checkUserId() {
            const user_id = this.formData.userId;

            if (!user_id) {
                this.availabilityMessage = "아이디를 입력해주세요.";
                return;
            }

            try {
                const response = await axios.get(`http://13.58.59.124:8090/user/user_id/${user_id}`);
                if (response.data === "중복된 아이디입니다.") {
                    this.availabilityMessage = "중복된 아이디입니다. 다른 아이디를 입력해주세요.";
                } else {
                    this.availabilityMessage = "사용 가능한 아이디입니다.";
                }
            } catch (error) {
                this.availabilityMessage = "요청에 실패했습니다. 다시 시도해주세요.";
            }
        },


        async checkNickname() {
            const nickname = this.formData.nickname;

            if (!nickname) {
                this.nicknameAvailabilityMessage = "닉네임을 입력해주세요.";
                return;
            }
            try {
                
                const response = await axios.get(`http://localhost:8090/user/nickname/${nickname}`);
                console.log('Nickname Check Response:', response);
                if (response.data === "중복된 닉네임입니다.") {
                    this.nicknameAvailabilityMessage = "중복된 닉네임입니다. 다른 닉네임를 입력해주세요.";
                } else {
                    this.nicknameAvailabilityMessage = "사용 가능한 닉네임입니다.";
                }
            } catch (error) {
                this.nicknameAvailabilityMessage = "요청에 실패했습니다. 다시 시도해주세요.";
            }
        },

        async checkEmail() {
            const email = this.formData.email;

            if (!email) {
                this.emailAvailabilityMessage = "이메일을 입력해주세요.";
                return;
            }
            try {
                const response = await axios.get(`http://localhost:8090/user/email/${email}`);
                if (response.data === "중복된 이메일입니다.") {
                    this.emailAvailabilityMessage = "중복된 이메일입니다. 다른 이메일을 입력해주세요.";
                } else {
                    this.emailAvailabilityMessage = "사용 가능한 이메일입니다.";
                }
            } catch (error) {
                this.emailAvailabilityMessage = "요청에 실패했습니다. 다시 시도해주세요.";
            }
        },

        async submitForm() {
            try {
                const obj = {
                    ...this.formData,
                    category : this.formData.category.toString()
                }
                const response = await axios.post('http://localhost:8090/user/register', obj);
                console.log('Server Response:', response);
                console.log(response.status==200 )
                if (response.data ==="성공적으로 회원 가입이 진행되었습니다.") {
                    console.log('가입 성공');
                    this.registrationResult = 'success';
                    // location.href = "http://localhost:9090/user/login"
                    router.push("/user/login");
                } else {
                    console.error('아이디 또는 비밀번호가 잘못 입력되었습니다.', response.data);
                    this.registrationResult = 'failure';
                }
            } catch (error) {
                console.error('서버와의 통신 중 오류가 발생했습니다.', error);
            }
        },
        redirectToLogin() {
        this.showModal = false; // Close the modal if needed
        // location.href = "http://localhost:9090/user/login";
        router.push("/user/login");
    },
    },
};
</script>
<style scoped>
@import "@/assets/css/userRegister.css";
</style>