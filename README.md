# DevConnect API

> API created for managing programming projects

## Table of Contents

- [About](#about)
- [Technologies](#technologies)
- [Features](#features)
- [Instructions](#instructions)

## About

## Technologies

- Spring Boot
- PostgreSQL
- Docker
- Amazon S3

## Features

- Creating user account
- Adding profile picture stored in S3
- Authentication with JWT
- Creating, updating, deleting projects
- Adding contributors to projects
- Assigning tasks for contributors
- Admin users can block and delete regular user accounts

## Instructions

### Step 1: Clone repository

```bash
git clone https://github.com/czajkowski-szymon/dev-connect.git
```

### Step 2: Package application with Maven

```bash
mvn clean package
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