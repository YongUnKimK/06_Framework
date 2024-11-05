console.log("main.js loaded..");

// 이메일, 비밀번호 미작성 시 로그인 막기

const loginForm = document.querySelector("#loginForm") ; // form태그
const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");
const loginPw = document.querySelector("#loginForm input[name='memberPw']");


// #loginForm 이 화면에 존재할 때 (== 로그인 상태가 아닐 때 )
// -> 타임리프에 의해 로그인 되었다면 #loginFrom 요소는 화면에 노출되지 않음
// -> 로그인 상태일 때 loginForm 을 이용한 코드가 수행된다면
// -> 콘솔창에 에러가 발생한다.

if(loginForm != null) {
  
  // 제출 이벤트 발생 시
  loginForm.addEventListener("submit", e => {
    
    // 이메일 미작성
    if(loginEmail.value.trim().length === 0) {
      alert("이메일을 작성해주세요");
      e.preventDefault(); // 기본이벤트(제출) 막기
      loginEmail.focus(); // 초점 이동
      return;
    }

    // 비밀번호 미작성
    if(loginPw.value.trim().length === 0) {
      alert("비밀번호를 입력해주세요");
      e.preventDefault(); // 기본이벤트(제출) 막기
      loginPw.focus(); // 초점 이동
      return;
    }
    

  });
}