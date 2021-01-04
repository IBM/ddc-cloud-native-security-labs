# Delete your Resources

```bash
kubectl delete secret cos-write-access
```

## Removing the IBM OS Plugin

<https://hub.helm.sh/charts/ibm-charts/ibm-object-storage-plugin>
<https://cloud.ibm.com/docs/containers?topic=containers-object_storage>

Installed in `default` namespace

Verify that you do not have any PVCs or PVs in your cluster that use IBM Cloud Object Storage.

List all pods that mount a specific PVC.

```bash
kubectl get pods --all-namespaces -o=jsonpath='{range .items[*]}{"\n"}{.metadata.name}{":\t"}{range .spec.volumes[*]}{.persistentVolumeClaim.claimName}{" "}{end}{end}' | grep "<pvc_name>"
```

If one or more pod is returned, remove the pods or deployment before removing the Helm chart.

Find the installation name of your Helm chart.

```bash
helm ls --all --all-namespaces | grep ibm-object-storage-plugin
```

```bash
helm delete <helm_chart_name> -n <helm_chart_namespace>

helm delete ibm-object-storage-plugin -n default
```

Remove the ibmc Helm plug-in.

Remove the plug-in.

```bash
helm plugin remove ibmc
```

Verify that the ibmc plug-in is removed.

```bash
helm plugin list
```

Verify that the IBM Cloud Object Storage pods are removed.

```bash
kubectl get pods -n <namespace> | grep object-storage
```

Verify that the storage classes are removed.

```bash
kubectl get storageclasses | grep 'ibmc-s3fs'
```

### Remove the PVC

```bash
kubectl delete pvc my-iks-pvc
```

```bash
get pv
kubectl get pv
delete pv
```

### Remove MongoDB

```bash
helm uninstall mongodb
```
