package com.friendsurance.s3api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friendsurance.s3api.model.Image;
import com.friendsurance.s3api.service.ImageService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/image")
public class ImageController {

	private final Logger log = LogManager.getLogger(ImageController.class);
	
	@Autowired
	ImageService imgService;

	
	@PostMapping(value = "/saveImageToS3")
	public ResponseEntity<Object> saveImage(@RequestBody Image image) {
		Date date = new Date();
		Map<String, String> errors = new HashMap<>();
		try {
			if(image != null) {
				image.setDateOfStorage(date);
				imgService.saveImageToDB(image);
				return new ResponseEntity<>(image, HttpStatus.CREATED);
			}
			else
			{
				errors.put("null", "Image Object was null");
				return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
			}
				
		}
		catch(Exception exp)
		{
			return new ResponseEntity(errors, HttpStatus.CONFLICT);
		}
	}
	
	
	
	public ImageService getImgService() {
		return imgService;
	}

	public void setImgService(ImageService imgService) {
		this.imgService = imgService;
	}
	
	
}
