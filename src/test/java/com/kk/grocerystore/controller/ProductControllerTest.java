package com.kk.grocerystore.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.grocerystore.model.Product;
import com.kk.grocerystore.service.ProductService;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	
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
		given(productService.saveProduct(ArgumentMatchers.any(Product.class)))
			.willAnswer(x -> x.getArgument(0));
		
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
		given(productService.listProduct())
			.willReturn(products);
		
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
		Long id = 1L;
		given(productService.getProductById(id))
			.willReturn(Optional.of(prod1));
		
		// when
		ResultActions response = mockMvc.perform(get("/api/product/{id}", id));
		
		// then
		response.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(prod1.getName())));
	}
	
	@Test
	void givenNotValidId_whenGetProductById_thenReturnNothing() throws Exception {
		// given
		Long id = 1L;
		given(productService.getProductById(id))
			.willReturn(Optional.empty());
		
		// when
		ResultActions response = mockMvc.perform(get("/api/product/{id}", id));
		
		// then
		response.andDo(print())
				.andExpect(status().isNotFound());
	}
	
	@Test
	void givenValidIdAndProduct_whenUpdateProduct_thenUpdatedProduct() throws Exception {
		
		// given
		Long id = 1L;
		
		given(productService.getProductById(id))
			.willReturn(Optional.of(prod1));
		
		given(productService.updateProduct(any()))
			.willAnswer(x -> x.getArgument(0));
		
		prod2.setDescription("descrip123");
		
		// when
		ResultActions response  = mockMvc.perform(put("/api/product/{id}", id)
												  .contentType(MediaType.APPLICATION_JSON)
												  .content(objectMapper.writeValueAsString(prod2)));
		
		// then
		response.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", is(prod2.getDescription())));
	}
	
	@Test
	void givenNotValidIdAndProduct_whenUpdateProduct_thenUpdatedProduct() throws Exception {
		
		// given
		Long id = 1L;
		
		given(productService.getProductById(id))
			.willReturn(Optional.empty());
		
		given(productService.updateProduct(any()))
			.willAnswer(x -> x.getArgument(0));
		
		prod2.setDescription("descrip123");
		
		// when
		ResultActions response  = mockMvc.perform(put("/api/product/{id}", id)
												  .contentType(MediaType.APPLICATION_JSON)
												  .content(objectMapper.writeValueAsString(prod2)));
		
		// then
		response.andDo(print())
				.andExpect(status().isNotFound());
		verify(productService, never()).saveProduct(any());
	}
	
	@Test
	void givenId_whenDeleteProductById_thenReturnNothing() throws Exception{
		
		// given
		Long id = 1L;
		willDoNothing().given(productService).deleteProductById(id);
		
		// when
		ResultActions response = mockMvc.perform(delete("/api/product/{id}", id));
		
		// then
		response.andDo(print())
				.andExpect(status().isOk());
	}
	
	
	
	
	
	
	
	
	

}
