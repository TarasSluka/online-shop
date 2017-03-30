# Online-Shop
In this project i realised a simple online market, with ability to support many product domains  trees.
The system is designed following the principle of client-server architecture.

## How to run in production environments
* Download project from git repository.
* Create db with name "online-shop"
* Change db config(userName, password) in application.properies 
* Edit application.properies with names "localPathToProductImage", "localPathToUserImage" and set absolute  path to shop folder   
* Running could be performed in two ways:
	* run spring boot main class :
		1. comment dependency in pom.xml with artifactid "spring-boot-starter-tomcat,
		2. comment "extends SpringBootServletInitializer" and "configure" method in OnlineShopApplication.java 
		3. Run OnlineShopApplication.java
	* build war file and deploy it to servlet container (was tested on Tomcat 9)
## Overview
System supports next user types
* anonymous - just a guest
* user - plays buyer role (username - "user2", password - "11111111")
* admin (username - "admin", password - "11111111")

Each user has certain features.
* ROLE_ANONYMOUS
	* log in
	* registration
	* product review
	* add / remove product in basket
	* basket review
	* product search on keyword with ability to search on certain category
	* filter products by price
	* sort products by price and date of addition
* ROLE_USER
	* inherits all features from anonymous
	* personal cabinet view 
	* change personal data
	* change password
	* add / change avatars 
* ROLE_ADMIN
	* add / remove / edit products
	* add / remove / edit  categories of products
	* search, block, remove, filter users
	* ability to search for products that are not linked to a certain category
	* Following the principle of client server architecture there are next two pars:

### The server side: 
* Tomcat - as a servlet container, where app is running
* Maven - for dependency management and build. 
* Application back-end is created based on mvc pattern, spring-mvc was used as implementation of this pattern. 
	* model - for data access is used spring-data-jpa, 
	* view - thymeleaf templates
	* controller - spring controller
* Authorization and authentication is implemented with spring security.
* Used relational database - mysql

### The client side:
* bootstrap - for styling the front-end
* angular version  1.5.1
	* angular-ui-router -  used to manage the url and its templates
	* angular-resource - used to exchange data between the server and the client on the basis of rest api.

### Some additional features
* Basket synchronization on login - When user is not authenticated he can add products into the basket, and they will be persisted in localStorage, after user becomes authenticated, synchronization of products between localStorage and server is starting, localStorage becomes empty, and result from server transmits to client, and next operation with basket will be working directly with server.



	

