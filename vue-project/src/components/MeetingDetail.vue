<template >
    <div class="col-lg-8 mx-auto p-5 py-md-5">
        <main class="row">
            <!--        상단 왼쪽(카테고리, 제목, 신청현황)-->
            <div class="col mb-2">

                <!--            카테고리(button 클릭 시 해당 카테고리의 meeting board 페이지로 이동)-->
                <div class="row justify-content-between mb-4" style="margin:2px;">
                    <a style="width:200px;" class="col-1 btn btn-outline-primary" @click="$router.push('/meetings')">{{ category }}</a>
                </div>

                <!--            미팅 제목-->
                <h1 class="fw-bold mb-2">{{ title }}</h1>

                <!--            미팅 현재인원/ 모집인원-->
                <div class="text-body-secondary mb-4">
                    <p class="fs-6">현재인원 / 모집인원 : {{ currentRecruits }} / {{ recruits }} | 시작일: {{
                        startDatetime.substring(0,
                            10) }}
                    </p>
                </div>
            </div>

            <!--        상단 오른쪽( 신청/스크랩 버튼 등 )-->
            <div class="col mb-2 position-relative" style="text-align: right;">
                <div v-if="writer">
                    <!--                삭제 & 수정 버튼 -->
                    <div class="btn-group position-absolute top-0 end-0" role="group"
                        aria-label="Basic mixed styles example" style="width:max-content;">
                        <button type="button" class="btn btn-danger" @click="deleteMeeting">삭제
                        </button>
                        <button type="button" class="btn btn-success"
                            @click="$router.push('/meeting/goUpdateMet/' +meeting_idx)">수정
                        </button>
                    </div>
                    <!--                신청 현황 offcanvas 열기 버튼-->
                    <button id="currentApply" class="btn btn-primary position-absolute bottom-0 end-0 mb-4" type="button"
                        data-bs-toggle="offcanvas" data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">신청
                        현황</button>
                </div>
                <!--            th:unless=${isWriter} -> 작성자가 아닐 경우-->
                <div v-else class="row justify-content-end">

                    <!--                and v-if=${apply} -> 해당 소모임에 참가 신청을 하지 않은 경우, 신청 버튼 보이기 -->
                    <!--                and v-if=${apply} -> 해당 소모임에 참가 신청을 한 경우, 신청 취소 버튼 보이기 -->
                    <div class="col-auto offset-md-4" v-if="!applicant">
                        <a class="btn btn-primary btn-lg px-4 btn-color" data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                            @click="apply">신청</a>
                    </div>
                    <div class="col-auto offset-md-4" v-else>
                        <a class="btn btn-primary btn-lg px-4 btn-color" data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                        @click="deleteApply('applicant')">신청 취소</a>
                    </div>

                    <!--                and th:if=${scrap} -> 해당 소모임에 스크랩을 하지 않은 경우, 스크랩 버튼 보이기 -->
                    <!--                and th:unless=${scrap} -> 해당 소모임에 스크랩을 한 경우, 스크랩 취소 버튼 보이기 -->
                    <div class="col-auto" v-if="!scrap">
                    <a class="btn btn-primary btn-lg px-4 btn-color"
                    @click="scraping">스크랩</a>
                </div>
                <div class="col-auto" v-else>
                    <a class="btn btn-primary btn-lg px-4 btn-color"
                    @click="cancelScrap">스크랩 취소</a>
                </div>
                </div>
            </div>
            <hr class="mb-5">

            <!--        하단(이미지, 글 내용) -->
            <div class="meeting-contents">

                <!--            v-if="${meeting_image}" -> 이미지가 포함된 글일 경우-->
                <div v-if="images.length != 0" id="carouselExampleDark"
                    class="carousel carousel-dark slidez border border-dark-subtle mb-5" data-bs-ride="carousel">
                    <div class="carousel-inner position-absolute top-50 start-50 translate-middle">
                        <template v-for="(image, index) in images" :key="image">

                            <div class="carousel-item active" v-if="index == 0">
                                <img :src="server + image" class="d-block" alt="no image">
                            </div>
                            <div class="carousel-item" v-else>
                                <img :src="server + image" class="d-block" alt="no image">
                            </div>

                        </template>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target='#carouselExampleDark'
                        data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target='#carouselExampleDark'
                        data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
                <br>
                <div class="mb-5 fs-6" style="width:80%;" v-html="content.split('\n').join('<br />')"></div>
            </div>

        </main>
        <!--    신청 현황 offcanvas-->
        <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasRight" aria-labelledby="offcanvasRightLabel">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title" id="offcanvasRightLabel">신청 현황</h5>
                <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
            </div>
                <div v-if="members.length != 0" class="offcanvas-body">
                    <template v-for="member in members">
                            <div class="card mb-2" style="width: 18rem;">
                                <div class="card-body">
                                    <h5 class="card-title">{{ member.nickname }}</h5>
                                    <p class="card-text">{{ member.category }}</p>
                                    <template v-if="member.accepted == 0">
                                        <a @click='acceptApply(true, member.idx, member.nickname)'
                                        class="btn btn-primary me-1">수락</a>
                                        <a @click="deleteApply('writer', member.userIdx, member.nickname)"
                                        class="btn btn-primary">거절</a>
                                    </template>
                                    <a v-else
                                    @click='acceptApply(false, member.idx, member.nickname)'
                                        class="btn btn-primary">수락 취소</a>
                                </div>
                            </div>
                        </template>
                </div>
                <div v-else class="offcanvas-body">
                        아직 신청한 사람이 없어요!
                </div>

            </div>

</div>
    <!-- <div class="modal fade" id="staticBackdrop" ref="confirmModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="staticBackdropLabel">{{title}}</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        {{ modalMessage }}
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" @click="confirmAction()">Yes</button>
      </div>
    </div>
  </div>
</div> 

<div class="modal fade" id="exampleModal" ref="alertModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">{{title}}</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        {{ modalMessage }}
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>  
      </div>
    </div>
  </div>
</div>  -->
</template>
<script setup>
import { ref, onBeforeMount, defineProps     } from 'vue';
import { api } from '@/common.js'
import axios from 'axios';

const props = defineProps(['meeting_idx']);
let meeting_idx = props.meeting_idx;

const server = "http://localhost:8090";
const meeting_server = server + "/meetings/";

const title = ref('');
const content = ref('');
const category = ref('');
const recruits = ref(0);
const location = ref('');
const startDatetime = ref('');
const createdDatetime = ref('');
const currentRecruits = ref(0);
const images = ref([]);
const applicant = ref(false);
const writer = ref(false);
const scrap = ref(false);
const members = ref([]);

// const confirmModal = ref(null);
// const alertModal = ref(null);
// let modalMessage = ref('');

// const confirmCallback = ref(null)
// const showAlertModal = (message) => {
//   modalMessage.value = message;
//   alertModal.value.show();
// }

// const showConfirmModal = (message, confirmAction) => {
//   modalMessage.value = message;
//   confirmCallback.value = confirmAction;
//   confirmModal.value.style.display = "none";
// };

// const confirmAction = () => {
//   if (confirmCallback.value) {
//     confirmCallback.value();
//   }
//   confirmModal.value.style.display = "block";
// }
onBeforeMount(() => {
    // if (token) {
    axios.get(meeting_server + meeting_idx, {
        headers: {
            'Authorization': sessionStorage.getItem("token")
        }
    })
        .then((response) => {
            const resp = response.data;
            title.value = resp.title;
            content.value = resp.content;
            category.value = resp.category;
            recruits.value = resp.recruits;
            location.value = resp.location;
            startDatetime.value = resp.startDatetime;
            createdDatetime.value = resp.createdDatetime;
            currentRecruits.value = resp.currentRecruits;
            images.value = resp.images;
            applicant.value = resp.applicant;
            writer.value = resp.writer;
            scrap.value = resp.scrap;

            if (resp.writer) {
                api(meeting_server + "applicants/" + meeting_idx, "GET", {})
                    .then((member_resp) => {
                        members.value = member_resp;
                        console.log(member_resp)
                    })
            }
        })

    // }else{
    // }

})

const deleteMeeting = () => {
    if (window.confirm("이 글을 삭제하시겠습니까?")) {
        api(meeting_server + meeting_idx, "DELETE")
            .then((resp) => {

                window.alert(resp.message);
                document.location.href = "/meetings";
            })
    }
}

const apply = () => {
    // showConfirmModal("이 모임에 신청 요청을 보내겠습니까?")

    // console.log(resp)
    // if (confirmAction) {
    //     axios.post(meeting_server + "apply/" + meeting_idx, {}, {
    //         headers: {
    //             'Authorization': sessionStorage.getItem("token")
    //         }
    //     })
    //         .then((resp) => {
    //             console.log(resp.data)
    //             window.alert(resp.data.message);
    //             document.location.href = "/meeting/" + meeting_idx;
    //         })

    // }
    axios.post(meeting_server + "apply/" + meeting_idx, {}, {
        headers: {
            'Authorization': sessionStorage.getItem("token")
        }
    })
        .then((resp) => {
            console.log(resp.data)
            window.alert(resp.data.message);
            document.location.href = "/meeting/" + meeting_idx;
        })

}

const acceptApply = (accept, idx, nickname) => {
    const message = accept ? nickname + "님의 요청을 수락하시겠습니까?" : "수락을 취소하시겠습니까?";

    if (window.confirm(message)) {
        api(meeting_server + "apply/" + idx, "PUT", {})
            .then((resp) => {
                window.alert(resp.message);
                document.location.href = "/meeting/" + meeting_idx;
            })
    }
}

// type = 삭제 요청한 사람
const deleteApply = (type, user_idx, user_nickname) => {

    let url = `${meeting_server}apply/${meeting_idx}`;
    let alertMessage;
    if(type == 'writer'){
        url += `?user_idx=${user_idx}`
        alertMessage = user_nickname + "님의 요청을 거절하시겠습니까?";
    }else{
        alertMessage = "신청을 취소하시겠습니까?"
    }
    if (window.confirm(alertMessage)) {
        axios.delete(url , {
            headers: {
                'Authorization' : sessionStorage.getItem("token")
            }
        })
            .then((resp) => {
                window.alert(resp.data.message);
                document.location.href = "/meeting/" + meeting_idx;
            })
    }
}

const scraping = () => {
    axios.post(`${meeting_server}scrap/${meeting_idx}`, {},{
        headers : {
            'Authorization' : sessionStorage.getItem("token")
        }
    })
    .then((resp)=> {
        window.alert(resp.data.message);
    })

}

const cancelScrap = () => {
    axios.delete(`${meeting_server}scrap/${meeting_idx}`, {
        headers : {
            'Authorization' : sessionStorage.getItem("token")
        }
    })
    .then((resp)=> {
        window.alert(resp.data.message);
    })

}
</script>

<style scoped>
@font-face {
    font-family: 'Pretendard-Regular'!important;
    src: url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff') format('woff');
    font-weight: 400 !important;
    font-style: normal!important;
}

* {
    font-family: 'Pretendard-Regular' !important;
}

.btn-color {
    color: #5302FE !important;
}

.meeting-contents>* {
    margin: 0 auto !important;
}

a.btn.btn-primary.btn-lg.px-4.btn-color {
    --bs-btn-color: #5302FE !important;
    --bs-btn-bg: #fff !important;
    --bs-btn-border-color: #5302FE !important;
    --bs-btn-hover-border-color: white !important;
    --bs-btn-hover-bg: #a880ff !important;
    --bs-btn-active-bg: #a880ff !important;
    --bs-btn-active-border-color: #a880ff !important;
}
a.col-1.btn.btn-outline-primary {
    --bs-btn-color: #fff !important;
    --bs-btn-bg: #5302FE !important;
    --bs-btn-border-color: #5302FE !important;
    --bs-btn-hover-color: #5302FE !important;
    --bs-btn-hover-bg: white !important;
    --bs-btn-hover-border-color: #5302FE !important;
}

.col-md-auto a {
    width: 200px !important;
}

main {
    margin: auto !important;
}

.justify-content-end {
    height: fit-content !important;
}

#carouselExampleDark {
    height: 460px !important;
    width: 810px !important;
}

.carousel-item img {
    height: 450px !important;
    width: 800px !important;
    object-fit: contain !important;
}

#currentApply {
    --bs-btn-color: #fff !important;
    --bs-btn-bg: #5302FE !important;
    --bs-btn-border-color: #5302FE !important;
    --bs-btn-hover-color: #5302FE !important;
    --bs-btn-hover-bg: white !important;
    --bs-btn-hover-border-color: #5302FE !important;
}
</style>