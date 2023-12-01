import { createRouter, createWebHistory } from 'vue-router'

import Home from '@/pages/Mainrounge.vue'
import meeting from '@/pages/Meeting.vue'
import mypage from '@/pages/Mypage.vue'
import meetingView from '@/pages/MeetingView.vue'
//Board
import Free from '@/pages/Board/Free.vue'
import Notice from '@/pages/Board/Notice.vue'
import Detail from '@/pages/Board/Detail.vue'
import Write from '@/pages/Board/Write.vue'
import Update from '@/pages/Board/Update.vue'
//User
import Register from '@/pages/User/Register.vue'
import Login from '@/pages/User/Login.vue'
import createMeeting from '@/pages/CreateMeeting.vue'
import updateMeeting from '@/pages/UpdateMeeting.vue'

const router = createRouter({
    history: createWebHistory(),
    routes : [
        { path: '/', component: Home },
        { path: '/meetings', component: meeting },
        // { path: '/join', component: join },
        { path: '/mypage', component: mypage},
        { path: '/meeting/:idx', component: meetingView},
        
        { path: '/boards/type/free', component: Free },
        { path: '/boards/type/notice', component: Notice },
        { path: '/boards/:idx', component: Detail },
        { path: '/boards/:type/write', component: Write},
        { path: '/user/register', component:Register},
        { path: '/user/login', component: Login },
        { path: '/meeting/goInsertMet', component: createMeeting },
        { path: '/meeting/goUpdateMet/:meeting_idx', component: updateMeeting },
        { path: '/boards/update/:idx', component:Update},
        // { path: '/boards/type/notice', component: Notice },s
        // { path: '/boards/:idx', component:BoardDetail},
    ]
})

export default router;