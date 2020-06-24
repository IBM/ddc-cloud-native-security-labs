# cloud-native-security-labs

## About Cloud Native Security Conference

The Cloud Native Security Conference is a free digital developer conference includes sessions and hands-on labs led by security experts from IBM and Red Hat for both beginners and experts, about security on Kubernetes.

Sessions and labs are divided into 3 tracks:
- **Application Security** - Securing containerized applications on Kubernetes or microservices. Includes configuration, policies and app governance, and APIs.
- **Data Security** - Securing your application data and analytics. Includes privacy, data storage, encryption, AI/ML, and NIST controls.
- **DevOps Security** - Building a secure CI/CD pipeline. Includes integration, testing, vulnerability scanning, image governance, and automation.

## About this workshop

This labs in Cloud Native Security are part of a series of developer enablement labs for Security on Kubernetes. 

## Agenda

|   |   |
| - | - |
| [Lab0](workshop/lab-00/README.md) | Account Setup and Cluster Access |
| [Lab1](workshop/lab-01/README.md) | Kubernetes Networking, using Service Types to Control Application Access with LoadBalancer and Ingress |
| [Lab2](workshop/lab-02/README.md) | Adding Secure Encrypted Object Storage using a Persistent Volume for MongoDB with S3FS-Fuse |
| [Lab3](workshop/lab-03/README.md) | Create a Custom Builder Image for Source-to-Image (S2I) |

## Pre-requirements

You need access to a Kubernetes cluster:
- Lab 1: requires a standard cluster with at least 2 worker nodes to enable LoadBalancer and Ingress. You need a client with `ibmcloud cli` and `kubectl cli` installed.
- Lab 2: requires a standard cluster with at least 1 worker node and permissions to add PersistentVolume and PersistentVolumeClaim resources. You need a client with `ibmcloud cli`, `kubectl cli` and `helm cli` installed.
- Lab 3: requires permission to an OpenShift cluster with at least 1 worker node. You need a client with `ibmcloud cli`, `kubectl cli` and `oc cli` installed.

For all labs you can use the IBM Cloud Shell client, which you can access via https://shell.cloud.ibm.com.

## Markdown lint tool (Option)

Install the [Markdown lint tool](https://github.com/markdownlint/markdownlint),
```
$ npm install -g markdownlint-cli
```

To use markdownlint, run the following command,
```
$ markdownlint workshop -c ".markdownlint.json" -o mdl-results.md
```

## Build Gitbook (Option)

Install the [gitbook-cli](https://github.com/GitbookIO/gitbook-cli),
```
$ npm install -g gitbook-cli
```

To build the Gitbook files into the `_book` sub-directory with the `gitbook-cli`, run the following command,
```
$ gitbook build ./workshop
```

Serve the Gitbook files locally with the following command,
```
$ gitbook serve ./workshop
```
