package app.core.services;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;
import app.core.system.exceptions.CouponSystemException;

@Service
@Transactional
public class AdminService extends ClientService {

	private String email = "admin@admin.com";
	private String password = "admin";

	public AdminService(CompanyRepository companyRepository, CouponRepository couponRepository,
			CustomerRepository customerRepository) {
		super();
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
		this.customerRepository = customerRepository;

	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		if (email.equalsIgnoreCase(this.email) && password.equalsIgnoreCase(this.password)) {
			return true;

		}
		throw new CouponSystemException("Admin login have been failed/ email or password are wrong");

	}

	/**
	 * A method that allows Client to add a company to the companies Table in the DB
	 * if and only if there is NOT already a company with the same name/email
	 * 
	 * @param company
	 * @throws CouponSystemException
	 */
	public Company addCompany(Company company) throws CouponSystemException {
		List<Company> listByNameOrEmail = companyRepository.findByNameOrEmail(company.getName(), company.getEmail());
		if (listByNameOrEmail != null) {
			throw new CouponSystemException(
					"company cannot added, there is already a company with the same name/email in DB");
		}

		System.out.println("New Company added successfully");
		return companyRepository.save(company);

	}

	public void updateCompany(Company company, int companyId) throws CouponSystemException {
		Company company_fromDB = companyRepository.getCompanyById(companyId);
		if (company_fromDB != null) {
			company_fromDB.setEmail(company.getEmail());
			company_fromDB.setPassword(company.getPassword());
			companyRepository.flush();
			System.out.println("Company has been Updated successfully");
		} else {
			throw new CouponSystemException(
					"Update Company has been Failed, there is NO such a company with id: " + companyId);
		}

	}

	public void deleteCompany(int companyId) throws CouponSystemException {
		Company currCompany = companyRepository.getCompanyById(companyId);
		if (currCompany != null) {
			companyRepository.deleteById(companyId);
			System.out.println("company with ID: " + currCompany.getId() + " has been deleted");

		} else {
			throw new CouponSystemException("Delete Company has been Failed, NO company with this ID");
		}

	}

	public List<Company> getAllCompanies() throws CouponSystemException {
		List<Company> companies_fromDB = companyRepository.findAll();
		return companies_fromDB;
	}

	public List<Customer> getAllCustomers() throws CouponSystemException {
		List<Customer> customers_fromDB = customerRepository.findAll();
		return customers_fromDB;
	}

	public Company getOneCompany(int companyId) throws CouponSystemException {
		Company currentCompany = companyRepository.getCompanyById(companyId);
		if (currentCompany != null) {
			return currentCompany;
		} else {
			throw new CouponSystemException("Comapny with ID: " + companyId + " was NOT FOUND");
		}
	}

	public Customer addCustomer(Customer customer) throws CouponSystemException {
		List<Customer> listByEmail = customerRepository.findByEmail(customer.getEmail());
		if (listByEmail != null) {
			throw new CouponSystemException(
					"Customer cannot added, there is already a Customer with the same email in DB");
		}

		System.out.println("New Customer added successfully");
		return customerRepository.save(customer);

	}

	public void updateCustomer(Customer customer, int customerId) throws CouponSystemException {
		Customer customer_fromDB = customerRepository.getCustomerById(customerId);
		if (customer_fromDB != null) {
			customer_fromDB.setFirstName(customer.getFirstName());
			customer_fromDB.setLastName(customer.getLastName());
			customer_fromDB.setEmail(customer.getEmail());
			customer_fromDB.setPassword(customer.getPassword());
			customerRepository.flush();
			System.out.println("Customer has been Updated successfully");
		} else {
			throw new CouponSystemException(
					"Update Customer has been Failed, there is NO such a Customer with this Id");
		}

	}

	public void deleteCustomer(int customerId) throws CouponSystemException {
		Customer currCustomer = customerRepository.getCustomerById(customerId);
		if (currCustomer != null) {
			customerRepository.deleteById(customerId);
			System.out.println("customer with ID: " + currCustomer.getId() + " has been deleted");
		} else {
			throw new CouponSystemException("delete Customer has been Failed, NO Customer with this id: " + customerId);
		}

	}

	public Customer getOneCustomer(int customerId) throws CouponSystemException {
		Customer currentCustomer = customerRepository.getCustomerById(customerId);
		if (currentCustomer != null) {
			return currentCustomer;
		} else {
			throw new CouponSystemException("Customer with ID: " + customerId + " was NOT FOUND");
		}
	}

}
