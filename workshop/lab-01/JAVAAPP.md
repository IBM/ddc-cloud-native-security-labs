# Deploy a HelloWorld App with ClusterIP

1. Create the Kubernetes deployment resource,

    ```
    $ echo 'apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: helloworld
      labels:
        app: helloworld
    spec:
      replicas: 1
      selector:
        matchLabels:
          app: helloworld
      template:
        metadata:
          labels:
            app: helloworld     
        spec:
          containers:
          - name: helloworld
            image: remkohdev/helloworld:lab1v1.0
            ports:
            - name: http-server
              containerPort: 8080' > deployment.yaml
    ```

1. Create the kubernetes Service resource,

    ```
    $ echo 'apiVersion: v1
    kind: Service
    metadata:
      name: helloworld
      labels:
        app: helloworld
    spec:
      ports:
      - port: 8080
        targetPort: http-server
      selector:
        app: helloworld' > service.yaml
    ```

1. Deploy the Kubernetes Deployment resource for HellWorld,

    ```
    $ kubectl create -f deployment.yaml
    deployment.apps/helloworld created
    ```

2. Check the deployment,

    ```
    $ kubectl get all
    NAME    READY    STATUS    RESTARTS    AGE
    pod/helloworld-5f8b6b587b-5tcd7    1/1    Running
    0    4m56s
    pod/mongodb-867d8f9796-2tqb2    1/1    Running    0    46m

    NAME    TYPE    CLUSTER-IP    EXTERNAL-IP    PORT(S)    AGE
    service/kubernetes   ClusterIP    172.21.0.1    <none>      443/TCP    7d
    service/mongodb     LoadBalancer    172.21.97.193    169.48.67.164    27017:30001/TCP    46m

    NAME    READY    UP-TO-DATE    AVAILABLE    AGE
    deployment.apps/helloworld    1/1    1    1    4m56s
    deployment.apps/mongodb    1/1    1     1    46m

    NAME     DESIRED    CURRENT    READY    AGE
    replicaset.apps/helloworld-5f8b6b587b    1    1    1    4m56s
    replicaset.apps/mongodb-867d8f9796    1    1    1    46m
    ```

Go back to the next step in the [lab](README.md).

