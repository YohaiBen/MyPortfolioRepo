package app.core.sessions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.core.services.ClientService;

@Component
@Scope("prototype")
public class Session {

	private Map<String, Object> attributes = new HashMap<String, Object>();
	// token and token's maximum length=============
	public final String token;
	private static final int TOKEN_MAX_LENGTH = 15;
	// =============================================
	private long lastAccessed;
	@Value("${session.max.inactive.interval:2}")
	private long maxInactiveInterval; // miliseconds

	{

		this.token = UUID.randomUUID().toString().replace("-", "").substring(0, TOKEN_MAX_LENGTH);
//		Long x = (long) (Math.random() * 1203004034);
//		this.token = x.toString();
		resetLastAccessed();

	}

	public void resetLastAccessed() {
		this.lastAccessed = System.currentTimeMillis();
	}

	@PostConstruct
	private void init() {
		maxInactiveInterval = TimeUnit.MINUTES.toMillis(maxInactiveInterval);
	}

	// **********************************************************
	// ********************************************************************
	public void setAttribute(String attrName, ClientService clientService) {
		attributes.put(attrName, clientService);
	}

	public Object getAttribute(String attrName) {
		return attributes.get(attrName);
	}

	// ==================================================================
	public long getLastAccessed() {
		return lastAccessed;
	}

	public long getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

}
