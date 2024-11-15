package edu.kh.project.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;


@Controller
public class MainController {
	
	@Autowired
	private BoardService service;
	
	@RequestMapping("/") // "/" 요청 매핑 
	public String mainPage() {
		
		// 접두사/ 접미사 제외
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		return "common/main";
	}
	
	// LoginFilter -> loginError 리다이렉트
	// -> message 만들어서 메인페이지로 리다이렉트
	@GetMapping("loginError")
	public String loginError(
							RedirectAttributes ra) {
		
		ra.addFlashAttribute("message", "로그인 후 이용해 주십시오");
		
	return "redirect:/";	
	}
		 	
	
	
}
