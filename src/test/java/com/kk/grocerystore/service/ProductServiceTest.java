package com.kk.grocerystore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kk.grocerystore.exception.DuplicateProductException;
import com.kk.grocerystore.model.Product;
import com.kk.grocerystore.repository.ProductRepository;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;
	
	@MockBean
	private ProductRepository productRepository;
	
	private Product product1;
	
	@BeforeEach
	void setUp() {
		product1 = Product.builder()
				.id(1L) 
				.category("categ-1")
				.name("milk12")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10)).build();
	}
	
	@Test
	void givenProduct_whenSaveProduct_thenSavedProductObject() {
		
		// given
		given(productRepository.findByName(product1.getName()))
			.willReturn(Optional.empty());
		
		given(productRepository.save(product1))
			.willReturn(product1);
		
		// when
		Product savedProduct = productService.saveProduct(product1);
		
		// then
		assertThat(savedProduct).isNotNull();
	}
	
	@Test
	void givenProductInDb_whenSaveProduct_thenThrowsException() {
		
		// given
		given(productRepository.findByName(product1.getName()))
			.willReturn(Optional.of(product1));
		
		
//		given(productRepository.save(product1))
//			.willReturn(product1);
		// when
		Executable executable = () -> productService.saveProduct(product1);
		
		// then
		org.junit.jupiter.api.Assertions.assertThrows(DuplicateProductException.class, executable);
		verify(productRepository, never()).save(any(Product.class));
	}
	
	@Test
	void givenProductList_whenListProduct_ThenProductList() {
	
		
		// given
		Product product2 = Product.builder()
							.category("categ2")
							.name("milk12345")
							.description("desc")
							.initialQuantity(1000)
							.remainingQuantity(100)
							.pricePerUnit(new BigDecimal(10)).build();
		
		given(productRepository.findAll())
			.willReturn(List.of(product1, product2));
		
		// when
		List<Product> products = productService.listProduct();
		
		// then
		assertThat(products).isNotNull();
		assertThat(products.size()).isEqualTo(2);		
	}
	
	
	@Test
	void givenEmptyList_whenListProduct_ThenProductList() {
		
		// given
		given(productRepository.findAll())
			.willReturn(List.of());
		
		// when
		List<Product> products = productService.listProduct();
		
		// then
		assertThat(products).isNotNull();
		assertThat(products.size()).isEqualTo(0);		
	}
	
	@Test
	void givenProductId_whenGetProductById_thenReturnProductObject() {
		// given
		Long id = 1L;
		given(productRepository.findById(id))
			.willReturn(Optional.of(product1));
		
		
		// when
		Optional<Product> optProd = productService.getProductById(id);
		
		// then
		assertThat(optProd).isPresent();
		
	} 
	
	@Test
	void givenProduct_whenUpdateProduct_thenReturnProductObject() {
		
		
		// given
		given(productRepository.save(product1))
			.willReturn(product1);

		product1.setCategory("cat12354");
		
		// when
		Product updatedProduct = productService.updateProduct(product1);
		
		// then
		assertThat(updatedProduct).isNotNull();
		assertThat(updatedProduct.getCategory()).isEqualTo("cat12354");
	}
	
	@Test
	void givenProductId_whenDeleteProductById_thenReturnNothing() {
		
		// given
		Long id = 1L;
		willDoNothing().given(productRepository).deleteById(id);
		
		// when
		productService.deleteProductById(id);
		
		// then
		verify(productRepository, times(1)).deleteById(id);
		
	}
}
