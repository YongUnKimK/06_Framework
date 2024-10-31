package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

// Controller -> Service -> ServiceImpl -> DAO -> Mapper -> **.xml

@Slf4j
@Controller // 요청/ 응답 제어 역할 명시 + Bean 등록.. 
public class MainController {
	
	@Autowired // 등록된 Bean중 같은 타입이거나 상속관계 의존성 주입 ( DI ) 
	private TodoService service;
	
	@RequestMapping("/")
	public String mainPage(Model model) {
		
		log.debug("service : " + service);
		
		
		
		String testTitle = service.testTitle();
		model.addAttribute("testTitle", testTitle);
		//------------------------------------------------
		
		// TB_TODO 테이블에 저장된 전체 할일 목록 조회하기
		// + 완료된 할 일 개수
		
		Map<String, Object> map = service.selectAll();
		
		// map에 담긴 내용 추출하기
		List<Todo> todoList = (List<Todo>)map.get("todoList");
		int completeCount = (int)map.get("completeCount");
		
		model.addAttribute("todoList",todoList);
		model.addAttribute("completeCount",completeCount);
		
		// 접두사 : classpath:/templates/
		// 접미사 : .html		
		//todoNo가 1인 todo의 제목 조회하여 request scope에 추가
		return "common/main"; //forward
	}
}
