<template>
    <form id="form" @submit.prevent="submitMeeting(meeting_idx)" method="post" enctype="multipart/form-data"
        class="col-lg-8 mx-auto p-4 py-md-5">
        <fieldset class="row mb-3">
            <legend class="col-form-label col-sm-2 pt-0">카테고리</legend>
            <div class="col-sm-10">
                <div class="form-check">
                    <input class="form-check-input" type="radio" v-model="addMeeting.category" id="gridRadios1" value="운동"
                        required>
                    <label class="form-check-label" for="gridRadios1">
                        운동
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" v-model="addMeeting.category" id="gridRadios2" value="게임">
                    <label class="form-check-label" for="gridRadios2">
                        게임
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" v-model="addMeeting.category" id="gridRadios3" value="애니">
                    <label class="form-check-label" for="gridRadios3">
                        애니
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" v-model="addMeeting.category" id="gridRadios4" value="영화">
                    <label class="form-check-label" for="gridRadios4">
                        영화
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" v-model="addMeeting.category" id="gridRadios5" value="요리">
                    <label class="form-check-label" for="gridRadios5">
                        요리
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" v-model="addMeeting.category" id="gridRadios6" value="코딩">
                    <label class="form-check-label" for="gridRadios6">
                        코딩
                    </label>
                </div>
            </div>
        </fieldset>
        <div class="row mb-3 position-relative">
            <label for="title" class="col-sm-2 form-label col-form-label">제목</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="addMeeting.title" id="title" minlength="2" required>
            </div>
        </div>
        <div class="row mb-3 position-relative">
            <label for="location" class="col-sm-2 form-label col-form-label">장소</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="addMeeting.location" id="location" required>
            </div>
        </div>
        <div class="row mb-3">
            <label for="recruits" class="col-sm-2 col-form-label">모집 인원</label>
            <div class="col-sm-10">
                <input type="number" min="2" v-model="addMeeting.recruits" class="form-control" id="recruits" required>
            </div>
        </div>
        <div class="row mb-3">
            <label for="startDatetime" class="col-sm-2 col-form-label">시작일</label>
            <div class="col-sm-10">
                <input type="date" class="form-control" v-model="addMeeting.startDatetime" id="startDatetime" required>
            </div>
            <!--        <div class="invalid-feedback">-->
            <!--            Please enter a message in the textarea.-->
            <!--        </div>-->
        </div>
        <div class="row mb-3">
            <label for="content" class="form-label">소개</label>
            <textarea class="form-control" id="content" v-model="addMeeting.content" rows="10" maxlength="1000"
                required></textarea> {{ addMeeting.content.length }} / 1000
            <!--        <div class="invalid-feedback">-->
            <!--            Please enter a message in the textarea.-->
            <!--        </div>-->
        </div>
        <div class="mb-3">
            <label for="images" class="form-label">사진</label>
            <input class="form-control" type="file" id="images" @change="handleFileChange" multiple >
        </div>
<div v-if="meeting_idx">
    <template v-for="url in oldImaegs.values">
        <div class="position-relative" style="display: inline-block; margin: 5px;">
            <!-- 이미지 -->
            <img :src="'http://localhost:8090' + url" class="rounded m-2" style="width: 200px" disabled >

            <!-- 삭제 버튼 -->
            <button type="button" class="btn btn-danger btn-sm position-absolute top-0 end-0"
                    @click="deleteImage(url)">
                <i class="bi bi-trash">X</i>
            </button>
        </div>
    </template>
</div>

        <button id="submit" type="submit" class="btn btn-primary"
            @click="was_validated()">등록</button><!--    제목: <label><input id="title" name="title" /><br></label>-->
        <!--    모집 인원: <label><input name="recruits" type="number" min="2"/><br></label>-->
        <!--    시작일: <label><input name="startDatetime" type="date"/><br></label>-->
        <!--    소개: <label><input name="content_text" maxlength="500" /><br></label>-->
        <!--    사진: <label><input type="file" name="images" multiple/><br></label>-->
    </form>
</template> 

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const server = "http://localhost:8090";
const meeting_server = server + "/meetings";
let addMeeting = reactive({
    category: '',
    title: '',
    location: '',
    recruits: 0,
    startDatetime: '',
    content: ''
});
const images = reactive([]);
const oldImaegs = reactive([]);

const props = defineProps(['meeting_idx']);
let meeting_idx = props.meeting_idx;
if (meeting_idx) {
    axios.get(`${meeting_server}/update/` + meeting_idx)
        .then((resp) => {
            const oldMet = resp.data;
            console.log(oldMet)

            addMeeting.category = oldMet.meeting.category;
            addMeeting.title = oldMet.meeting.title;
            addMeeting.location = oldMet.meeting.location;
            addMeeting.recruits = oldMet.meeting.recruits;
            addMeeting.startDatetime = oldMet.meeting.startDatetime.slice(0, 10);
            addMeeting.content = oldMet.meeting.content;
            oldImaegs.values = oldMet.imgUrl;
            console.log(oldImaegs.values)

        })
}


const submitMeeting = (update) => {

    let formData = new FormData();

    if (images.values.length !== 0) {
        images.values.forEach((file, index) => {
            formData.append('file', file); // 'file'이라는 키를 사용하여 파일 추가
        });
    }

    const formattedData = {
        ...addMeeting,
        startDatetime: addMeeting.startDatetime + 'T00:00', // Add a default time, adjust as needed
    };

    // 파일과 다른 데이터를 함께 보내기 위해 FormData를 사용하고, 헤더 설정

    formData.append('meetingData', new Blob([JSON.stringify(formattedData)], { type: 'application/json' }));
    console.log(formData)
    if (update) {

        axios.put(`${meeting_server}/${meeting_idx}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data;    boundary=----WebKitFormBoundaryYourBoundary',
            },})
        .then((resp) => {
            window.alert(resp.data.message);
            router.push("/meeting/" + meeting_idx);
            // location.href = "http://localhost:5173/meeting/" + meeting_idx;
        })

    } else {
        console.log("this")
        axios.post(meeting_server, formData, {
            headers: {
                'Authorization': sessionStorage.getItem("token"),
                'Content-Type': 'multipart/form-data;    boundary=----WebKitFormBoundaryYourBoundary',
            },
        }).then((resp) => {
            window.alert(resp.data.message);
            // location.href = "http://localhost:5173/meetings"
            router.push("/meetings");
        }).catch((e) => console.log(e));
    }
}
// deleteImage 구현해야함. 서버는 구현완
const deleteImage = (url) => {
    
    axios.delete(`${meeting_server}/${meeting_idx}/image?image=${url}`, {
            headers: {
                'Authorization': sessionStorage.getItem("token"),
                'Content-Type': 'multipart/form-data;    boundary=----WebKitFormBoundaryYourBoundary',
            },
        }).then((resp) => {
            window.alert(resp.data.message);
        }).catch((e) => console.log(e));

}



const handleFileChange = (event) => {
    images.values = Array.from(event.target.files);
}


// 빈칸 입력 막기
const was_validated = () => {
    const form = document.querySelector("#form");
    form.classList.add("was-validated");
};


</script>

<style scoped>
@font-face {
    font-family: 'Pretendard-Regular';
    src: url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff') format('woff');
    font-weight: 400;
    font-style: normal;
}

* {
    font-family: 'Pretendard-Regular';

}

#title {
    width: 80%;

}

#recruits {
    width: 10%;
}

#startDatetime {
    width: 20%;
}

#images {
    width: 20%;
}

@font-face {
    font-family: 'Pretendard-Regular';
    src: url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff') format('woff');
    font-weight: 400;
    font-style: normal;
}

* {
    font-family: 'Pretendard-Regular';

}

.form-check-input:checked {
    background-color: #5302FE;
    border-color: #5302FE;
}

.btn-primary {
    --bs-btn-color: #fff;
    --bs-btn-bg: #5302FE;
    --bs-btn-border-color: #5302FE;
    --bs-btn-hover-color: #fff;
    --bs-btn-hover-bg: #6129FF;
    --bs-btn-hover-border-color: #6129FF;
    --bs-btn-focus-shadow-rgb: 49, 132, 253;
    --bs-btn-active-color: #fff;
    --bs-btn-active-bg: #6129FF;
    --bs-btn-active-border-color: #6129FF;
    --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
    --bs-btn-disabled-color: #fff;
    --bs-btn-disabled-bg: #5302FE;
    --bs-btn-disabled-border-color: #5302FE;
}


.btn-outline-primary {
    --bs-btn-color: #0d6efd;
    --bs-btn-border-color: #0d6efd;
    --bs-btn-hover-color: #fff;
    --bs-btn-hover-bg: #dee2e6 !important;
    --bs-btn-hover-border-color: #0d6efd;
    --bs-btn-focus-shadow-rgb: 13, 110, 253;
    --bs-btn-active-color: #fff;
    --bs-btn-active-bg: #0d6efd;
    --bs-btn-active-border-color: #0d6efd;
    --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
    --bs-btn-disabled-color: #0d6efd;
    --bs-btn-disabled-bg: transparent;
    --bs-btn-disabled-border-color: #0d6efd;
    --bs-gradient: none;
}

#title {
    width: 80%;

}

#recruits {
    width: 20%;
}

#startDatetime {
    width: 20%;
}

#images {
    border: 1px solid #dee2e6;
    width: 20%;
    background-image: none;
}

.form-check-label {
    color: #212529 !important;
}


form.was-validated .invalid-feedback {
    display: inline;
}

#displayNone * {
    display: none;
}
</style>