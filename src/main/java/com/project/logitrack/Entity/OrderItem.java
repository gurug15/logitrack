package com.project.logitrack.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;import org.springframework.context.annotation.Fallback;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orderitem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "orderId", foreignKey = @ForeignKey(name = "fk_orderitem_ordeId"))
	@JsonBackReference  //added
	private Order order;

	@ManyToOne(optional = false)
	@JoinColumn(name = "itemId", foreignKey = @ForeignKey(name = "fk_orderitem_itemId"))
	private Item item;
	
	private Integer quantity;
	
	private BigDecimal unitPrice;

	public String getProductName() {
		return item.getName();    //we are going into item object and finding out the name 
	}

	public void setProductName(String productName) {
		item.setName(productName);
		
	}
	
		
	

}


