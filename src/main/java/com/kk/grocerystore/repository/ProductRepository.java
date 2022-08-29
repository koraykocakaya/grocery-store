package com.kk.grocerystore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kk.grocerystore.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	public Optional<Product> findByName(String name);
	
	@Query("Select p from Product p where p.category = ?1")
	public List<Product> findProductsByCategory(String category);
	
	@Query("Select p from Product p where p.category = :category")
	public List<Product> findProductsByCategoryNamed(@Param("category") String category);
	
	@Query(value = "select p.* from products p where p.remaining_quantity > 0", nativeQuery = true)
	public List<Product> findRemaningProducts();
}
