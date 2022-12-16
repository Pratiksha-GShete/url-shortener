Overview
A URL shortener service creates a short url against a long url. Moreover, when user click on the short url, it gets redirected to original url.
Long URL: https://github.com/Pratiksha-GShete/url-shortener/
Short URL :  http://localhost:8080/api/url-shortener/jf5RDLqJ9

Tech stack overview
•	Kotlin
•	spring-boot-starter-web (Spring boot framework V2.6.0)
•	Swagger 2.9.2 (API Doc)
•	Swagger annotation
•	spring-boot-starter-actuator (for API statistics)
•	spring boot jpa
•	Oracle 
•	Google Cache
•	docker 
•	commons-validator:2.0.1
•	h2 (Test Database)
•	spring-boot-starter-test
Design Diagram

![image](https://user-images.githubusercontent.com/120738012/208180944-58ed9404-81e6-4bed-9f2e-fa2c3721b865.png)

 
Build an executable JAR
Run the application by using ./gradlew bootRun. Alternatively, can build the JAR file by using ./gradlew build and then run the JAR file, as follows:
java -jar urlShortener-0.0.1-SNAPSHOT.jar
Containerize It
docker build --build-arg JAR_FILE=build/libs/\*.jar -t springio/gs-spring-boot-docker 

URL table

Below are the columns for the URL Table
LONG_URL  -> long url detail
SHORT_URL  -> Short url details
CREATION_DATE_TIME - Date of creation
EXPIRE_DATE_TIME  -> Expire Date of Short Url


Flow:

Create Shortener Url from Long URL 
   1 Valide the Url and Syntax 
   2 Check the long url present in cache if its present then check expire date if its expired then create new short url and update expire date and send to user ,if its not expired then send resonse with Short url is already present 
   3 If Url is not present in Cache then create new Url and persist in DB and put in Cache
   
 Get Api for Long Url
    1 fetch the URL from Db and Cache
   




Test Case 1:  Create Shorter Url  against a long Url

Request:![image](https://user-images.githubusercontent.com/120738012/208181145-30a962fc-4ca9-4d5e-b8ca-e9d426029f72.png)
  
Curl Command
curl -X POST "http://localhost:8080/api/url-shortener/" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"fullUrl\": \"https://github.com/Pratiksha-GShete/url-shortener/\"}"
Response:


![image](https://user-images.githubusercontent.com/120738012/208181218-98faaa70-4702-479c-85ca-14f9bb23fc6a.png)

![image](https://user-images.githubusercontent.com/120738012/208183440-814145cc-a278-46e1-9dad-1ad4fb06b84c.png)
![image](https://user-images.githubusercontent.com/120738012/208183444-959d9557-0679-48d7-b848-3a39d463bb52.png)



 

Test Case 2: Get Long Url from Short Url
Request:

![image](https://user-images.githubusercontent.com/120738012/208182731-ac2bbc66-b1ef-4064-9c88-d2dcba9a7276.png)


Curl Command:
curl -X GET "http://localhost:8080/api/longurl/jf5RDLqJ9" -H "accept: */*"

Response:
![image](https://user-images.githubusercontent.com/120738012/208182798-2eb3ee3b-f646-4e95-ae6b-0031b25df15f.png)


 

Test Case 3: Invalid Validation with wrong Short Url


![image](https://user-images.githubusercontent.com/120738012/208182870-dc7e4266-5381-4811-aa30-77935b6d272e.png)


Test Case 4: Invalid Url

![image](https://user-images.githubusercontent.com/120738012/208182964-4c3d27bd-2190-4391-aaf3-cb933860a72c.png)



 

 





 



