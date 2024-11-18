

// 쿠키에 저장된 이메일 input 창에 뿌려놓기
// 로그인이 안된 경우에 수행

// 쿠키에서 매개변수로 전달받은 key가 일치하는 value 얻어오는 함수
const getCookie = (key) => {
  const cookies = document.cookie; // "K=V; K=V;  ... 

  // console.log(cookies) => saveId=user01@kh.or.kr

  // cookies 문자열을 배열 형태로 변환  - split : 문자열 기준으로 쪼개서 배열 형태로 반환
  const cookieList = cookies.split("; ") // ["k=v", "k=v" ... ]
    .map(el => el.split("=")); // ["K", "V"].. 

  //  console.log(cookieList); // ['saveId', 'user01@kh.or.kr']
  // ['testkey' , 'testvalue']
  // 배열.map(함수) : 배열의 각 요소를 이용해 함수 수행 후
  //                 결과값으로 새로운 배열을 만들어서 반환

  // 배열 => 객체로 변환 ( 그래야 다루기 쉽다 )

  const obj = {}; // 비어있는 객체 선언

  for (let i = 0; i < cookieList.length; i++) {
    const k = cookieList[i][0]; // 각 인덱스의 key값을 가져옴
    const v = cookieList[i][1]; // 각 인덱스의 value 값을 가져옴
    obj[k] = v; // obj[("saveId")] = 'user01@kh.or.kr"; 
  }

  //console.log(obj); saveId "user01@kh.or.kr" ,testky "testvalue"

  return obj[key]; // 매개변수로 전달받은 key와
  // obj 객체에 저장된 key가 일치하는 요소의 value값 반환

}

// 이메일 작성 input 태그 요소
const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");

if (loginEmail != null) { // 로그인 창에 이메일 input 태그가 화면에 존재할 때

  // 쿠키 중 key 값이 "saveId" 인 요소의 value 얻어오기
  const saveId = getCookie("saveId");  // 이메일 또는 undefined

  // saveId 값이 있을 경우
  if (saveId != undefined) {
    loginEmail.value = saveId; // 쿠키에서 얻어온 이메일 값을 input 요소의 value에 세팅

    // 아이디 저장 체크박스에 체크해두기
    document.querySelector("input[name='saveId']").checked = true;
  }


}






// 이메일, 비밀번호 미작성 시 로그인 막기

const loginForm = document.querySelector("#loginForm"); // form태그

const loginPw = document.querySelector("#loginForm input[name='memberPw']");


// #loginForm 이 화면에 존재할 때 (== 로그인 상태가 아닐 때 )
// -> 타임리프에 의해 로그인 되었다면 #loginFrom 요소는 화면에 노출되지 않음
// -> 로그인 상태일 때 loginForm 을 이용한 코드가 수행된다면
// -> 콘솔창에 에러가 발생한다.

if (loginForm != null) {

  // 제출 이벤트 발생 시
  loginForm.addEventListener("submit", e => {

    // 이메일 미작성
    if (loginEmail.value.trim().length === 0) {
      alert("이메일을 작성해주세요");
      e.preventDefault(); // 기본이벤트(제출) 막기
      loginEmail.focus(); // 초점 이동
      return;
    }

    // 비밀번호 미작성
    if (loginPw.value.trim().length === 0) {
      alert("비밀번호를 입력해주세요");
      e.preventDefault(); // 기본이벤트(제출) 막기
      loginPw.focus(); // 초점 이동
      return;
    }


  });
}


//------------------------------------------비동기과제시작----------------------------

// 조회 버튼
const selectMemberList = document.querySelector("#selectMemberList");

// tbody
const memberList = document.querySelector("#memberList");

// td 요소를 만들고 text 추가 후 반환
const createTd = (text) => {
  const td = document.createElement("td");
  td.innerText = text;
  return td; // <td>1</td> // <td>user01@kh.or.kr</td> // <td>유저일</td> // <td>N</td>
}

// 조회 버튼 클릭 시
selectMemberList.addEventListener("click", () => {

  // 1) 비동기로 회원 목록 조회
  //   (포함될 회원 정보 : 회원번호, 이메일, 닉네임, 탈퇴여부)

  fetch("/member/selectMemberList")
    .then(response => response.json()) // JSON.parse(response)
    .then(list => {

      console.log(list);

      // 이전 내용 삭제
      memberList.innerHTML = "";

      // tbody에 들어갈 요소를 만들고 값 세팅 후 추가
      list.forEach((member, index) => {
        // member : 현재 반복 접근 중인 요소
        // index : 현재 접근중인 인덱스

        // tr 만들어서 그 안에 td 만들고, append후
        // tr을 tbody에 append       
        const keyList = ['memberNo', 'memberEmail', 'memberNickname', 'memberDelFl'];

        const tr = document.createElement("tr");

        keyList.forEach(key => tr.append(createTd(member[key])));
        
        // tbody 자식으로  tr 추가
        memberList.appendChild(tr);
      });
    })
});


// 조회



/* 특정 회원 비밀번호 초기화 */
const resetMemberNo = document.querySelector("#resetMemberNo");
const resetPw = document.querySelector("#resetPw");

resetPw.addEventListener("click", () => {

	// 입력 받은 회원 번호 얻어오기
	const inputNo = resetMemberNo.value;

	if (inputNo.trim().length == 0) {
		alert("회원 번호 입력해주세요");
		return;
	}

	fetch("/member/resetPw", {
		method: "PUT", // PUT : 수정 요청 방식
		headers: { "Content-Type": "application/json" },
		body: inputNo
	})
		.then(resp => resp.text())
		.then(result => {
			// result == 컨트롤러로부터 반환받아 TEXT 로 파싱한 값
			// "1", "0"

			if (result > 0) {
				alert("초기화 성공!");

			} else {
				alert("해당 회원이 존재하지 않습니다 :-(");

			}
		});
});



// -------------------------------------------------------

/* 특정 회원 탈퇴 복구 */
const restorationBtn = document.querySelector("#restorationBtn");
const restorationMemberNo = document.querySelector("#restorationMemberNo");


restorationBtn.addEventListener("click", () => {
	// 입력 받은 회원 번호 얻어오기
	const inputNo = restorationMemberNo.value;

	if (inputNo.trim().length == 0) {
		alert("회원 번호 입력해주세요");
		return;
	}

	fetch("/member/restoreMember", {
		method: "PUT", // PUT : 수정 요청 방식
		headers: { "Content-Type": "application/json" },
		body: inputNo
	})
		.then(resp => resp.text())
		.then(result => {
			// result == 컨트롤러로부터 반환받아 TEXT 로 파싱한 값
			// "1", "0"
			
			if (result > 0) {
				alert("복구 성공!");

			} else {
				alert("해당 회원이 존재하지 않습니다 :-(");

			}
		});
});