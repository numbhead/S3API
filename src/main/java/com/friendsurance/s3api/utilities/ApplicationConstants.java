package com.friendsurance.s3api.utilities;

import java.util.Date;

import com.amazonaws.regions.Regions;

public class ApplicationConstants {
	
	static String  fileSeparator = System.getProperty("file.separator");
	static Date currDate = new Date();

	public static final String CLIENTREGION = Regions.US_WEST_2.getName();;
	public static final String BUCKETNAME = "s3apiimagebucket";
	public static final String FILEUPLOAD = currDate.getTime()+"";
	
}
