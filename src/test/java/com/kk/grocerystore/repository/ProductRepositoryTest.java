package com.kk.grocerystore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.kk.grocerystore.model.Product;

/**
 * DataJpaTest sadece repository beanlerini yukleyecektir
 * @author korayk
 *
 */
@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	private Product product;
	 
	private Product product1;
	
	@BeforeEach
	void setUp() {
		product = Product.builder()
				.category("categ-1") 
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
		
		product1 = Product.builder()
				.category("categ-1")
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();

	}
	
	@Test
	void givenProductObject_whenSaveProduct_thenReturnSavedProduct() {
		
		// given
		
		// when
		Product savedProduct = productRepository.save(product);
		
		// then
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}

	@Test
	void givenProducts_whenFindAll_thenReturnProductList() {
		
		// given
		
		Product product2 = Product.builder()
				.category("categ-1")
				.name("milk13")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
		
		// product Rep ile de save alabiliriz
		entityManager.persist(product1);
		entityManager.persist(product2);
		
		// when
		List<Product> productList = productRepository.findAll();
		
		// then
		assertThat(productList).isNotNull();
		assertThat(productList.size()).isEqualTo(2);
	}
	
	@Test
	void givenProduct_whenFindById_thenReturnProductObject() {
		
		// given		
		
		// product Rep ile de save alabiliriz
		entityManager.persist(product1);
		
		// when
		Product savedProduct = productRepository.findById(product1.getId()).get();
		
		// then
		assertThat(savedProduct.getId()).isEqualTo(product1.getId());
		assertThat(savedProduct.getName()).isEqualTo(product1.getName());
	}
	
	@Test
	void givenProductName_whenFindByName_thenReturnProductObject() {
		
		// given
		String name = "milk12";
		Product product1 = Product.builder()
				.category("categ-1")
				.name(name)
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
		
		productRepository.save(product1);
		
		// when
		Optional<Product> optProduct = productRepository.findByName(name);
		
		// then
		assertThat(optProduct).isPresent();
	}
	
	
	@Test
	void givenProductObject_whenUpdateProduct_thenUpdatedProductObject() {
		
		// given
		Product product1 = Product.builder()
				.category("categ-1")
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
		
		productRepository.save(product1);
		Product savedProduct = productRepository.findById(product1.getId()).get();
		
		// when
		savedProduct.setCategory("categ-2");
		savedProduct.setRemainingQuantity(10);
		Product updatedProduct = productRepository.save(savedProduct);
		
		// then
		assertThat(updatedProduct).isNotNull();
		assertThat(updatedProduct.getCategory()).isEqualTo("categ-2");
		assertThat(updatedProduct.getRemainingQuantity()).isEqualTo(10);
		
	}
	
	@Test
	void givenProductObject_WhenDeleteProduct_ThenRemovedProduct() {
		
		// given
		Product product1 = Product.builder()
				.category("categ-1")
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
		
		productRepository.save(product1);
		
		// when
		productRepository.delete(product1);
		Optional<Product> optProduct = productRepository.findById(product1.getId());
		
		// then
		assertThat(optProduct).isEmpty();
	}
	
	@Test
	void givenCategory_whenProductCategoryQuery_thenReturnProductObject() {
		
		// given
		Product product1 = Product.builder()
				.category("categ-1")
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
		String category = "categ-1";
		
		productRepository.save(product1);
		
		// when
		List<Product> products = productRepository.findProductsByCategory(category);
		
		// then
		assertThat(products).isNotNull();
		assertThat(products.size()).isEqualTo(1);
	}
	
	
	@Test
	void givenCategory_whenProductCategoryNamedQuery_thenReturnProductObject() {
		
		// given
		Product product1 = Product.builder()
				.category("categ-1")
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
		String category = "categ-1";
		
		productRepository.save(product1);
		
		// when
		List<Product> products = productRepository.findProductsByCategoryNamed(category);
		
		// then
		assertThat(products).isNotNull();
		assertThat(products.size()).isEqualTo(1);
	}
	
	@Test
	void given_whenRemainingProducts_thenProductList() {
		
		// given
		Product product1 = Product.builder()
				.category("categ-1")
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
		
		Product product2 = Product.builder()
				.category("categ-1")
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(0)
				.pricePerUnit(new BigDecimal(10)).build();
		
		productRepository.save(product1);
		productRepository.save(product2);
		
		// when
		List<Product> products = productRepository.findRemaningProducts();
		
		// then
		assertThat(products).isNotNull();
		assertThat(products.size()).isEqualTo(1);
	}
	
	
}
