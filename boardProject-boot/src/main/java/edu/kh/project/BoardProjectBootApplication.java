package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
// Spring Security에서 기본 제공하는 로그인 페이지를 이용 안하겠다!
//	(암호화할때사용)
public class BoardProjectBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectBootApplication.class, args);
		
		
		
	}

}
