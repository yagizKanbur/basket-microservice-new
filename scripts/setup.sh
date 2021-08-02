container=$1

if [ "$container" -eq '' ]
then
  echo 'Please input a container id'
  exit
else
  echo 'Container captured=' $container
fi

docker exec -t $container couchbase-cli cluster-init --cluster-name basket-microservice-cluster \
   --cluster-username Administrator --cluster-password opensesame --services data,index,query,fts,eventing,analytics \
   --cluster-ramsize 2048

docker exec -t $container couchbase-cli bucket-create -c localhost -u Administrator -p opensesame \
  --bucket basket --bucket-type couchbase --bucket-ramsize 1024

docker exec -t $container couchbase-cli user-manage -c localhost -u Administrator -p opensesame \
  --set --rbac-username yagiz --rbac-name yagiz --rbac-password opensesame --roles admin \
  --auth-domain local