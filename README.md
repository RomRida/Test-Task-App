# Test Task App

### About 
This is an app with couple endpoints (both of them have only POST method exposed):

1) `api/v1/login`
2) `api/v1/messages`

* First `/login` endpoint accepts json messages formatted like this:
    ```json
    {
      "name":"username",
      "password":"password"
    }
    ```
    And _(assuming username and password correct)_ will return:
    ```json
    {
      "token": "Encoded JWT token"
    }
    ```
* Second `/messages` endpoint accepts json messages formatted like this:
  ```json
  {
    "name":"username",
    "message":"history 5"
  }
  ```
  If message will contain word `history` followed by some number (as shown above) it will return a list of messages 
  owned by the user specified in the original message **(be careful, it is case sensitive)**.
  ```json
   {
     "name":"username",
     "message":"history book"
   }
   ```
  Otherwise (as shown above), it creates a new message on behalf of the user specified in the original message and 
  return created message as conformation

### Local launch 

For local app launch from your IDE of choice you would need Docker and JDK 11 or above.

You have two main ways to launch this app:

1) First navigate to `\docker\local\ ` directory located inside root of the project.
   Use `docker pull romrida/skilltestapp:0.0.3` command to pull image of this app from docker hub; 
   From terminal launch `docker-compose up` command; 
   Postgres Database and App will launch after that, and you can start freely using them 

3) If you choose to run the application from the IDE, you will either need to define environment variables (listed below),
   or just run the app with a `local` spring profile where all the environment variables are already defined 
  (Already defined verifiable in local profile tuned to work with postgresql database launched from Docker compose, 
   you may use your own local database, but then you may need to change some variables)
    ```table
   | Environmental variable |            Definition           |
   |:----------------------:|:-------------------------------:|
   | POSTGRES_HOST          | define postgresql host          |
   | POSTGRES_PORT          | define postgresql port          |
   | POSTGRES_DATABASE      | define postgresql database name |
   | POSTGRES_USER          | define postgresql user          |
   | POSTGRES_PASSWORD      | define postgresql password      |
   | JWT_SECRET             | define jwt secret               |
    ```
* You also may find helpful curl commands located in `\curl\ ` repository inside curl.txt file, 
  use theme to quickly test main functionality. First command lets you acquire token, witch you 
  need to insert in two commands below in place of `*token goes here*`
  
