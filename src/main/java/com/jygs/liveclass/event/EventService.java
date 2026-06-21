package com.jygs.liveclass.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class EventService {
	
	@Autowired
	private EventGenerator generator;
	
	
	@Autowired
	private EventLogRepository repository;
	
	
	@PostConstruct
	public void init() {
		createEvents(100);
	}
	
	
	public void createEvents(int count) {
		for(int i=0;i<count;i++) {
			EventLog event = generator.generator();
			repository.save(event);
		}
	}
	
	public List<Object[]> getEventTypeCount() {
	    return repository.countByEvnetType();
	}
	public List<Object[]> getUserCount() {
		return repository.countByUser();
	}
	
	
}
