package app.core.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.core.entities.Category;
import app.core.entities.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	@Query("from Coupon where coupon_Id= :couponID")
	public Coupon getCouponById(Integer couponID);

	public List<Coupon> findByCompanyId(Integer company_id);

	public List<Coupon> findByCompanyIdAndCategory(Integer company_id, Category category);

	public List<Coupon> findByCompanyIdAndPriceLessThanEqual(Integer company_id, double maxPrice);

	public boolean existsByTitleAndCompanyId(String title, Integer company_id);

	public List<Coupon> findByEndDateBefore(Date CurrentTime);

}
