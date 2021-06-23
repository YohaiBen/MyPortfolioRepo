package app.core.services;

import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;
import app.core.system.exceptions.CouponSystemException;

public abstract class ClientService {

	protected CompanyRepository companyRepository;

	protected CouponRepository couponRepository;

	protected CustomerRepository customerRepository;

	public abstract boolean login(String userEmail, String userPass) throws CouponSystemException;

}
