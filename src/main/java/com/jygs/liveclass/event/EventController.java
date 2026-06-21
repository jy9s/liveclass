package com.jygs.liveclass.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EventController {

	@Autowired
	private EventService eventService;
	
	@GetMapping("/event-save")
    @ResponseBody
	public String createEvent() {
        eventService.createEvents(100);
        return "저장 완료";
	}
	
	@GetMapping("/view/event-type")
	@ResponseBody
	public List<Object[]> eventTypeStats() {
	    return eventService.getEventTypeCount();
	    
	}
	
	
	@GetMapping("/view/user")
	@ResponseBody
	public List<Object[]> userStats() {
		return eventService.getUserCount();
		
	}
	
	
	
}
