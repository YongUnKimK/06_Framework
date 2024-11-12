package edu.kh.project.mypage.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.mypage.model.dto.UploadFile;

public interface MyPageService {

	/** 회원 정보 수정 서비스
	 * @param inputMember
	 * @param memberAddress
	 * @return result
	 */
	int updateInfo(Member inputMember, String[] memberAddress);

	 
	
	/** 회원 비밀번호 변경 서비스
	 * @param paramMap
	 * @param memberNo
	 * @return
	 */
	int changePw(Map<String, Object> paramMap,int memberNo);



	/** 회원 탈퇴 서비스
	 * @param memberPw
	 * @param memberNo
	 * @return result
	 */
	int secession(String memberPw, int memberNo);



	/** 파일 업로드a 테스트 1....
	 * @param uploadFile
	 * @return path
	 */
	String fileUpload1(MultipartFile uploadFile) throws Exception;



	/** 파일 업로드 테스트 2 ( + DB 저장 )  ..
	 * @param uploadFile
	 * @param memberNo
	 * @return result
	 */
	int fileUpload2(MultipartFile uploadFile, int memberNo) throws Exception;



	/** 파일 목록 조회
	 * @param memberNo
	 * @return list
	 * @throws Exception 
	 */
	List<UploadFile> fileList(int memberNo) throws Exception;



	/** 여러 파일 업로드 서비스
	 * @param aaaList : 
	 * @param bbbList :
	 * @param memberNo :
	 * @return
	 * @throws Exception 
	 */
	int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws Exception;



	/** 프로필 이미지 변경 서비스
	 * @param profileImg
	 * @param loginMember
	 * @return
	 */
	int profile(MultipartFile profileImg, Member loginMember) throws Exception;

	

}