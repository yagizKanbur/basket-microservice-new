container=$1

if [ "$container" -eq '' ]
then
  echo 'Please input a container id'
  exit
else
  echo 'Container captured=' $container
fi

docker exec -t $container couchbase-cli cluster-init --cluster-name user-follows-product-cluster \
   --cluster-username Administrator --cluster-password password --services data,index,query,fts,eventing,analytics \
   --cluster-ramsize 2048

docker exec -t $container couchbase-cli bucket-create -c localhost -u Administrator -p password \
  --bucket followed_products --bucket-type couchbase --bucket-ramsize 1024

docker exec -t $container couchbase-cli user-manage -c localhost -u Administrator -p password \
  --set --rbac-username myapp --rbac-name myapp --rbac-password 123321 --roles admin \
  --auth-domain local