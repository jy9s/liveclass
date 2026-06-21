package com.jygs.liveclass.event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {
	@Query("""
			SELECT e.eventType, COUNT(e)
			FROM EventLog e
			GROUP BY e.eventType
			""")
	List<Object[]> countByEvnetType();
	
	@Query("""
			SELECT e.userId, COUNT(e)
			FROM EventLog e
			GROUP BY e.userId
			""")
	List<Object[]> countByUser();
}
