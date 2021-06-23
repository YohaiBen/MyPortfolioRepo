package app.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import app.core.sessions.Session;
import app.core.sessions.SessionContextManager;
import app.core.system.exceptions.CouponSystemException;

public class LoginFilter implements Filter {

	private SessionContextManager sessionContextManager;

	public LoginFilter(SessionContextManager sessionContext) {
		super();
		this.sessionContextManager = sessionContext;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		// we are going to intercept the incoming request
		String token = req.getHeader("token");
		System.out.println("-------------- " + token + " --------------");
		try {

			if (req.getMethod().equals("OPTIONS")) {
				System.out.println("preflight request");
				chain.doFilter(req, response);
				return;
			}
			if (token != null && sessionContextManager.getSession(token) != null) {
				System.out.println("**session is OK** -token: " + token);
				chain.doFilter(req, response);
				return;
			}
			// if we are here there is no session =============================
			HttpServletResponse res = (HttpServletResponse) response;
			System.out.println("---------Error----------");
			res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
			res.setStatus(HttpStatus.UNAUTHORIZED.ordinal());
			res.sendError(HttpStatus.UNAUTHORIZED.value(), "you cannot perform operrations, You are NOT logged in!");
		} catch (CouponSystemException e) {
			e.printStackTrace();
		}
	}

}
