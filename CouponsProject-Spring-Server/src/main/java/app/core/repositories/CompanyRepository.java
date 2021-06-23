package app.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.core.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

	@Query("from Company where id= :company_id")
	public Company getCompanyById(Integer company_id);

	public Company findByEmailAndPassword(String email, String password);

	public boolean existsByEmailAndPassword(String email, String password);

	public List<Company> findByNameOrEmail(String name, String email);

}
