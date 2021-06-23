package app.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.core.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("from Customer where id= :customerID")
	public Customer getCustomerById(Integer customerID);

	public Customer findByEmailAndPassword(String email, String password);

	public boolean existsByEmailAndPassword(String email, String password);

	public List<Customer> findByEmail(String email);

}
