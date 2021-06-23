package app.core.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import app.core.system.exceptions.CouponSystemException;

@Component
public class LoginManager {

	@Autowired
	private ApplicationContext ctx;

	/**
	 * @param email
	 * @param password
	 * @param clientType
	 * @return type of Client Service per clientType that the client insert - can be
	 *         one of the 3 below: 1. Administrator 2. Company 3. Customer
	 * @throws CouponSystemException while the email/password are wrong
	 */
	public ClientService login(String email, String password, ClientType clientType) throws CouponSystemException {
		if (clientType.equals(ClientType.Administrator)) {
			AdminService adminService = ctx.getBean(AdminService.class);
			if (adminService.login(email, password)) {
				System.out.println("you have been entred as An Administrator Successfully!");
				return adminService;
			}
		}
		if (clientType.equals(ClientType.Company)) {
			CompanyService companyService = ctx.getBean(CompanyService.class);
			if (companyService.login(email, password)) {
				System.out.println("you have been entred successfully as Company Client");
				return companyService;
			}
		}
		if ((clientType.equals(ClientType.Customer))) {
			CustomerService customerService = ctx.getBean(CustomerService.class);
			if (customerService.login(email, password) == true) {
				System.out.println("you have been entred successfully as Customer Client");
				return customerService;
			}
		}
		throw new CouponSystemException("Client Type does NOT Recognized");

	}

}
