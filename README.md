# JavaTest-Account-Statements
Spring boot application uses MS as DB to search history statements of the account 

# For building and running the application you need:

* JDK 11
* Maven 


# Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to use spring boot maven plugin. Navigate to the application folder and run below command:

```bash

mvn spring-boot:run

```

# To run the unit test you can also use maven plugin as below:


```bash

mvn clean test

```

Before running the application make sure the MS access file is in the same folder as the project or change its path in application.properties file accordingly.  
