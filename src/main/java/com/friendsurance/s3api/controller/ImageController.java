package com.friendsurance.s3api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.regions.Regions;
import com.friendsurance.s3api.model.Image;
import com.friendsurance.s3api.model.S3Image;
import com.friendsurance.s3api.service.ImageService;
import com.friendsurance.s3api.service.S3Service;
import com.friendsurance.s3api.utilities.ApplicationConstants;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/image")
public class ImageController {

	private final Logger log = LogManager.getLogger(ImageController.class);

	@Autowired
	ImageService imgService;

	@Autowired
	S3Service s3Service;

	// Method to handle the Save image to S3
	@PostMapping(value = "/saveImageToS3")
	public ResponseEntity<Object> saveImage(@RequestBody Image image) {
		
		String filePath = null;
		String s3URL = null;
		Date date = new Date();
		Map<String, String> errors = new HashMap<>();
		
		// Creation of Bucket if it does not exist. Mostly used during the first time run.
		s3Service.checkBucket(ApplicationConstants.BUCKETNAME); 
																 
		try {
			if (image != null) {
				filePath = ApplicationConstants.FILEUPLOAD + image.getName();
				image.setDateOfStorage(date);
				
				//Validates the Date object received in the HTTP request
				errors = imgService.validateImage(image);
				
				if (errors.size() == 0
						&& !s3Service.checkFileExistance(ApplicationConstants.BUCKETNAME, image.getName())) {
					imgService.downloadImage(image.getSourceLink(), image.getName());	// downloads the image file from the URL shared
					s3URL = s3Service.uploadFileToS3(ApplicationConstants.BUCKETNAME, image.getName(), filePath); // Uploads the file to S3
					if (s3URL != null || !s3URL.isEmpty())
						image.setS3Link(s3URL);    			// sets the S3 link received after saving
					imgService.saveImageToDB(image);  		// saves the image details to DB
					imgService.deleteFile(filePath);  		// deletes the file locally once the upload is complete.
					return new ResponseEntity<>(image, HttpStatus.CREATED);
				} else {
					if (s3Service.checkFileExistance(ApplicationConstants.BUCKETNAME, image.getName()))
						errors.put("duplicate", "Image by this name already exist");		// returns error if the name passed by the user already exist in S3
					return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
				}
			} else {
				errors.put("null", "Image Object was null");		// returns error when the object passed on HTTP was null
				return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception exp) {
			log.error(exp.getStackTrace().toString());
			errors.put("exceptions", exp.getStackTrace().toString());   // returns any other exception generated
			return new ResponseEntity<>(errors, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@GetMapping(value = "/getAllImageList")
	public List<S3Image> getAllImageList() {
        return s3Service.getAllImageList(ApplicationConstants.BUCKETNAME);
    }

	public S3Service getS3Service() {
		return s3Service;
	}

	public void setS3Service(S3Service s3Service) {
		this.s3Service = s3Service;
	}

	public ImageService getImgService() {
		return imgService;
	}

	public void setImgService(ImageService imgService) {
		this.imgService = imgService;
	}

}
