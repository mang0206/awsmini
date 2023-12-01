function makeMeetingBlock(meeting) {
    // console.log(meeting)
    const div = document.createElement('div');
    div.id = "meeting_div";
    div.addEventListener('click', function() {
      window.location.href = `/meeting/${meeting.idx}`;
    });
  
    const img_div = document.createElement('div');
    img_div.id = "img_div";
  
    const img = document.createElement('img');
    img.id = "meeting_img";
    img.src = "http://localhost:8090" +meeting.imageUrl;
    img_div.appendChild(img);
  
    img.onerror = function() {
      img.src = "http://localhost:8090/images/book.png" 
    }
    const content_div = document.createElement('div');
    content_div.id = "content_div";
  
    
    const info = document.createElement('h5');
    info.id ="Title_Category"
    info.classList.add("meeting-info"); // h5에 class 추가
  
    const category = document.createElement('span');
    category.textContent = meeting.category;
    category.classList.add("meeting-info"); // span에 class 추가
  
    const location = document.createElement('span');
    location.textContent = meeting.location;
    location.id = "location"
    location.classList.add("meeting-info"); // span에 class 추가
  
    info.appendChild(category);
    info.appendChild(document.createTextNode(' ')); // 공백을 추가하여 간격을 만듭니다.
    info.appendChild(location);
  
    const title = document.createElement('h2');
    title.textContent = meeting.title;
    title.classList.add("meeting-info"); // h4에 class 추가
  
    const content_text = document.createElement('h5');
    content_text.textContent = meeting.content;
    content_text.id = "meeting_content";
    content_text.classList.add("meeting-info"); // h5에 class 추가
  
    const recruits = document.createElement('div');
    recruits.textContent = meeting.memberNowRecruits + "/" + meeting.recruits;
    recruits.classList.add("meeting-info", "user-card"); // h6에 class 추가
  
    const create_datetime = document.createElement('div');
    create_datetime.textContent = elapsedTime(meeting.createdDatetime);
    create_datetime.style.color = "gray";
    create_datetime.classList.add("meeting-info", "user-thumb"); // h6에 class 추가
    
    const writer_nickname = document.createElement('div');
    writer_nickname.textContent = meeting.writerNickname;
    writer_nickname.classList.add("meeting-info", "details"); // h6에 class 추가
    
    const wirter_time = document.createElement('div');
    wirter_time.id = "flowchild";
    writer_nickname.classList.add("meeting-info");
    wirter_time.appendChild(create_datetime);
    wirter_time.appendChild(writer_nickname);
  
    const flowdiv = document.createElement('div');
    flowdiv.id="flow"
  
    flowdiv.appendChild(recruits);
    flowdiv.appendChild(wirter_time);
  
    content_div.appendChild(info);
    content_div.appendChild(title);
    content_div.appendChild(content_text);
    content_div.appendChild(flowdiv);
  
    div.appendChild(img_div);
    div.appendChild(content_div);
  
    document.getElementById('info_result').appendChild(div);
  }
  
  function elapsedTime(date) {
    console.log(date)
    const start = new Date(date);
    const end = new Date();
  
    const diff = (end - start) / 1000;
    
    const times = [
      { name: '년', milliSeconds: 60 * 60 * 24 * 365 },
      { name: '개월', milliSeconds: 60 * 60 * 24 * 30 },
      { name: '일', milliSeconds: 60 * 60 * 24 },
      { name: '시간', milliSeconds: 60 * 60 },
      { name: '분', milliSeconds: 60 },
    ];
  
    for (const value of times) {
      const betweenTime = Math.floor(diff / value.milliSeconds);
  
      if (betweenTime > 0) {
        return `${betweenTime}${value.name} 전`;
      }
    }
    return '방금 전';
  }
  
const makeMyinfoBlock = (userObj)=> {
  let result_div = document.getElementById("info_result");
  // let idx = result_div.getAttribute('data-suser');

  result_div.innerHTML = `
  <form action='/mypage/user/{userObj.idx}' method='patch' class='form_group'>
  <input type="hidden" name='idx' value=${userObj.idx}>
  <span class='span_text'>아이디</span> <input type='text' name='id' class='input_box' disabled value = '${userObj.userId}'> <br>
  <span class='span_text'>비밀번호</span> <input class='input_box input_chane_box' type='password' name='password' id='pwd' placeholder = '변경할 비밀번호 입력' required> <br>
  <span class='span_text'>비밀번호 확인</span> <input class='input_box input_chane_box' type='password' name='check_pwd' id='c_pwd' placeholder = '비밀번호 확인' required> <br>
  <span class='span_text'>닉네임</span> <input class='input_box input_chane_box' type='text' name='nickname' placeholder = '변경할 닉네임 입력' required> <br>
  <div class='edit-category'>
      <div class="span-height-div">
          <span class='span_text'>관심 카테고리</span>
      </div>
      <div class="custom-checkbox">
          <label>
              <input type="checkbox" class="custom-checkbox-input" name="category" value="운동">
              <div class="custom-checkbox-text">운동</div>
          </label>
          <label>
              <input type="checkbox" class="custom-checkbox-input" name="category" value="코딩">
              <div class="custom-checkbox-text">코딩</div>
          </label>
          <label>
              <input type="checkbox" class="custom-checkbox-input" name="category" value="요리">
              <div class="custom-checkbox-text">요리</div>
          </label>
          <label>
              <input type="checkbox" class="custom-checkbox-input" name="category" value="게임">
              <div class="custom-checkbox-text">게임</div>
          </label>
          <label>
              <input type="checkbox" class="custom-checkbox-input" name="category" value="애니">
              <div class="custom-checkbox-text">애니</div>
          </label>
          <label>
              <input type="checkbox" class="custom-checkbox-input" name="category" value="영화">
              <div class="custom-checkbox-text">영화</div>
          </label>
      </div>
  </div>
  <span class='span_text'>이메일</span> <input class='input_box' type='text' name='email' disabled value = '${userObj.email}'> <br>
  <span class='span_text'>휴대폰 번호</span> <input class='input_box' type='text' name='phone_num' disabled value = '${userObj.phoneNumber}'> <br>
  <div class='submit_btn'>
      <input type='submit' value='정 보 변 경' id='submit_button' disabled>
  </div> </form>`;
  document.getElementById('pwd').addEventListener('input', validatePassword);
  document.getElementById('c_pwd').addEventListener('input', validatePassword);
}

const makeWithDraw = (userObj) => {
  let result_div = document.getElementById("info_result");

  result_div.innerHTML = `
      <h3> 탈퇴 안내 </h3>

      <h5>사용하고 계신 아이디(${userObj.userId})는 탈퇴할 경우 복구가 불가능합니다. </h5>
      <h5>탈퇴 후 회원정보 및 개인형 서비스 이용기록은 모두 삭제됩니다. </h5>
      <input type="hidden" name='user_pwd' id='user_pwd'value=${userObj.password}>
      <input class='input_box input_chane_box' type='password' id='wd' placeholder = '비밀번호 입력' required> <br>
      <button class='submit_btn' id='wdr-btn' disabled onclick="window.location.href = '/mypage/withdraw'">
          <span id = 'submit_button'>회 원 탈 퇴</span>
      </button>
  `
  document.getElementById('wd').addEventListener('input', withDrawPassword);
}

function validatePassword() {
  let submitButton = document.getElementById('submit_button');

  var passwordValue = document.getElementById('pwd').value;
  var checkPwdValue = document.getElementById('c_pwd').value;

  if (passwordValue === checkPwdValue) {
     submitButton.disabled = false;
  } else {
     submitButton.disabled = true;
  }
}

function withDrawPassword() {
  const button = document.getElementById('wdr-btn');
  console.log(button)
  let passwordValue = document.getElementById('user_pwd').value;
  var checkPwdValue = document.getElementById('wd').value;

  console.log(passwordValue + "///" + checkPwdValue + "///" + typeof(passwordValue) + "///"+ typeof(checkPwdValue));
  if (passwordValue === checkPwdValue) {
     button.disabled = false;
  } else {
     button.disabled = true;
  }
}

export { makeMeetingBlock, makeMyinfoBlock, makeWithDraw }