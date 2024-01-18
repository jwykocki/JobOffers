# JobOffers
## Web application helping you find the best job offer for Junior Java Developer!

### Description
JobOffers is a Spring Boot web application that allows you to collect job offers from various sources for Junior Java Developers (websites, other web applications).
The main function of the application is fetching offers form external server in scheduled time. In order to receive offers, user has to register to website. After that, using generated token, user can get offers or add a new one by themselves. In order to connect to the real-time job offer fetcher, saved offers are removed at each scheduler interval. For a better presentation simple frontend in React is made. Everything is deployed on AWS and available at:  

http://ec2-16-171-15-220.eu-north-1.compute.amazonaws.com

> **Author:** Jakub Wykocki  
> **Linkedin:** https://www.linkedin.com/in/jakub-wykocki/   

### Specification
Spring Boot web application  
Facade design pattern  
Modular monolith hexagonal architecture  
NoSQL databases (MongoDB) for storing offers and users data  
Code coverage with unit tests  
Integration tests  
Scheduled fetching offers from external server  
Controllers tested via mockMvc  
Token authentication  
Redis cache  
Simple frontend in React  
Application deployed on AWS Linux EC2 server  

### Technologies

Core: <br>
![image](https://img.shields.io/badge/17-Java-orange?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring) &nbsp;
![image](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white) &nbsp;

Testing:<br>
![image](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/Testcontainers-9B489A?style=for-the-badge) &nbsp;

### Architecture


    
![diagram.png](https://github.com/jwykocki/JobOffers/blob/master/architecture/diagram.png?raw=true)

### Endpoints

Application provides the following endpoints:  

|   ENDPOINT   | METHOD |              REQUEST              |             RESPONSE             |      FUNCTION       |
|:------------:|:------:|:---------------------------------:|:--------------------------------:|:-------------------:|
|  /register   |  POST  | JSON BODY (username and password) | JSON (username and created flag) |   register a user   |
|    /token    |  POST  | JSON BODY (username and password) |           JSON (token)           |  give user a token  |
|   /offers    |  GET   |       PATH VARIABLE (token)       |          JSON (offers)           |   sending offers    |
| /offers/{id} |  GET   |       PATH VARIABLE (token)       |           JSON (offer)           | finding offer by id |
|   /offers    |  POST  |       PATH VARIABLE (token)       |           JSON (offer)           |  creates new offer  |

### How to use  

**Using frontend:**

Go to: http://ec2-16-171-15-220.eu-north-1.compute.amazonaws.com  
Before using the application, you must login. If you do not have an account, register a new one. When you are logged in, you can get job offers, get one offer by ID or create and save a new job offer.  

**Using endpoints:**

To test app you can use swagger:  
http://ec2-16-171-15-220.eu-north-1.compute.amazonaws.com:8000/swagger-ui/index.html#  
or send requests manually (e.g. Postman) on address:  
http://ec2-16-171-15-220.eu-north-1.compute.amazonaws.com:8000  
by adding proper /endpoint (/register, /token, /offers)

### In progress  
- connection with real-time job offer web-scrapper (my small project in Python)

### Contact

**Author:** Jakub Wykocki  
**Linkedin:** https://www.linkedin.com/in/jakub-wykocki/   

If you like this project, please hit the star button, thank you!




