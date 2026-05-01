#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/job .

docker run -ti --name job_test -p8014:8014 -idt java110/job:latest

docker logs -f jobtest
