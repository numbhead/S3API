package com.frdsn.s3api.model;

public class S3Image {
	
	private String imageS3Link;
	private String imageName;
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getImageS3Link() {
		return imageS3Link;
	}
	public void setImageS3Link(String imageS3Link) {
		this.imageS3Link = imageS3Link;
	}

}
