package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.Login.ClientType;
import app.core.Login.LoginItem;
import app.core.Login.LoginManager;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import app.core.sessions.Session;
import app.core.sessions.SessionContextManager;
import app.core.system.exceptions.CouponSystemException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private LoginManager loginManager;
	@Autowired
	private SessionContextManager sessionContextManager;

	@PostMapping(path = "/login")
	public LoginItem login(@RequestParam String userEmail, @RequestParam String userPass,
			@RequestParam ClientType clientType) {
		LoginItem loginItem = new LoginItem();
		Session currSession = sessionContextManager.createSession();
		try {
			switch (clientType) {
			case Administrator:
				AdminService adminService = (AdminService) loginManager.login(userEmail, userPass, clientType);
				loginItem.setClientName("Admin");
				currSession.setAttribute("service", adminService);
				break;
			case Company:
				CompanyService companyService = (CompanyService) loginManager.login(userEmail, userPass, clientType);
				loginItem.setClientName(companyService.getCompanyDetails().getName());
				currSession.setAttribute("service", companyService);
				break;
			case Customer:
				CustomerService customerService = (CustomerService) loginManager.login(userEmail, userPass, clientType);
				loginItem.setClientName(customerService.getCustomerDetails().getFirstName());
				currSession.setAttribute("service", customerService);
				break;
			default:
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						"Login Failed, Client Type does NOT Recognaized! :(");
			}

			loginItem.setToken(currSession.token);
			return loginItem;
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

}
