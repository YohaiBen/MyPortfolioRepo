package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.services.AdminService;
import app.core.sessions.Session;
import app.core.sessions.SessionContextManager;
import app.core.system.exceptions.CouponSystemException;

@RestController
@CrossOrigin()
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private SessionContextManager sessionContext;

	private AdminService isAdminSessionExists(String theToken) {
		try {
			Session session = this.sessionContext.getSession(theToken);
			AdminService adminService = (AdminService) session.getAttribute("service");
			return adminService;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NO session, you need to login as an Admin!");
		}
	}

	@PostMapping(path = "/add/company")
	public ResponseEntity<?> addCompany(@RequestHeader String token, @RequestBody Company company) {
		try {
			Company addedCompany = isAdminSessionExists(token).addCompany(company);
			return ResponseEntity.status(HttpStatus.OK).body(addedCompany);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@PutMapping(path = "/update/company")
	public ResponseEntity<?> updateCompany(@RequestHeader String token, @RequestParam int companyId,
			@RequestBody Company company) {
		try {
			isAdminSessionExists(token).updateCompany(company, companyId);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Company with ID: " + companyId + " has been Updated successfully");
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@DeleteMapping(path = "/delete/company")
	public ResponseEntity<?> deleteCompany(@RequestHeader String token, @RequestParam int companyId) {
		try {
			isAdminSessionExists(token).deleteCompany(companyId);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Comapny with ID: " + companyId + " has been Deleted successfully");
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@PostMapping(path = "/add/customer")
	public ResponseEntity<?> addCustomer(@RequestHeader String token, @RequestBody Customer customer) {
		try {
			Customer addedCustomer = isAdminSessionExists(token).addCustomer(customer);
			return ResponseEntity.status(HttpStatus.OK).body(addedCustomer);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@PutMapping(path = "/update/customer")
	public ResponseEntity<?> updateCustomer(@RequestHeader String token, @RequestParam int customerId,
			@RequestBody Customer customer) {
		try {
			isAdminSessionExists(token).updateCustomer(customer, customerId);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Customer with ID: " + customerId + " has been Updated successfully");
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@DeleteMapping(path = "/delete/customer")
	public ResponseEntity<?> deleteCustomer(@RequestHeader String token, @RequestParam int customerId) {
		try {
			isAdminSessionExists(token).deleteCustomer(customerId);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Customer with ID: " + customerId + " has been Deleted successfully");
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/company")
	public ResponseEntity<?> getOneCompany(@RequestHeader String token, @RequestParam int companyId) {
		try {
			Company company = isAdminSessionExists(token).getOneCompany(companyId);
			return ResponseEntity.status(HttpStatus.OK).body(company);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}

	@GetMapping(path = "/getAll/companies")
	public ResponseEntity<?> getAllCompanies(@RequestHeader String token) {
		try {
			List<Company> CompaniesList = isAdminSessionExists(token).getAllCompanies();
			return ResponseEntity.status(HttpStatus.OK).body(CompaniesList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/customer")
	public ResponseEntity<?> getOneCustomer(@RequestHeader String token, @RequestParam int customerId) {
		try {
			Customer customer = isAdminSessionExists(token).getOneCustomer(customerId);
			return ResponseEntity.status(HttpStatus.OK).body(customer);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}

	@GetMapping(path = "/getAll/customers")
	public ResponseEntity<?> getAllCustomers(@RequestHeader String token) {
		try {
			List<Customer> customersList = isAdminSessionExists(token).getAllCustomers();
			return ResponseEntity.status(HttpStatus.OK).body(customersList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}

}
