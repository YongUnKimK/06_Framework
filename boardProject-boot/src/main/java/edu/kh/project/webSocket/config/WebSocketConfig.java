package edu.kh.project.webSocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import edu.kh.project.webSocket.handler.ChattingWebSocketHandler;
import edu.kh.project.webSocket.handler.TestWebSocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration   // 서버 실행 시 작성된 메서드를 모두 수행
@EnableWebSocket // 웹소켓 활성화 설정
@RequiredArgsConstructor // 필드에 final => autowired
public class WebSocketConfig implements WebSocketConfigurer {

	// Bean으로 등록된 SessionHandshakeInterceptor가 주입됨
	private final HandshakeInterceptor handshakeInterceptor ;
	
	// 웹소켓 처리 동작이 작성된 객체 의존성 주입
	private final TestWebSocketHandler testWebSocketHandler ;
	
	// 채팅관련 웹소켓 처리동작이 작성된 객체 의존성 주입
	private final ChattingWebSocketHandler chattingWebSocketHandler; 

	// 웹소켓 핸들러를 등록하는 메서드
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {	
		// addHandler( 웹소켓 핸들러, 웹소켓 요청 주소 )
		
		registry.addHandler(testWebSocketHandler, "/testSock")
		// ws://localhost/testSock 으로 클라이언트가 요청을 하면
		// testWebSocketHandler가 처리하도록 등록함
		.addInterceptors(handshakeInterceptor)		
		// 클라이언트 변경 시 HttpSession을 가로채 핸들러에게 전달하는 hadnshakeInterceptor 등록
		.setAllowedOriginPatterns("http://localhost/",
								"http://127.0.0.1/",
								"http://192.168.50.252")
		// 웹소켓 요청이 허용되는 ip/ 도메인 지정 ( 로컬호스트 / 루프백 / 본인ip )
		.withSockJS(); // SockJs 지원
		
				
		// ------------------------------------------------ 채팅웹소켓
		
		registry.addHandler(chattingWebSocketHandler, "/chattingSock")
		.addInterceptors(handshakeInterceptor)
		.setAllowedOriginPatterns("http://localhost/",
								"http://127.0.0.1/",
								"http://192.168.50.252") // 타인접속하려면 필수입력
		.withSockJS();
		
		
	}
}
