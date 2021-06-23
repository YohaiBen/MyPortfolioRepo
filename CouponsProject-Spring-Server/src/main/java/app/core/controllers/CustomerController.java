package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import app.core.entities.Category;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.services.CustomerService;
import app.core.sessions.Session;
import app.core.sessions.SessionContextManager;
import app.core.system.exceptions.CouponSystemException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private SessionContextManager sessionContextManger;

	private CustomerService isCompanySessionExists(String theToken) {
		try {
			Session session = this.sessionContextManger.getSession(theToken);
			CustomerService customerService = (CustomerService) session.getAttribute("service");
			return customerService;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NO session, you need to login as a client!");
		}
	}

	@PostMapping(path = "/purchase")
	public ResponseEntity<?> purchaseCoupon(@RequestHeader String token, @RequestParam Integer couponId) {
		try {
			isCompanySessionExists(token).purchaseCoupon(couponId);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Coupon with ID: " + couponId + " has been purchased successfully");
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCoupons(@RequestHeader String token) {
		try {
			List<Coupon> couponsList = isCompanySessionExists(token).getCustomerCoupons();
			return ResponseEntity.status(HttpStatus.OK).body(couponsList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/byCategory", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCouponsByCategory(@RequestHeader String token, @RequestParam Category category) {
		try {
			List<Coupon> couponsByCategoryList = isCompanySessionExists(token).getCustomerCoupons(category);
			return ResponseEntity.status(HttpStatus.OK).body(couponsByCategoryList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/byMaxPrice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCouponsByMaxprice(@RequestHeader String token, @RequestParam double maxPrice) {
		try {
			List<Coupon> couponsByMaxPriceList = isCompanySessionExists(token).getCustomerCoupons(maxPrice);
			return ResponseEntity.status(HttpStatus.OK).body(couponsByMaxPriceList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/getDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCustomerDetails(@RequestHeader String token) {
		try {
			Customer customer = isCompanySessionExists(token).getCustomerDetails();
			return ResponseEntity.status(HttpStatus.OK).body(customer);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(path = "/get/websiteCoupons", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> showAllWebsiteCoupons(@RequestHeader String token) {
		try {
			List<Coupon> couponsList = isCompanySessionExists(token).showMeWebsiteCoupons();
			return ResponseEntity.status(HttpStatus.OK).body(couponsList);
		} catch (CouponSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}
}
