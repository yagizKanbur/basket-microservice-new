function clone_and_build() {
    microservice_folder=$1
    is_java_project=$2

    echo "$microservice_folder clone started.."

    git clone https://github.com/yagizKanbur/basket-microservice-new

    if $is_java_project ; then
        echo "$microservice_folder compilation started.."
        cd $microservice_folder
        ./mvnw package -DskipTests=true
        cd ..
    fi
}

clone_and_build "basket-microservice" true

echo "Docker composing started.."
docker compose up -d --build