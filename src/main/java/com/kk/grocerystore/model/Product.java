package com.kk.grocerystore.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "Products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false)
	private String name;
	
	private String description;
	
	@Column (name = "initial_quantity")
	private Integer initialQuantity;
	
	@Column (name = "remaining_quantity")
	private Integer remainingQuantity;
	
	@Column (nullable = false)
	private String category;
	
	@Column (name = "price_per_unit")
	private BigDecimal pricePerUnit;
	
	
}
