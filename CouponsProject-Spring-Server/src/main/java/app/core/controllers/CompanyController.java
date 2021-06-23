package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.services.CompanyService;
import app.core.sessions.Session;
import app.core.sessions.SessionContextManager;
import app.core.system.exceptions.CouponSystemException;

@RestController
@CrossOrigin
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	private SessionContextManager sessionContext;

	private CompanyService isCompanySessionExists(String theToken) {
		try {
			Session session = this.sessionContext.getSession(theToken);
			CompanyService companyService = (CompanyService) session.getAttribute("service");
			return companyService;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NO session, you need to login as a client!", e);
		}
	}

	@PostMapping(path = "/add")
	public ResponseEntity<?> addCoupon(@RequestHeader String token, @RequestBody Coupon coupon) {
		try {
			isCompanySessionExists(token).addCoupon(coupon);
			return ResponseEntity.status(HttpStatus.OK).body("New Coupon was addad successfully");
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@PutMapping(path = "/update/coupon/{coupon_ID}")
	public ResponseEntity<?> updateCoupon(@RequestHeader String token, @RequestBody Coupon coupon,
			@PathVariable int coupon_ID) {
		try {
			isCompanySessionExists(token).updateCoupon(coupon, coupon_ID);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Coupon with ID: " + coupon_ID + " has been Updated successfully");
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@DeleteMapping(path = "/delete")
	public ResponseEntity<?> deleteCoupon(@RequestHeader String token, @RequestParam int couponId) {
		try {
			isCompanySessionExists(token).deleteCoupon(couponId);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Coupon with ID: " + couponId + " has been Deleted successfully");
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCoupons(@RequestHeader String token) {
		try {
			List<Coupon> couponsList = isCompanySessionExists(token).getCompanyCoupons();
			return ResponseEntity.status(HttpStatus.OK).body(couponsList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/byCategory", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCouponsByCategory(@RequestHeader String token, @RequestParam Category category) {
		try {
			List<Coupon> couponsByCategoryList = isCompanySessionExists(token).getCompanyCoupons(category);
			return ResponseEntity.status(HttpStatus.OK).body(couponsByCategoryList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/byMaxPrice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCouponsByMaxPrice(@RequestHeader String token, @RequestParam double maxPrice) {
		try {
			List<Coupon> couponsByMaxPriceList = isCompanySessionExists(token).getCompanyCoupons(maxPrice);
			return ResponseEntity.status(HttpStatus.OK).body(couponsByMaxPriceList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/getDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCompanyDetails(@RequestHeader String token) {
		try {
			Company company = isCompanySessionExists(token).getCompanyDetails();
			return ResponseEntity.status(HttpStatus.OK).body(company);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

}
