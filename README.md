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

1) First navigate to `docker.local` directory from terminal and from there launch docker-compose up command;
2) 


