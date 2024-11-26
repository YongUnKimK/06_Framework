package edu.kh.project.member.model.service;

import java.util.List;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	
	
	/** 로그인 서비스
	 * @param inputMember
	 * @return loginMember
	 * @throws Exception 
	 */
	Member login(Member inputMember) throws Exception;

	
	
	/** 이메일 중복검사 서비스
	 * @param memberEmail
	 * @return
	 * @author ~_~
	 */
	int checkEmail(String memberEmail);



	/** 멤버 닉네임 중복검사 서비스
	 * @param memberNickname
	 * @return
	 */
	int checkNickname(String memberNickname);



	/** 멤버 회원가입 써삐스
	 * @param inputMember
	 * @param memberAddress
	 * @return result
	 */
	int signup(Member inputMember, String[] memberAddress);



	/** 회원 목록 조회 서비스
	 * @return
	 */
	List<Member> selectMemberList();

	/** 비밀번호 초기화(비동기)
	 * @param inputNo
	 * @return
	 */
	int resetPw(int inputNo);

	/** 탈퇴 회원 복구(비동기)
	 * @param inputNo
	 * @return
	 */
	int restoreMember(int inputNo);


	
}
