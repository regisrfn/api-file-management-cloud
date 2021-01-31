# File- API
Spring boot rest api

## PREREQUISITES
- Java
- Docker | https://docs.docker.com/engine/install/ubuntu/ 

## DOCKER
- RUNNING POSTGRES
- create:
docker run --name file_management_db -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_DB=file_management -d postgres:alpine
- stop:
docker stop postgres
- start:
docker start postgres

## DATABASE DESCRIPTION

|       Files           |
|--------------------   |
|fileId: UUID (PK)      |
|fileName: String       |
|fileSize: Long         |
|fileContentType:String |
|fileUrl:String         |
|fileUserId:UUID (FK)   | 
