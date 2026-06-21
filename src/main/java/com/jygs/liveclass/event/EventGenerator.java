package com.jygs.liveclass.event;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Component;



@Component
public class EventGenerator {
	
	private final Random random = new Random();
	
	public EventLog generator() {
		
		EventLog event = new EventLog();
		
		//type 랜덤으로 받기 위해 EventType에 길이만큼 숫자 발생해서 값 가져옴.
		EventType type = EventType.values()[random.nextInt(EventType.values().length)];		
		event.setEventType(type);
		
		// userId 1~100 사이로 생성
		event.setUserId((long)random.nextInt(100)+1);
		
		//발생 시간 설정
		event.setCreatedAt(LocalDateTime.now().minusHours(random.nextInt(24)));
		
		
		//type에 따라서 필요한 컬럼에 저장.
		switch(type) {
		
		case PAGE_VIEW:
			
			event.setPageUrl("/page/"+random.nextInt(10));
			break;
		
		case PURCHASE:
			
			event.setProductId((long)random.nextInt(1000)+1);
			event.setAmount(random.nextInt(100000));
			
			break;
		
		case ERROR:
			
			event.setErrorCode("ERR_" + random.nextInt(10));
						
			break;		
		
		}
		
		return event;
	}
	
}
