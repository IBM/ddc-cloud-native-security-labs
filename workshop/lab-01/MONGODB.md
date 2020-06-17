### Deploy MongoDB to IKS Cluster and Persist its Datastore in IBM Cloud Object Storage

In this section, you are going to deploy an instance of MongoDB to your IKS cluster. We will disable persistence for simplicity.

1. To install the Bitnami/MongoDB chart, you need Helm v3. At the time of writing, by default, Helm v2.16 was installed on the `Cloud Shell`. 

1. In the `Cloud Shell`, download and unzip Helm v3.

    ```
    $ wget https://get.helm.sh/helm-v3.2.0-linux-amd64.tar.gz
    $ tar -zxvf helm-v3.2.0-linux-amd64.tar.gz
    ```

1. Make Helm v3 CLI available in your `PATH` environment variable.

    ```
    $ echo 'export PATH=$HOME/linux-amd64:$PATH' > .bash_profile
    $ source .bash_profile
    ```

1. Verify Helm v3 installation.

    ```
    $ helm version --short

    v3.2.0+ge11b7ce
    ```

1. Add the bitnami Helm repository to your repositories and update the local repository.

    ```
    $ helm repo add bitnami https://charts.bitnami.com/bitnami
    "bitnami" has been added to your repositories

    $ helm repo update
    Hang tight while we grab the latest from your chart repositories...
    ...Successfully got an update from the "bitnami" chart repository
    Update Complete. ⎈ Happy Helming!⎈
    ```

2. Now, install MongoDB using helm with the following parameters. Note, that I am deliberately opening NodePort for MongoDB for the purpose of the lab.

    ```
    $ helm install mongodb --set persistence.enabled=false,livenessProbe.initialDelaySeconds=180,usePassword=true,mongodbRootPassword=passw0rd,mongodbUsername=user1,mongodbPassword=passw0rd,mongodbDatabase=mydb bitnami/mongodb --set service.type=NodePort,service.nodePort=30001

    NAME: mongodb
    LAST DEPLOYED: Sat May 23 21:04:44 2020
    NAMESPACE: default
    STATUS: deployed
    REVISION: 1
    TEST SUITE: None
    NOTES:
    ** Please be patient while the chart is being deployed **

    MongoDB can be accessed via port 27017 on the following DNS name from within your cluster:
        mongodb.default.svc.cluster.local

    To get the root password run:

        export MONGODB_ROOT_PASSWORD=$(kubectl get secret --namespace default mongodb -o jsonpath="{.data.mongodb-root-password}" | base64 --decode)

    To get the password for "my-user" run:

        export MONGODB_PASSWORD=$(kubectl get secret --namespace default mongodb -o jsonpath="{.data.mongodb-password}" | base64 --decode)

    To connect to your database run the following command:

        kubectl run --namespace default mongodb-client --rm --tty -i --restart='Never' --image docker.io/bitnami/mongodb:4.2.7-debian-10-r0 --command -- mongo admin --host mongodb --authenticationDatabase admin -u root -p $MONGODB_ROOT_PASSWORD

    To connect to your database from outside the cluster execute the following commands:

        kubectl port-forward --namespace default svc/mongodb 27017:27017 & mongo --host 127.0.0.1 --authenticationDatabase admin -p $MONGODB_ROOT_PASSWORD
    ```

3. Note, the service type for MongoDB is set to `NodePort` with the Helm parameter `--set service.type=NodePort`, and the nodePort value is set to `30001`. Normally, you will set MongoDB to be accessed only within the cluster using the type `ClusterIP`.

4. Patch the NodePort to type `LoadBalancer` for ease of use, cause it will give you an External IP, and it will allow you to do a simple liveness test.

    ```
    $ kubectl patch svc mongodb -p '{"spec": {"type": "LoadBalancer"}}'
    service/mongodb patched
    ```

5. As mentioned before, in a production environment you don't want to allow external access to your database.

6. Retrieve and save MongoDB passwords in environment variables.

    ```
    $ export MONGODB_ROOT_PASSWORD=$(kubectl get secret --namespace default mongodb -o jsonpath="{.data.mongodb-root-password}" | base64 --decode)

    $ export MONGODB_PASSWORD=$(kubectl get secret --namespace default mongodb -o jsonpath="{.data.mongodb-password}" | base64 --decode)

    $ echo $MONGODB_ROOT_PASSWORD
    passw0rd

    $ echo $MONGODB_PASSWORD
    passw0rd
    ```

7. Verify the MongoDB deployment.

    ```
    $ kubectl get deployment

    NAME      READY   UP-TO-DATE   AVAILABLE   AGE
    mongodb   1/1     1            1           6m30s
    ```

    > Note: It may take 1 to 2 minutes until the deployment is completed and the container initialized, wait till the READY state is 1/1

8. Verify that pods are running.

    ```
    $ kubectl get pod

    NAME                      READY   STATUS    RESTARTS   AGE
    mongodb-9f76c9485-sjtqx   1/1     Running   0          5m40s
    ```

    > Note: It may take a few minutes until the deployment is completed and pod turns to `Running` state.

    ```
    $ kubectl get svc mongodb
    NAME      TYPE           CLUSTER-IP      EXTERNAL-IP     PORT(S)           AGE
    mongodb   LoadBalancer   172.21.97.193   169.48.67.164   27017:30001/TCP   39s
    ```

9.  try to connect to MongoDB using the EXTERNAL IP and NodePort of your `mongodb` service, retrieved in the previous step: 

    ```
    $ curl http://169.48.67.164:30001
    It looks like you are trying to access MongoDB over HTTP on the native driver port.
    ```

10. The reply `It looks like you are trying to access MongoDB over HTTP on the native driver port.` means that you hit the MongoDB service, which in its turn does not allow `http` access.

Go back to the [Lab](README.md) and continue with the next step to deploy the HelloWorld application.