## Troubleshooting

######################################################
Additional steps for IBM Kubernetes Service(IKS) only:
######################################################

  1. If the plugin pods show an "ErrImagePull" or "ImagePullBackOff" error, verify that the image pull secrets to access IBM Cloud Container Registry exist in the "kube-system" namespace of your cluster.

     $ kubectl get secrets -n kube-system | grep icr-io

     Example output if the secrets exist:
     ----o/p----
     kube-system-icr-io
     kube-system-us-icr-io
     kube-system-uk-icr-io
     kube-system-de-icr-io
     kube-system-au-icr-io
     kube-system-jp-icr-io
     ----end----

  2. If the secrets do not exist in the "kube-system" namespace, check if the secrets are available in the "default" namespace of your cluster.

     $ kubectl get secrets -n default | grep icr-io

  3. If the secrets are available in the "default" namespace, copy the secrets to the "kube-system" namespace of your cluster. If the secrets are not available, continue with the next step.

     $ kubectl get secret -n default default-icr-io -o yaml | sed 's/default/kube-system/g' | kubectl -n kube-system create -f -
     $ kubectl get secret -n default default-us-icr-io -o yaml | sed 's/default/kube-system/g' | kubectl -n kube-system create -f -
     $ kubectl get secret -n default default-uk-icr-io -o yaml | sed 's/default/kube-system/g' | kubectl -n kube-system create -f -
     $ kubectl get secret -n default default-de-icr-io -o yaml | sed 's/default/kube-system/g' | kubectl -n kube-system create -f -
     $ kubectl get secret -n default default-au-icr-io -o yaml | sed 's/default/kube-system/g' | kubectl -n kube-system create -f -
     $ kubectl get secret -n default default-jp-icr-io -o yaml | sed 's/default/kube-system/g' | kubectl -n kube-system create -f -

  4. If the secrets are not available in the "default" namespace, you might have an older cluster and must generate the secrets in the "default" namespace.

     i.  Generate the secrets in the "default" namespace.

         $ ibmcloud ks cluster-pull-secret-apply

     ii. Verify that the secrets are created in the "default" namespace. The creation of the secrets might take a few minutes to complete.

         $ kubectl get secrets -n default | grep icr-io

     iii. Run the commands in step 3 to copy the secrets from the "default" namespace to the "kube-system" namespace.

  5. Verify that the image pull secrets are available in the "kube-system" namespace.

     $ kubectl get secrets -n kube-system | grep icr-io

  6. Verify that the state of the plugin pods changes to "Running".

     $ kubectl get pods -n kube-system | grep object

Verify that installation was successful

```
$ kubectl get storageclass | grep 'ibmc-s3fs'
ibmc-s3fs-cold-cross-region            ibm.io/ibmc-s3fs   60s
ibmc-s3fs-cold-regional                ibm.io/ibmc-s3fs   60s
ibmc-s3fs-flex-cross-region            ibm.io/ibmc-s3fs   60s
ibmc-s3fs-flex-perf-cross-region       ibm.io/ibmc-s3fs   60s
ibmc-s3fs-flex-perf-regional           ibm.io/ibmc-s3fs   60s
ibmc-s3fs-flex-regional                ibm.io/ibmc-s3fs   60s
ibmc-s3fs-standard-cross-region        ibm.io/ibmc-s3fs   60s
ibmc-s3fs-standard-perf-cross-region   ibm.io/ibmc-s3fs   60s
ibmc-s3fs-standard-perf-regional       ibm.io/ibmc-s3fs   60s
ibmc-s3fs-standard-regional            ibm.io/ibmc-s3fs   60s
ibmc-s3fs-vault-cross-region           ibm.io/ibmc-s3fs   60s
ibmc-s3fs-vault-regional               ibm.io/ibmc-s3fs   60s

$ kubectl get pods -n kube-system -o wide | grep object
ibmcloud-object-storage-driver-ns64d    1/1    Running    0    2m10s    10.47.79.90    10.47.79.90    <none>    <none>
ibmcloud-object-storage-plugin-79cc466c76-c2kcb    1/1    Running    0    2m10s    172.30.73.78    10.47.79.90    <none>    <none>
```
