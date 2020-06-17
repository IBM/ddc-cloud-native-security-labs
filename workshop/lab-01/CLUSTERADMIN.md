### Kubernetes Networking

Before we start and create our own Ingress object, let's review some of the network management tasks on an IBM Cloud Kubernetes (IKS) service. Note that for these labs you will not have full account permissions, so you do not have sufficient permissions to execute all the commands, but please follow along with the presentor's demo or review of the commands listed here. These commands are just extra information that will help you manage an account and clusters on IBM Cloud.

When you created a Service of type LoadBalancer, a NodePort was created as well. To access the application via the service NodePort, you can get the public IP address of the worker nodes and the NodePort of the Service. 

With a LoadBalancer type Service, you can access the service via an External IP, but the pods still are accessible via the public IP of the worker node and the NodePort as well as via the External IP of the NLB, unless you restrict access to your nodes.

List Service details,

```
$ kubectl get svc helloworld
NAME    TYPE    CLUSTER-IP    EXTERNAL-IP    PORT(S)    AGE
helloworld   LoadBalancer   172.21.161.255   169.48.67.163   8080:31777/TCP   31m
```

List the worker nodes on the cluster,

Note: the following commands require account management permissions, just review the next steps. 

Retrieve the worker nodes via the following cli command,
```
$ ibmcloud ks worker ls --cluster $CLUSTERNAME
OK
ID    Public IP    Private IP    Flavor    State    Status    Zone    Version
kube-br9v078d0qi43m0e31n0-remkohdevik-default-0000014b   150.238.93.101   10.187.222.149   b3c.4x16.encrypted   normal   Ready    dal13   1.16.10_1533   
kube-br9v078d0qi43m0e31n0-remkohdevik-default-000002e0   150.238.93.100   10.187.222.146   b3c.4x16.encrypted   normal   Ready    dal13   1.16.10_1533
```

You can access the application then via the worker node's public IP address and Service NodePort, e.g. at http://150.238.93.101:31777.


If you created a standard cluster in the very beginning, IKS automatically provisioned a portable public subnet and a portable private subnet for the VLAN. You need account permissions to list all subnets on your account, e.g.

```
$ ibmcloud ks subnets --provider classic
2422910    10.186.196.112/29    10.186.196.113    2847992    private    br0ktged0io7g05iakcg    dal13
1609575    169.60.156.136/29    169.60.156.137    2847990    public    br0ktged0io7g05iakcg    dal13
```

or to list the resources for the cluster, e.g.

```
$ ibmcloud ks cluster get --show-resources -c $CLUSTERNAME
Retrieving cluster remkohdev-iks116-2n-cluster and all its resources...
OK
                                   
Name:                           remkohdev-iks116-2n-cluster   
ID:                             br9v078d0qi43m0e31n0   
State:                          normal   
Created:                        2020-05-31T17:58:58+0000   
Location:                       dal13   
Master URL:                     https://c108.us-south.containers.cloud.ibm.com:30356   
Public Service Endpoint URL:    https://c108.us-south.containers.cloud.ibm.com:30356   
Private Service Endpoint URL:   https://c108.private.us-south.containers.cloud.ibm.com:30356   
Master Location:                Dallas   
Master Status:                  Ready (5 hours ago)   
Master State:                   deployed   
Master Health:                  normal   
Ingress Subdomain:              remkohdev-iks116-294603-2bef1f4b4097001da9502000c44fc2b2-0000.us-south.containers.appdomain.cloud   
Ingress Secret:                 remkohdev-iks116-294603-2bef1f4b4097001da9502000c44fc2b2-0000   
Workers:                        2   
Worker Zones:                   dal13   
Version:                        1.16.10_1533   
Creator:                        remkohdev@us.ibm.com   
Monitoring Dashboard:           -   
Resource Group ID:              fdd290732f7d47909181a189494e2990   
Resource Group Name:            default   

Subnet VLANs
VLAN ID   Subnet CIDR        Public   User-managed   
2847992   10.208.29.72/29    false    false   
2847990   169.48.67.160/29   true     false    
```

The portable public subnet provides 5 usable IP addresses. 1 portable public IP address is used by the default public Ingress ALB. The remaining 4 portable public IP addresses can be used to expose single apps to the internet by creating public network load balancer services, or NLBs.

To list all of the portable IP addresses in the IKS cluster, both used and available, you can retrieve the following `ConfigMap` in the `kube-system` namespace listing the resources of the subnets,

```
$ kubectl get cm ibm-cloud-provider-vlan-ip-config -n kube-system -o yaml

apiVersion: v1
kind: ConfigMap
data:
  cluster_id: br9v078d0qi43m0e31n0
  reserved_private_ip: ""
  reserved_private_vlan_id: ""
  reserved_public_ip: ""
  reserved_public_vlan_id: ""
  vlanipmap.json: |-
    {
      "vlans": [
        {
          "id": "2847992",
          "subnets": [
            {
              "id": "2086403",
              "ips": [
                "10.208.29.74",
                "10.208.29.75",
                "10.208.29.76",
                "10.208.29.77",
                "10.208.29.78"
              ],
              "is_public": false,
              "is_byoip": false,
              "cidr": "10.208.29.72/29"
            }
          ],
          "zone": "dal13",
          "region": "us-south"
        },
        {
          "id": "2847990",
          "subnets": [
            {
              "id": "2387344",
              "ips": [
                "169.48.67.162",
                "169.48.67.163",
                "169.48.67.164",
                "169.48.67.165",
                "169.48.67.166"
              ],
              "is_public": true,
              "is_byoip": false,
              "cidr": "169.48.67.160/29"
            }
          ],
          "zone": "dal13",
          "region": "us-south"
        }
      ],
      "vlan_errors": [],
      "reserved_ips": []
    }
```

One of the public IP addresses on the public VLAN's subnet is assigned to the NLB. To list the registered NLB host names and IP addresses in a cluster, you need account access permissions,

```
$ ibmcloud ks nlb-dns ls --cluster $CLUSTERNAME

OK
Hostname    IP(
s)    Health Monitor    SSL Cert Status    SSL Cert Secret Name    Secret Namespace    
remkohdev-iks116-294603-2bef1f4b4097001da9502000c44fc2b2-0000.us-south.containers.appdomain.cloud    169.48.67.162    None    created    remkohdev-iks116-294603-2bef1f4b4097001da9502000c44fc2b2-0000    default  
```

This last command also gives you the default Ingress Subdomain and Ingress Secret that IKS already created by default. You can use these in the next section, configuring the Ingress resource.

You see that the portable IP address `169.48.67.162` is assigned to the NLB. You can access the application via the portable IP address of the NLB and service NodePort at http://169.48.67.162:31777.
