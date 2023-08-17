# File Upload Demo with Spring Boot

This repository contains a simple demonstration of how to handle file uploads using Spring Boot. The code explores various ways to store files, focusing primarily on database storage but also mentioning alternatives.

## Features
- File upload to a database using BLOBs
- Example code for uploading to local file systems and cloud storage services

## Prerequisites
- Java 17 or later
- Spring Boot 3.x
- MySQL (for database storage example)

## Getting Started
1. Clone the repository: `git clone https://github.com/rokon12/fileuplod-demo.git`
2. Navigate to the project directory: `cd fileuplod-demo`
3. Configure your database credentials in `application.properties`
4. Build and run the application: `./mvnw spring-boot:run`

## Usage
Send a POST request to `/files/upload` with a file attached to test the file upload.

```
curl -X POST -H 'Content-Type: multipart/form-data' -F 'file=@/home/uses/uploads/_cd03deb1-489d-4867-9b5b-2ffde99a3e20.jpeg http://localhost:8080/files/upload
```

## Contributing
Feel free to contribute to this project by opening issues, sending pull requests, or sharing advice on better practices.

## License
Just smile. 

## Acknowledgements
This demo is part of a tutorial article that explains the concept in detail. You can read the full article [here](https://bazlur.ca/2023/08/17/exploring-file-storage-solutions-in-spring-boot-database-local-systems-cloud-services-and-beyond/)
