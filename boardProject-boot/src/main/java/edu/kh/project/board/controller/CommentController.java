package edu.kh.project.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// @Controller  컨트롤러 명시 + Bean등록
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

/* @RestController (REST API 구축을 위해서 사용하는 컨트롤러 )
 * 
 * ==> @Controller (요청 / 응답 제어 역할 명시 + bean 등록 )
 * 		+ @ResponseBody (  응답 본문으로 응답데이터 자체를 반환 )
 * 
 * -> 모든 요청에 대한 응답을 응답 본문으로 반환하는 컨트롤러
 * 
 */

	// fetch -  비동기 요청
	// "/comment" 요청이 오면 해당 컨트롤러에서 잡아서 처리함
	// @ResponseBody를 매번 메서드에 추가..
	
	private final CommentService service;
	
	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	@GetMapping("") // comment로 시작하는 get요청을 다받음
	public List<Comment> select(@RequestParam("boardNo") int boardNo) {
		
		// HttpMessageConverter가
		// List -> JSON(문자열)로 변환 -> JS
		
		
		return service.select(boardNo);
	}
	
	/** 댓글 / 답글 등록
	 * @param comment
	 * @return
	 */
	@PostMapping("")
	public int insert(@RequestBody Comment comment) {
			
		return service.insert(comment);
	}
	
	
	/** 댓글 삭제하는 메서드
	 * @param commentNo
	 * @return
	 */
	@DeleteMapping("")
	public int delete(@RequestBody int commentNo ) {
		
		return service.delete(commentNo);
	}
	
	
	 
	/** 댓글 수정하는 메서드
	 * @param comment
	 * @return
	 */
	@PutMapping("")
	public int update(@RequestBody Comment comment) {
		
		return service.update(comment);
	}
	
	
	
	
	
	
	
	
	

}