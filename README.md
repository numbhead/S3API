# S3API

A REST API that has two endpoint URLs

 1. Saves image to S3 Bucket.
 
      A HTTP Post request on http://<Host_Name>/s3api/image/saveImageToS3
      
      Accepts input of an image name and a download URL of the image.
      
      The API downloads the image, saves the image to AWS S3 and records details in MySQL DB.
      
      
            example of a json Request Body
              {    
                   "name": "swan.jpg",
                    "sourceLink": "http://wallpaperswide.com/download/beautiful_swan_2-wallpaper-960x600.jpg"
              }
            
            Sample Response : 
              {
                  "id": 3,
                  "name": "swan.jpg",
                  "sourceLink": "http://wallpaperswide.com/download/beautiful_swan_2-wallpaper-960x600.jpg",
                  "s3Link": "https://s3apiimagebucket.s3.us-west-2.amazonaws.com/swan.jpg",
                  "dateOfStorage": "2018-11-06T07:47:23.747+0000"
              }
              
    
   2. List all images from S3 Bucket.
   
        A HTTP Get request on http://<Host_Name>/s3api/image/getAllImageList
        
          Sample Response :
          [
              {
                "imageS3Link": "https://s3apiimagebucket.s3.us-west-2.amazonaws.com/grandpa.jpg",
                "imageName": "grandpa.jpg"
              },
              {
                "imageS3Link": "https://s3apiimagebucket.s3.us-west-2.amazonaws.com/swan.jpg",
                "imageName": "swan.jpg"
              }
          ]
