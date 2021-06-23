package app.core.sessions;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.core.system.exceptions.CouponSystemException;

@Component
public class SessionContextManager {

	@Autowired
	private ApplicationContext ctx;

	private Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

//	@Value("${session.remove.expired.period:20}")
//	private int removeExpiredPeriod; // time between each removal task run

	private boolean isSessionExpired(Session session) {
		return System.currentTimeMillis() - session.getLastAccessed() > session.getMaxInactiveInterval();
	}

	// method that remove the session with the exact token
	public void invalidate(Session session) {
		sessionMap.remove(session.token);
	}

	public Session createSession() {
		Session session = ctx.getBean(Session.class);
		sessionMap.put(session.token, session);
		return session;

	}

	public Session getSession(String token) throws CouponSystemException {
		Session session = sessionMap.get(token);
		if (session != null) {
			if (!isSessionExpired(session)) {
				session.resetLastAccessed();
				return session;

			} else {
				invalidate(session);
				return null;

			}
		}
//		throw new CouponSystemException("sorry you are NOT logged in");
		return null;
	}

	@Scheduled(initialDelay = 10000L, fixedDelayString = "${session.context.activate.task.time}")
	private void initRemoveExpiredSessionsTask() {
		System.out.println(new Date());
		for (String token : sessionMap.keySet()) {
			Session currSession = sessionMap.get(token);
			if (isSessionExpired(currSession)) {
				invalidate(currSession);
				System.out.println("****session has been removed*****");
			}
		}

	}

}
