package com.jygs.liveclass.event;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="event_log")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	//이벤트를 발생시킨 사용자	
	private Long userId;
	
	//이벤트 종류
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	//이벤트 발생 시간
	private LocalDateTime createdAt;
	
	//PAGE_VIEW 이벤트 전용
	private String pageUrl;
	
	//PURCHASE 이벤트 전용
	private Long productId;
	
	//PURCHASE 이벤트 전용
	private Integer amount;
	
	//ERROR이벤트 전용
	private String errorCode;	
}
