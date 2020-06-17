## Optional: Connect to MongoDB within a Java Spring Boot App

A sample Java Spring Boot application is provided as part of this repo to demonstrate how you can access a MongoDB database from a Java application.

1. Open `cloud-native-security-master/workshop/lab-02/java/src/main/resources/application.properties` file in a file editor.

2. Change its contents to reflect your MongoDB connection information. For example,

    ```
    spring.data.mongodb.authentication-database=admin
    spring.data.mongodb.username=user1
    spring.data.mongodb.password=passw0rd
    spring.data.mongodb.database=mydb
    spring.data.mongodb.port=27017
    spring.data.mongodb.host=mongodb
    spring.data.mongodb.uri=mongodb://user1:passw0rd@mongodb:27017/mydb
    ```

3. Save the changes.

4. In `Cloud Shell`, navigate to the sample app folder.

    ```
    $ cd cloud-native-security-master/workshop/lab-02/java 
    ```

5. Run the sample application.

    ```
    $ ./mvnw spring-boot:run
    ```

 1. If your sample application is executed successfully, you should find the following information toward the end of its output.

    ```
    Start working with Mongo DB

    Clean up any existing customer data:
    ------------------------------------
    2020-05-30 00:10:04.349  INFO 41598 --- [           main] org.mongodb.driver.connection            : Opened connection  [connectionId{localValue:2, serverValue:660}] to 184.172.250.155:30001

    Populate customer data:
    -----------------------

    Customers found with findAll() method:
    --------------------------------------
    Customer[id=5ed1eaaca03193778dd6521a, firstName='Alice', lastName='Smith']
    Customer[id=5ed1eaaca03193778dd6521b, firstName='Bob', lastName='Smith']

    Customer found with findByFirstName('Alice') method:
    ----------------------------------------------------
    Customer[id=5ed1eaaca03193778dd6521a, firstName='Alice', lastName='Smith']

    Customers found with findByLastName('Smith') method:
    ----------------------------------------------------
    Customer[id=5ed1eaaca03193778dd6521a, firstName='Alice', lastName='Smith']
    Customer[id=5ed1eaaca03193778dd6521b, firstName='Bob', lastName='Smith']

    End working with Mongo DB
    ```

### Optional Verification

You can verify the data entry via the sample Java app through mongoDB shell.

1. Execute the same query as you did in the previous sections, you retrieve two different data entries.

    ```
    > db.customer.find( { lastName: "Smith" } )

    { "_id" : ObjectId("5ed1eaaca03193778dd6521a"), "firstName" : "Alice", "lastName" : "Smith", "_class" : "com.example.accessingdatamongodb.Customer" }
    { "_id" : ObjectId("5ed1eaaca03193778dd6521b"), "firstName" : "Bob", "lastName" : "Smith", "_class" : "com.example.accessingdatamongodb.Customer" }
    ```

If you are interested in verify the data persistency of the mongoDB database, you may remove the mongoDB from the IKS cluster and install it again. 

1. Remove `mongodb` deployment.

    ```
    helm delete mongodb
    ```

1. Re-deploy `mongodb` to IKS cluster.

    ```
    helm install mongodb bitnami/mongodb --set persistence.enabled=true --set persistence.existingClaim=my-iks-pvc --set livenessProbe.initialDelaySeconds=180 --set mongodbRootPassword=passw0rd --set mongodbUsername=user1 --set mongodbPassword=passw0rd --set mongodbDatabase=mydb --set service.type=NodePort --set service.nodePort=30001
    ```

After `mongodb` redeployment, you can verify the existing data in the mongoDB database via MongoDB shell.

## Conclusion

Congratulation! You have successfully deployed a MongoDB server to IKS cluster and `persistant its data` in IBM Cloud Object Storage. Becuase the IBM Cloud Object Storage offers data encryption and other rich security features out of box, you achieved `data-in-rest` objective.

There are many options to run a database in Cloud environment. The repo does not suggest one way is better than another. The use case in this repo is to demonstrate `data-in-rest` via IBM Cloud Object Storage.

