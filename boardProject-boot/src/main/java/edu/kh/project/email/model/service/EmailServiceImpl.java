package edu.kh.project.email.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.email.model.mapper.EmailMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
// 트랜잭션을 메서드에서씀
public class EmailServiceImpl implements EmailService {

	// 필드 의존성 주입될 객체들
	private final EmailMapper mapper;
	
	// JavaMailSender : 실제 메일 발송을 담당하는 객체(EmailConfig 설정이 적용된 객체)
	private final JavaMailSender mailSender;
	
	// SpringTemplateEngine : 타임리프 ( 템플릿 엔진 ) 을 이용해서 html코드 -> 자바 코드로 변환
	private final SpringTemplateEngine templateEngine;

	@Override
	public String sendEmail(String htmlName, String email) {
		
		// 1. 인증키 생성 및 DB 저장 준비
		String authKey = createAuthKey();
		log.debug("authKey : " + authKey); // authKey : 26dbca
		
		Map<String, String> map = new HashMap<>();
		map.put("authKey", authKey);
		map.put("email", email);
		
		log.debug("map : " + map); //  map : {authKey=26dbca, email=user01@kh.or.kra}
		
		storeAuthKey(map);
		
		// DB 저장 시도 - 실패 시 해당 메서드 종료
		if(!storeAuthKey(map)) {
			return null;
		}
		
		// 이메일 발송 코드 작성
		// 2. DB 저장이 성공한 경우에만 메일 발송 시도
		// jakarta.mail.internet.MimeMessage 
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		// 메일 발송 시 사용하는 객체
		
		// 메일 발송을 도와주는 Helper 클래스 (파일첨부 , 템플릿 설정 등 쉽게 처리 ) 
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			// - mimeMessage : MimeMessage 객체로, 이메일 메시지의 내용을 담고있음
			// 					이메일의 본문, 수신자 정보, 제목 등을 포함
			// - true : 파일 첨부를 사용 여부 지정 ( 파일 첨부 및 내부 이미지 삽입 가능 ) 
			// - "UTF-8" : 이메일 내용이 "UTF-8" 인코딩으로 전송
			
			// 메일 기본 정보 설정
			helper.setTo(email); // 받는 사람 ( 수신자 ) 
			helper.setSubject(" [boardProject] 회원 가입 인증번호 입니다. "); // 제목
			helper.setText( loadHtml(authKey, htmlName) , true ); // HTML 내용 설정 -> 마지막에 true안하면 false되서 텍스트값출력
			
			// 인증번호입니다 : abgsd2 텍스트만 보내고 싶은 경우~
			// helper.setText("인증번호입니다 : " + authKey );
			
			// 메일에 이미지 첨부 ( 로고 )
			helper.addInline("logo", new ClassPathResource("static/images/logo.jpg"));
			
			// 실제 메일 발송
			mailSender.send(mimeMessage);
			
			return authKey; // 모든 작업 성공 시 인증키 반환
			
			
		} catch (MessagingException e) {
			
			e.printStackTrace();
			return null; // 메일 발송 실패
		}
		
	}
	
	
	// HTML 템플릿에 데이터를 바인딩하여 최종 HTML 생성
	private String loadHtml(String authKey, String htmlName) {
		
		// org.thymeleaf.context.Context
		// Context : Thymeleaf에서 HTML 템플릿에 데이터를 전달하기 위해 사용하는 클래스
		// 
		
		Context context = new Context (); 
		context.setVariable("authKey", authKey);
		
		
		return templateEngine.process("email/" + htmlName, context);
	}

	// 인증키와 이메일을 DB예 저장하는 메서드 
	@Transactional(rollbackFor = Exception.class) // 메서드 레벨에서도 이용 가능
	private boolean storeAuthKey(Map<String, String> map) {
		
		// 1. 기존 이메일에 대한 인증키 update 수행
		int result = mapper.updateAuthKey(map);
				
		// 2. update 실패 ( == 기존 데이터가 없음 ) 시 insert 수행합니다 
		if(result == 0) {
			result = mapper.insertAuthKey(map);
		}
		
		return result > 0; // 성공 여부 반환 ( true / false ) 
	}

	// 인증번호 발급 메서드
	// UUID를 사용하여 인증키 생성
	// ( Universally Unique Identifier : 전 세계에서 고유한 식별자를 생성하기 위한 표준. )
	// 매우 낮은 확률로 중복된느 식별자를 생성
	// 주로 데이터베이스 기본 키, 고유한 식별자를 생성해야 할 때 사용
	private String createAuthKey() {
		
		return UUID.randomUUID().toString().substring(0, 6);
	}


	/**
	 *
	 */
	@Override
	public int checkAuthKey(Map<String, String> map) {
		
		
		return mapper.checkAuthKey(map);
	}
	
	
	
	
}
