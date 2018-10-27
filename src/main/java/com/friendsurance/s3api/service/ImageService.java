package com.friendsurance.s3api.service;

import java.io.Serializable;

import com.friendsurance.s3api.model.Image;
import com.friendsurance.s3api.repository.ImageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService implements Serializable{

	private static final long serialVersionUID = 7821042857243465720L;
	private final Logger log = LogManager.getLogger(ImageService.class);
	
	@Autowired
	private ImageRepository imageRepo;
	
	
	public void saveImageToDB(Image img)
	{
		try {
			this.imageRepo.save(img);
		} catch (Exception e) {
			log.error(e);
		}
	}

}
