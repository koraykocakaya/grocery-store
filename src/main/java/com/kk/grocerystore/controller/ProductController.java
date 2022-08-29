package com.kk.grocerystore.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kk.grocerystore.model.Product;
import com.kk.grocerystore.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/api/product")
	@ResponseStatus(HttpStatus.CREATED)
	public Product saveProduct(@RequestBody Product product) {
		return productService.saveProduct(product);
	}
	
	@GetMapping("/api/product")
	public List<Product> getAllProducts(){
		return productService.listProduct();
	}
	
	@GetMapping("/api/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable(name = "id") Long id){
		Optional<Product> productOpt = productService.getProductById(id);
		if(productOpt.isPresent())
			return ResponseEntity.ok(productOpt.get());
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/api/product/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable(name = "id") Long id,
												 @RequestBody Product product){
		
		Optional<Product> productOpt = productService.getProductById(id);
		if(!productOpt.isPresent())
			return ResponseEntity.notFound().build();
		
		Product savedProduct = productOpt.get();
		savedProduct.setCategory(product.getCategory());
		savedProduct.setDescription(product.getDescription());
		savedProduct.setInitialQuantity(product.getInitialQuantity());
		savedProduct.setPricePerUnit(product.getPricePerUnit());
		savedProduct.setRemainingQuantity(product.getRemainingQuantity());
		savedProduct.setName(product.getName());
		
		return ResponseEntity.ok(productService.updateProduct(savedProduct)); 
	}
	
	@DeleteMapping("/api/product/{id}")
	public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id){
		productService.deleteProductById(id);
		return ResponseEntity.ok("Product deleted");
	}
}
