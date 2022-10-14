#! /usr/bin/bash

echo "stopping and removing user-service container"
docker rm -f user-service

echo "starting user-service container"
docker run -d -p 80:9080 --name user-service --restart always --env spring_profiles_active={PROFILE} registry.devops-labs.it/students/devops32/user-service:version-{VERSION}

docker ps
