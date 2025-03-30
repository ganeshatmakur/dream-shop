package com.dailycodework.dreamshops.model;

import java.sql.Blob;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String fileName;
	private String fileType;
	
	@Lob
	private Blob images;
	private String downloadurl;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
}
