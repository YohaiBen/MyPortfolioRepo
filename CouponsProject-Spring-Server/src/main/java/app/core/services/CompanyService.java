package app.core.services;

import java.sql.Date;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.system.exceptions.CouponSystemException;

@Service
@Transactional
@Scope("prototype")
public class CompanyService extends ClientService {

	@PersistenceContext
	private EntityManager em;
	private int company_Id;

	public int getCompany_Id() {
		return company_Id;
	}

	public void setCompany_Id(int company_Id) {
		this.company_Id = company_Id;
	}

	public CompanyService(CompanyRepository companyRepository, CouponRepository couponRepository) {
		super();
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		Company currentCompany = companyRepository.findByEmailAndPassword(email, password);
		if (currentCompany != null) {
			this.company_Id = currentCompany.getId();
			return true;
		} else {
			throw new CouponSystemException("Company login have been failed/ email or password are wrong");

		}
	}

	public void addCoupon(Coupon coupon) throws CouponSystemException {
		if (couponRepository.existsByTitleAndCompanyId(coupon.getTitle(), this.company_Id)) {
			throw new CouponSystemException("add coupon has been Failed, you already have coupon with the same Title");
		}
		if (coupon.getEndDate().before(new Date(System.currentTimeMillis()))
				|| coupon.getEndDate().before(coupon.getStartDate())) {
			throw new CouponSystemException(
					"add coupon has been Failed, coupon's end-date is expired/before start date. \n notice that coupon's end date has to be in the future! ");

		}
		Company currCompany = companyRepository.getCompanyById(this.company_Id);
		currCompany.addCoupon(coupon);
		System.out.println("New Coupon was addad successfully");

	}

	public void updateCoupon(Coupon coupon, int coupon_ID) throws CouponSystemException {
		Coupon coupon_fromDB = couponRepository.getCouponById(coupon_ID);
		if (coupon_fromDB != null && coupon_fromDB.getCompany().getId() == this.company_Id) {
			coupon_fromDB.setCategory(coupon.getCategory());
			coupon_fromDB.setTitle(coupon.getTitle());
			coupon_fromDB.setDescription(coupon.getDescription());
			coupon_fromDB.setStartDate(coupon.getStartDate());
			coupon_fromDB.setEndDate(coupon.getEndDate());
			coupon_fromDB.setAmount(coupon.getAmount());
			coupon_fromDB.setPrice(coupon.getPrice());
			coupon_fromDB.setImage(coupon.getImage());
			couponRepository.flush();
			System.out.println("Coupon has been Updated successfully");
		} else {
			throw new CouponSystemException(
					"Update Coupon has been Failed, there is NO such a coupon with this Id/ you are NOT owner of this Coupon");
		}

	}

	public void deleteCoupon(int couponId) throws CouponSystemException {
		Coupon currCoupon = couponRepository.getCouponById(couponId);
		if (currCoupon != null && currCoupon.getCompany().getId() == this.company_Id) {
			couponRepository.deleteById(couponId);
			System.out.println("Coupon has been Deleted successfully");
		} else {
			throw new CouponSystemException("NO coupon with this ID/ your Company is Not an Owner of this coupon");
		}

	}

	public List<Coupon> getCompanyCoupons() throws CouponSystemException {
		List<Coupon> couponsList = couponRepository.findByCompanyId(this.company_Id);
		return couponsList;
	}

	public List<Coupon> getCompanyCoupons(Category category) throws CouponSystemException {
		List<Coupon> couponsList = couponRepository.findByCompanyIdAndCategory(this.company_Id, category);
		return couponsList;
	}

	public List<Coupon> getCompanyCoupons(double maxPrice) throws CouponSystemException {
		List<Coupon> couponsList = couponRepository.findByCompanyIdAndPriceLessThanEqual(this.company_Id, maxPrice);
		return couponsList;
	}

	public Company getCompanyDetails() throws CouponSystemException {
		Company currCompany = companyRepository.getCompanyById(this.company_Id);
		if (currCompany != null) {
			return currCompany;
		}
		throw new CouponSystemException("Company with This ID does Not exists");

	}

	public Coupon getOneCoupon(int couponId) throws CouponSystemException {
		Coupon currentCoupon = couponRepository.getCouponById(couponId);
		if (currentCoupon != null && currentCoupon.getCompany().getId() == this.company_Id) {
			return currentCoupon;
		} else {
			throw new CouponSystemException("NO coupon with this ID/ you are Not Owner of the coupon ");
		}
	}

}
