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
				errors =  imgService.validateImage(image);
					if(errors.size()==0)
					{
						imgService.saveImageToDB(image);
						return new ResponseEntity<>(image, HttpStatus.CREATED);
					}
					else
						return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
			else
			{
				errors.put("null", "Image Object was null");
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}
				
		}
		catch(Exception exp)
		{
			errors.put("exceptions", exp.getStackTrace().toString());
			return new ResponseEntity<>(errors, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	
	
	public ImageService getImgService() {
		return imgService;
	}

	public void setImgService(ImageService imgService) {
		this.imgService = imgService;
	}
	
	
}
