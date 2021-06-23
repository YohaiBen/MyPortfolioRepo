package app.core.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String first_name;
	private String last_name;
	private String email;
	private String password;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinTable(name = "customers_vs_coupons", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "coupon_id"))
	@JsonIgnore
	private List<Coupon> couponsList;

	public void addCoupon(Coupon coupon) {
		if (this.couponsList == null) {
			this.couponsList = new ArrayList<Coupon>();
		}
		this.couponsList.add(coupon);
	}

	public Customer() {
		super();

	}

	public Customer(String firstName, String lastName, String email, String password) {
		super();
		this.first_name = firstName;
		this.last_name = lastName;
		this.email = email;
		this.password = password;

	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}

	public void setLastName(String lastName) {
		last_name = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCouponsList(List<Coupon> couponsList) {
		this.couponsList = couponsList;
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return first_name;
	}

	public String getLastName() {
		return last_name;
	}

	public String getEmail() {
		return email;
	}

	public List<Coupon> getCouponsList() {
		return couponsList;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + first_name + ", LastName=" + last_name + ", email=" + email
				+ ", password=" + password + "]";
	}

}
