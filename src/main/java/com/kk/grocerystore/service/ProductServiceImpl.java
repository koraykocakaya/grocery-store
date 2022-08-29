package com.kk.grocerystore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kk.grocerystore.exception.DuplicateProductException;
import com.kk.grocerystore.model.Product;
import com.kk.grocerystore.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product saveProduct(Product product) {
		Optional<Product> productOpt = productRepository.findByName(product.getName());
		if(productOpt.isPresent())
			throw new DuplicateProductException("Product found with name: " + product.getName());
		return productRepository.save(product);		
	}
	
	@Override
	public List<Product> listProduct(){
		return productRepository.findAll();
	}
	
	public Optional<Product> getProductById(Long id){
		return productRepository.findById(id);
	}
	
	public Product updateProduct(Product product) {
		return productRepository.save(product);
	}
	
	public void deleteProductById(Long id) {
		productRepository.deleteById(id);
	}
}
