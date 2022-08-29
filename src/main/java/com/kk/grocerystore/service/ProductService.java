package com.kk.grocerystore.service;

import java.util.List;
import java.util.Optional;

import com.kk.grocerystore.model.Product;

public interface ProductService {

	public Product saveProduct(Product product);
	
	public List<Product> listProduct();
	
	public Optional<Product> getProductById(Long id);
	
	public Product updateProduct(Product product);
	
	public void deleteProductById(Long id);
}
