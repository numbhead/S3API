package com.frdsn.s3api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Image implements Serializable{

	private static final long serialVersionUID = 5340548217025992023L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String sourceLink;
	
	@Column(unique = true)
	private String s3Link;
	
	@Column
	private Date dateOfStorage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	public String getS3Link() {
		return s3Link;
	}

	public void setS3Link(String s3Link) {
		this.s3Link = s3Link;
	}

	public Date getDateOfStorage() {
		return dateOfStorage;
	}

	public void setDateOfStorage(Date dateOfStorage) {
		this.dateOfStorage = dateOfStorage;
	}
	
	
	
	
}
