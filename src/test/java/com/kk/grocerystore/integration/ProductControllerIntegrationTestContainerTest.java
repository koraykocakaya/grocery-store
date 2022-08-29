package com.kk.grocerystore.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.grocerystore.model.Product;
import com.kk.grocerystore.repository.ProductRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTestContainerTest extends AbstractIntegrationExample{

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static Product prod1;
	private static Product prod2;
	
	@BeforeAll
	static void beforeAll() {
			prod1 = Product.builder()
				.name("Milk13")
				.category("MM")
				.initialQuantity(1000)
				.remainingQuantity(1000)
				.pricePerUnit(new BigDecimal(10))
				.build();
						
						
			prod2 = Product.builder()
				.name("Milk12")
				.category("MM")
				.initialQuantity(2000)
				.remainingQuantity(1000)
				.pricePerUnit(new BigDecimal(5))
				.build();
	}
	
	@BeforeEach
	void setUp(){
		productRepository.deleteAll();
	}
	
	@AfterEach
	void afterEach(){
		productRepository.deleteAll();
	}
	
	
	@Test
	void givenProductObject_whenSaveProduct_thenReturnSavedProductObject() throws Exception{
		
		// given
		Product product1 = Product.builder()
								.name("Milk12")
								.category("MM")
								.initialQuantity(1000)
								.remainingQuantity(1000)
								.pricePerUnit(new BigDecimal(1))
								.build();
		// when
		ResultActions response =
				mockMvc.perform(post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(product1)));
		
		
		// then
		response.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is(product1.getName())))
				.andExpect(jsonPath("$.category", is(product1.getCategory())));
	}
	
	@Test
	void givenProductList_whenGetAllProducts_thenReturnProductList() throws Exception{
		// given
		List<Product> products = Arrays.asList(prod1, prod2);
		productRepository.saveAll(products);
		
		// when
		ResultActions response = mockMvc.perform(get("/api/product"));
												
		
		// then
		response.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(products.size())))
				.andExpect(jsonPath("$.[0].name", is("Milk13")));
	}
	
	@Test
	void givenValidId_whenGetProductById_thenReturnProductObject() throws Exception{
		// given
		Product p1 = Product.builder()
							.name("uno")
							.category("bread")
							.initialQuantity(100)
							.remainingQuantity(100)
							.pricePerUnit(new BigDecimal(10))
							.build();
		
		productRepository.save(p1);
 
		// when
		ResultActions response = mockMvc.perform(get("/api/product/{id}", p1.getId()));
		
		// then
		response.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(p1.getName())));
	}
	
	@Test
	void givenNotValidId_whenGetProductById_thenReturnNotFound() throws Exception {
		// given
		Long id = 2L;
		
		// when
		ResultActions response = mockMvc.perform(get("/api/product/{id}", id));
		
		// then
		response.andDo(print())
				.andExpect(status().isNotFound());
	}
	
	@Test
	void givenValidIdAndProduct_whenUpdateProduct_thenUpdatedProduct() throws Exception {
		
		// given
		Product p1 = Product.builder()
				.name("uno")
				.category("bread")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10))
				.build();
		
		productRepository.save(p1);
		
		Product p2 = Product.builder()
				.name("uno2")
				.category("bread2")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10))
				.build();
		
		
		// when
		ResultActions response  = mockMvc.perform(put("/api/product/{id}", p1.getId())
												  .contentType(MediaType.APPLICATION_JSON)
												  .content(objectMapper.writeValueAsString(p2)));
		
		// then
		response.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", is(p2.getName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.category", is(p2.getCategory())));
	}
	
	
	@Test
	void givenNotValidIdAndProduct_whenUpdateProduct_thenUpdatedProduct() throws Exception {
		
		// given
		Long id = 0L;
		
		prod2.setDescription("descrip123");
		
		// when
		ResultActions response  = mockMvc.perform(put("/api/product/{id}", id)
												  .contentType(MediaType.APPLICATION_JSON)
												  .content(objectMapper.writeValueAsString(prod2)));
		
		// then
		response.andDo(print())
				.andExpect(status().isNotFound());
	}
	
	@Test
	void givenId_whenDeleteProductById_thenReturnNothing() throws Exception{
		
		// given
		Product p1 = Product.builder()
				.name("uno")
				.category("bread")
				.initialQuantity(100)
				.remainingQuantity(100)
				.pricePerUnit(new BigDecimal(10))
				.build();
		
		productRepository.save(p1);
		
		// when
		ResultActions response = mockMvc.perform(delete("/api/product/{id}", p1.getId()));
		
		// then
		response.andDo(print())
				.andExpect(status().isOk());
	}
	
}
