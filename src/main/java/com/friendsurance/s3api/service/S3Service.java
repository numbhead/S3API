package com.friendsurance.s3api.service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.friendsurance.s3api.model.S3Image;
import com.friendsurance.s3api.utilities.ApplicationConstants;

@Service
public class S3Service implements Serializable {

	private static final long serialVersionUID = -5079661386824294918L;

	final AmazonS3 s3 = AmazonS3Client.builder()
		    .withRegion("us-west-2")
		    .withCredentials(new ProfileCredentialsProvider())
		    .build();

	// Method that checks if the bucket exists. If not creates one.
	public void checkBucket(String bucketName) {
		boolean bucketExist = false;
		List<Bucket> bucketList = s3.listBuckets();
		for (Bucket b : bucketList) {
			if (b.getName() == bucketName)
				bucketExist = true;
		}
		if (!bucketExist) {
			try {
				s3.createBucket(bucketName);
			} catch (AmazonS3Exception ex) {
				System.err.println(ex.getErrorMessage());
			}
		}
	}

	// Method to upload the image file to S3 Bucket and return the url of the image file of s3
	public String uploadFileToS3(String bucketName, String keyName, String filePath) {
		String url = null;
		try {
			s3.putObject(bucketName, keyName, new File(filePath));
			url = s3.getUrl(bucketName, keyName).toString();
		} catch (AmazonServiceException ex) {
			System.err.println(ex.getErrorMessage());
			System.exit(1);
		}
		return url;
	}
	
	// Method to get the S3 image URLs from S3 Bucket
	public String getS3URL(String bucketName, String keyName) {
		String url = null;
		try {
			url = s3.getUrl(bucketName, keyName).toString();
		} catch (AmazonServiceException ex) {
			System.err.println(ex.getErrorMessage());
			System.exit(1);
		}
		return url;
	}

	// Method to check if a file by the same name already exist
	public boolean checkFileExistance(String bucketName, String fileName) {
		boolean fileExist = false;
		ObjectListing objList = s3.listObjects(bucketName);
		List<S3ObjectSummary> objects = objList.getObjectSummaries();
		for (S3ObjectSummary objSummary : objects) {
			if (objSummary.getKey().equals(fileName))
				fileExist = true;
		}
		return fileExist;
	}
	
	// Method to return all list of images from S3
	public List<S3Image> getAllImageList(String bucketName) {
		List<S3Image> imageList = new ArrayList<S3Image>();
		ObjectListing objList = s3.listObjects(bucketName);
		List<S3ObjectSummary> objects = objList.getObjectSummaries();
		for (S3ObjectSummary objSummary : objects) {
			S3Image s3Image = new S3Image();
			s3Image.setImageName(objSummary.getKey());
			s3Image.setImageS3Link(this.getS3URL(ApplicationConstants.BUCKETNAME, objSummary.getKey()));
			imageList.add(s3Image);
		}
		return imageList;
	}
}
