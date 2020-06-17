## Create a Java Spring Boot App and Connect to MongoDB

See: https://github.com/spring-projects/spring-data-book/blob/master/mongodb]
See: https://docs.spring.io/spring-data/mongodb/docs/3.0.0.RELEASE/reference/html/#reference

```
$ spring init --dependencies=web,data-rest,thymeleaf guestbook-api
$ cd guestbook-api
$ mvn clean install
$ mvn test
$ mvn spring-boot:run
```

Create the APIController,
```
$ echo 'package com.example.guestbookapi;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class APIController {

  @Autowired
  private MessageRepository repository;
    
  @GetMapping("/api")
  public String index() {
    return "Welcome to Spring Boot App";
  }

  @RequestMapping(value = "/api/hello", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String hello(@RequestParam String name) {
    String message = "Hello "+ name;
    String responseJson = "{ \"message\" : \""+ message + "\" }";
    repository.save(new Message(name, message));
    for (Message msg : repository.findAll()) {
        System.out.println(msg);
    }
    return responseJson;
  }

  @RequestMapping(value = "/api/messages", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<Message>> getMessages() {
    List<Message> messages = (List<Message>) repository.findAll();
    return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
  }
}
' > src/main/java/com/example/guestbookapi/APIController.java
```

Create the Message class,
```
$ echo 'package com.example.guestbookapi;

import org.springframework.data.annotation.Id;

public class Message {

  @Id
  private String id;
  
  private String sender;
  private String message;

  public Message(String sender, String message) {
    this.sender = sender;
    this.message = message;
  }

  public String getId() {
    return id;
  }
  public String getSender() {
    return sender;
  }
  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "Message [id=" + id + ", sender=" + sender + ", message=" + message + "]";
  }
}' > src/main/java/com/example/guestbookapi/Message.java
```

Create the MessageRepository class,
```
echo 'package com.example.guestbookapi;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

	public List<Message> findBySender(String sender);

}' > src/main/java/com/example/guestbookapi/MessageRepository.java
```

Add a new file ‘~/src/main/resources/application.properties’,
```
echo 'spring.data.mongodb.authentication-database=admin
spring.data.mongodb.username=user1
spring.data.mongodb.password=passw0rd
spring.data.mongodb.database=messagesdb
spring.data.mongodb.port=27017
spring.data.mongodb.host=mongodb' > src/main/resources/application.properties
```

## Test Java App on localhost:

Create local Mongodb,
```
docker run --name mongo -d -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=user1 -e MONGO_INITDB_ROOT_PASSWORD=passw0rd  mongo
```

Get IP address on mac osx,
```
$ ifconfig en0 | grep inet
```

Configure application.properties and change the host to your local IP Address to test the app on localhost.
```
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.username=user1
spring.data.mongodb.password=passw0rd
spring.data.mongodb.database=messagesdb
spring.data.mongodb.port=27017
spring.data.mongodb.host=192.168.1.4
```

Clean, install and run,
```
$ mvn clean install
$ mvn spring-boot:run
```

Test it your messages are saved and retrieved,
```
$ curl -X GET 'http://127.0.0.1:8080/api/hello?name=remko'
{ "message" : "Hello remko" }
$ curl -X GET 'http://127.0.0.1:8080/api/hello?name=emily'
{ "message" : "Hello emily" }

$ curl --location --request GET 'http://127.0.0.1:8080/api/messages'
[{"id":"5ec9e4912b3d9d5451fe463a","sender":"remko","message":"Hello remko"},{"id":"5ec9e4a52b3d9d5451fe463b","sender":"emily","message":"Hello emily"}]
```

## Dockerize

```
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```


## Deploy to Kubernetes

TODO: set spring boot config at deploy time via secret,
TODO: add Dockerfile, add Deployment files, deploy

