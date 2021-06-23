package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import app.core.sessions.Session;
import app.core.sessions.SessionContextManager;
import app.core.system.exceptions.CouponSystemException;

@RestController
@CrossOrigin
@RequestMapping("/api/client")
public class LogoutController {

	@Autowired
	private SessionContextManager sessionContextManager;

	@GetMapping(path = "/logout")
	public String logout(@RequestHeader String token) {
		try {
			Session currSession = sessionContextManager.getSession(token);
			if (currSession != null) {
				sessionContextManager.invalidate(currSession);
				System.out.println("You have Logged out Successfully");
				return "You have logged out successfully, Bye Bye :)";
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"you did NOT perform any operations for a long period - you were disconnected from the system :( ");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Client does NOT recognaized, you are not logged in");
		}
	}
}
