# DevConnect API

> API created for managing programming projects

## Table of Contents

- [About](#about)
- [Technologies](#technologies)
- [Features](#features)
- [Instructions](#instructions)

## About

## Technologies

- Spring Boot <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="20" height="20"/>
- PostgreSQL <img src="https://www.vectorlogo.zone/logos/postgresql/postgresql-icon.svg" alt="postgresql" width="20" height="20"/>
- Docker <img src="https://www.vectorlogo.zone/logos/docker/docker-tile.svg" alt="docker" width="20" height="20"/>
- Amazon S3 <img src="https://www.vectorlogo.zone/logos/amazon_aws/amazon_aws-icon.svg" alt="aws" width="20" height="20"/>

## Features

- Creating user account
- Adding profile picture stored in S3
- Authentication with JWT
- Creating, updating, deleting projects
- Adding contributors to projects
- Assigning tasks to contributors
- Admin users can block and delete regular user accounts

## Instructions

### Step 1: Clone repository

```bash
git clone https://github.com/czajkowski-szymon/dev-connect.git
```

### Step 2: Package application with Maven

```bash
mvnw clean package
```

### Step 3: Run docker containers

```bash
docker-compose up --build
```

### Step 4: Test API using Swagger

Go to:

```bash
http://localhost:8080/swagger-ui/index.html
```
<br>
In case of any problems feel free to contact me.
