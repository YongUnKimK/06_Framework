package edu.kh.project.member.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Service // 비즈니스 + Bean등록
//@Transactional // 트랜잭션 처리 (기본 - rollbackFor=RuntimeException.class )
@Slf4j
public class MemberServiceImpl implements MemberService {

	// 등록된 Bean 중에서 같은 타입 or 상속관계인 Bean
	@Autowired // 의존성 주입 ( DI )
	private MemberMapper mapper;
	
	// Bcrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember) throws Exception {
		
		// 암호화 진행 
		
		//bcrypt.encode(문자열) : 문자열을 암호화하여 반환
		String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		log.debug("becryptPassword :" + bcryptPassword);
		
		// bcrypt.matches(평문, 암호화) 평문과 암호화가 일치하면 true, 아니면 false
		
		//boolean bcrypt.matches ( 평문, 암호화된문장) 
		
		//log.debug("result : "  + result );
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 조회
		
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 null인 경우
		if(loginMember == null) return null;
		
		// 3. 입력 받은 비밀번호 ( 평문 : inputMember.getMemberPw() ) 와 
		// 	  암호화된 비밀번호 두 비밀번호(loginMember.getMembPw())
		// 	  가 일치하는지 확인 !
		
		// 일치하지 않으면
		if( !bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
			return null;			
		}
		
		// 로그인 결과에서 비밀번호 제거
		loginMember.setMemberPw(null);
		
		return loginMember;
	}

	// 이메일 중복 검사 서비스
	@Override
	public int checkEmail(String memberEmail) {
		
		return mapper.checkEmail(memberEmail);
	}

	// 닉네임 중복 검사 서비스
	@Override 
	public int checkNickname(String memberNickname) {
		
		return mapper.checkNickname(memberNickname);
	}

	/**
	 * 회원가입 서비스
	 */
	@Override
	public int signup(Member inputMember, String[] memberAddress) {
		
		// 주소가 입력되지 않으면 
		// inputMember.getMemberAddress() -> ",,"
		// memberAddress -> [,,]
		
		// 주소가 입력된 경우
		if(!inputMember.getMemberAddress().equals(",,")) {
			
			// String.join("구분자" , 배열 ) 
			// => 배열의 모든 요소 사이에 구분자를 추가하여
			// 하나의 문자열로 만들어 반환하는 메서드
			
			String address = String.join("^^^", memberAddress);
			// 04132^^^서울시중구^^^3층,E강의장
			
			// 구분자로 "^^^" 쓴 이유 :
			// -> 주소, 상세주소에 없는 특수문자 작성
			// -> 나중에 마이페이지에서 주소 수정 시 다시 3분할 해야할 때 구분자로 이용할 예정
			// inputMember 주소로 합쳐진 주소를 세팅
			inputMember.setMemberAddress(address);
			
		} else {			
		// 주소가 입력되지 않은 경우
			inputMember.setMemberAddress(null); // null 저장	
		}
		
		// inputMember 안의 memberPw -> 평문	
		// 비밀번호를 암호화하여 inputMember에 세팅
		String encPw = bcrypt.encode(inputMember.getMemberPw()); // 암호화하는과정
		inputMember.setMemberPw(encPw);
		// 회원 가입 매퍼 메서드 호출쓰
		
		return mapper.signup(inputMember);
	}

	// 회원조회 ( 비동기 ) 
	@Override
	@Transactional
	public List<Member> selectMemberList() {
		
		return mapper.selectMemberList();
	}
	
	// 비밀번호 초기화(비동기)
	@Override
	public int resetPw(int inputNo) {

		// pass01! -> 암호화
		String encPw = bcrypt.encode("pass01!");

		Map<String, Object> map = new HashMap<>();
		map.put("inputNo", inputNo);
		map.put("encPw", encPw);

		return mapper.resetPw(map);
	}

		// 탈퇴 회원 복구(비동기)
		@Override
		public int restoreMember(int inputNo) {

			return mapper.restoreMember(inputNo);
		}

	}
