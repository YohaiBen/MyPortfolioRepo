package app.core.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import app.core.entities.Category;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;
import app.core.system.exceptions.CouponSystemException;

@Service
@Transactional
@Scope("prototype")
public class CustomerService extends ClientService {

	@PersistenceContext
	private EntityManager em;
	private int customer_Id;

	public int getCustomer_Id() {
		return customer_Id;
	}

	public void setCustomer_Id(int customer_Id) {
		this.customer_Id = customer_Id;
	}

	public CustomerService(CustomerRepository customerRepository, CouponRepository couponRepository) {
		super();
		this.customerRepository = customerRepository;
		this.couponRepository = couponRepository;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		Customer currentCustomer = customerRepository.findByEmailAndPassword(email, password);
		if (currentCustomer != null) {
			this.customer_Id = currentCustomer.getId();
			return true;
		} else {
			throw new CouponSystemException("Customer login have been failed/ email or password are wrong ");

		}
	}

	public void purchaseCoupon(Integer couponId) throws CouponSystemException {
		Customer customer = customerRepository.getCustomerById(this.customer_Id);
		Coupon coupon = couponRepository.getCouponById(couponId);
		if (coupon != null) {
			if (coupon.getAmount() == 0 || coupon.getEndDate().before(new Date(System.currentTimeMillis()))) {
				throw new CouponSystemException(
						"Cannot purchase coupon. Amount in stock is 0/ coupon's endTime Expired");
			}
			if (customer != null) {
				em.refresh(customer);
				for (Coupon currCoupon : customer.getCouponsList()) {
					if (coupon.getCouponId() == currCoupon.getCouponId()) {
						throw new CouponSystemException(
								"Cannot purchase coupon. you already purchased coupon with the same ID");
					}
				}
				coupon.setAmount(coupon.getAmount() - 1);
				customer.addCoupon(coupon);
				System.out.println("Coupon with ID: " + coupon.getCouponId() + " has been purchased successfully");
				return;
			}
		}

		throw new CouponSystemException("Coupon with this ID: " + couponId + " was NOT_FOUND");
	}

	public List<Coupon> getCustomerCoupons() throws CouponSystemException {
		Customer customer = customerRepository.getCustomerById(this.customer_Id);
		if (customer != null) {
			em.refresh(customer);
			return customer.getCouponsList();
		} else {
			throw new CouponSystemException("There is no such a Customer with this ID");
		}

	}

	public List<Coupon> getCustomerCoupons(Category category) throws CouponSystemException {
		Customer customer = customerRepository.getCustomerById(this.customer_Id);
		if (customer != null) {
			em.refresh(customer);
			List<Coupon> couponsByCategory = new ArrayList<Coupon>();

			List<Coupon> couponsList = customer.getCouponsList();
			for (Coupon coupon : couponsList) {
				if (coupon.getCategory().equals(category)) {
					couponsByCategory.add(coupon);
				}
			}
			return couponsByCategory;

		} else {
			throw new CouponSystemException("There is no such a Customer with this ID");
		}
	}

// אולי אפשר ליעל ביצועים
	public List<Coupon> getCustomerCoupons(double maxPrice) throws CouponSystemException {
		Customer customer = customerRepository.getCustomerById(this.customer_Id);
		if (customer != null) {
			em.refresh(customer);
			List<Coupon> couponsByPrice = new ArrayList<Coupon>();
			List<Coupon> couponsList = customer.getCouponsList();
			for (Coupon coupon : couponsList) {
				if (coupon.getPrice() <= maxPrice) {
					couponsByPrice.add(coupon);
				}
			}

			return couponsByPrice;
		} else {
			throw new CouponSystemException("There is no such a Customer with this ID");
		}
	}

	public Customer getCustomerDetails() throws CouponSystemException {
		Customer currCustomer = customerRepository.getCustomerById(customer_Id);
		return currCustomer;

	}

	public Coupon getOneCoupon(int coupon_Id) throws CouponSystemException {
		Coupon currentCoupon = couponRepository.getCouponById(coupon_Id);
		if (currentCoupon != null) {
			return currentCoupon;
		} else {
			throw new CouponSystemException("NO coupon with this ID");
		}
	}

	public List<Coupon> showMeWebsiteCoupons() throws CouponSystemException {
		return couponRepository.findAll();

	}

}
