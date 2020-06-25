kubectl delete namespace test
kubectl delete networkpolicy test-deny -n test
kubectl delete networkpolicy allow-all-ingress
kubectl delete ingress helloworld-ingress
kubectl delete deploy helloworld
kubectl delete svc helloworld
helm uninstall mongodb