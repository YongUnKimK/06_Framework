package edu.kh.project.webSocket.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpSession;

/** SessionHandshakeInterceptor
 *  
 *  WebSocketHandler 가 동작하기 전 / 후에
 *  연결된 클라이언트 세션을 가로채는 동작을 작성할 클래스
 *  
 */
@Component // bean등록
public class SessionHandshakeInterceptor implements HandshakeInterceptor {

	// 핸들러 동작 전에 수행되는 메서드

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		//	ServerHttpResponse : HttpServletRequest 의 부모 인터페이스
		//	ServerHttpRequest  : HttpServletResponse의 부모 인터페이스
		//	attributes : 해당 맵에 세팅된 속성(데이터)는 다음에 동작할
		//			Handler 객체에게 전달됨
		//			HandshakeInterceptor -> Handler 데이터 전달하는 역할
		
		// instanceof  : 
		// request가 참조하는 객체가 ServletServerHttpRequest로 다운캐스팅이 가능한가?
		if(request instanceof ServletServerHttpRequest) {
			
			// 다운캐스팅
			ServletServerHttpRequest servletRequest= (ServletServerHttpRequest)request;
			
			// 웹소켓 동작을 요청한 클라이언트의 세션을 얻어줌
			HttpSession session =  servletRequest.getServletRequest().getSession();
			
			// 가로챈 세션을 Handler에 전달할 수 있게 값 세팅
			attributes.put("session", session);
			
		}
		
		
		return true; // 가로채기 진행 여부 : true로 작성해야 세션을 가로채서 handler 에게 전달 가능
	}

	// 핸들러 동작 후에 수행되는 메서드
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			@Nullable Exception exception) {
		// TODO Auto-generated method stub
		
	}
	
}

// Handshake : 클라이언트와 서버가 WebSocket 연결을 수립하기 위해 HTTP 프로토콜을 통해 수행하는 초기단계.
// 			   -> 기존 HTTP 연결을 WebSocket 연결로 변경


