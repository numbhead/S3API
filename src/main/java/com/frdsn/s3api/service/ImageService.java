package com.frdsn.s3api.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frdsn.s3api.model.Image;
import com.frdsn.s3api.model.S3Image;
import com.frdsn.s3api.repository.ImageRepository;
import com.frdsn.s3api.utilities.ApplicationConstants;

@Service
public class ImageService implements Serializable {

	private static final long serialVersionUID = 7821042857243465720L;
	private final Logger log = LogManager.getLogger(ImageService.class);

	@Autowired
	private ImageRepository imageRepo;

	private Map<String, String> errors = null;

	// Method to save the image details to Database
	public void saveImageToDB(Image img) {
		try {
			this.imageRepo.save(img);
		} catch (Exception e) {
			log.error(e);
		}
	}

	// Method to download the image and store locally before upload
	public Map<String, String> downloadImage(String sourceLink, String fileName) {
		errors = new LinkedHashMap<>();
		File newFile = new File(ApplicationConstants.FILEUPLOAD + fileName);
		System.out.println("File about to be created");
		URL imageLink = null;
		try {
			imageLink = new URL(sourceLink);
		} catch (MalformedURLException e1) {
			errors.put("URLInvalid", "Could not find the URL-" + sourceLink);
			e1.printStackTrace();
		}

		try {
			if (!newFile.createNewFile())
				errors.put("fileCreation", "Could not create the file-");
		} catch (IOException e1) {
			errors.put("fileCreation", "Could not create the file. Exception occured");
			e1.printStackTrace();
		}

		try {
			FileUtils.copyURLToFile(imageLink, newFile);
		} catch (IOException e) {
			errors.put("imageDownload", "There was a problem downloading the image.");
			e.printStackTrace();
		}

		return errors;
	}

	// Method to validate the image details passes over the HTTP request
	public Map<String, String> validateImage(Image image) {
		errors = new LinkedHashMap<>();
		if (image.getName() == null || image.getName().isEmpty())
			errors.put("name", "Name was empty");
		if (image.getSourceLink() == null || image.getSourceLink().isEmpty())
			errors.put("sourceLink", "Source Link was empty");
		return errors;
	}

	// Method to delete the file locally once the upload to S3 is successful
	public void deleteFile(String filePath) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

}
