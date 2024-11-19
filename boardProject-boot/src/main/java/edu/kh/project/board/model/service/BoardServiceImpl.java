package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.Pagination;
import edu.kh.project.board.model.mapper.BoardMapper;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor=Exception.class)
@RequiredArgsConstructor // final달린 객체에게 의존성자동주입
public class BoardServiceImpl implements BoardService {

	private final BoardMapper mapper;


	// 특정 게시판 지정된 페이지 목록 조회
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		
		
		int listCount = mapper.getListCount(boardCode);
		
		Pagination pagination = new Pagination(cp, listCount);
				
	
		int limit = pagination.getLimit() ; //  10개
		int offset = (cp - 1 ) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);

		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		// 4. 목록 조회 결과 + Pagination 객체를 Map으로 묵음
		Map<String, Object> map = new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		// 5. 결과 반환
		
		
		return map;
	}
	
	// 게시판 종류 조회.
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		
		
		return mapper.selectBoardTypeList(); 
	}

	//게시글 상세 조회
	@Override
	public Board selectOne(Map<String, Integer> map) {
		
		// 여러 SQL을 실행하는 방법
		// 1. 하나의 service 메서드에서 여러 Mapper 메서드를 호출하는 방법
		
		// 2. 수행하려는 SQL이 
		// 		1) 모두 SELECT 이면서
		// 		2) 먼저 조회된 결과 중 일부를 이용해서
		// 		   나중에 수행되는 SQL의 조건으로 삼을 수 있는 경우
		// -> Mybatis의 <resultMap>, <collection> 태그를 이용해서
		// Mapper 메서드 1회 호출로 여러 SELECT 한번에 수행 가능
		
		return mapper.selectOne(map);
	}

	// 게시글 좋아요 체크 / 해제 ..
	@Override
	public int boardLike(Map<String, Integer> map) {
	
		int result = 0;
		
		// 1. 좋아요가 체크된 상태인 경우 ( likeCheck == 1 )
		// => BOARD_LIKE 테이블에 DELETE 
		if(map.get("likeCheck") == 1) {
			result = mapper.deleteBoardLike(map);
		} else {
			
		// 2. 좋아요가 해제된 상태인 경우 ( likeCheck == 0)
		// -> BOARD_LIKE 테이블에 INSERT..
			result = mapper.insertBoardLike(map);
			
		}	
		
		// 3. 다시 해당 게시글의 좋아요 개수 조회해서 반환.
		if(result > 0) {
			return mapper.selectLikeCount(map.get("boardNo"));
		}
		
		return -1;
	}
	
	// 조회 수 1 증가
	@Override
	public int updateReadCount(int boardNo) {
	
		// 1. 조회 수 1 증가 ( UPDATE )
		int result = mapper.updateReadCount(boardNo);
				
		// 2. 현재 조회 수 조회
		if(result>0) {
			return mapper.selectReadCount(boardNo);
		}
				
		
		// 실패한 경우 -1 반환
			
		return -1;
	}

	
}
