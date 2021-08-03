echo 'Couchbase preperation started..'
res=`docker ps | grep couchbase`
read -ra arr <<<"$res"
echo $arr
# prepare couchbase
container=$arr

docker exec -t $container couchbase-cli cluster-init --cluster-name basket-microservice-cluster \
   --cluster-username Administrator --cluster-password password --services data,index,query,fts,eventing,analytics \
   --cluster-ramsize 2048

docker exec -t $container couchbase-cli bucket-create -c localhost -u Administrator -p password \
  --bucket basket --bucket-type couchbase --bucket-ramsize 1024

docker exec -t $container couchbase-cli user-manage -c localhost -u Administrator -p password \
  --set --rbac-username Administrator --rbac-name Administrator --rbac-password password --roles admin \
  --auth-domain local