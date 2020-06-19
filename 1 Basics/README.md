# Build Full Stack with Vue and Spring

Build an application with Vue and Spring framework from scretch. 

Prepare

- [ ] `> mkdir backend`

- [ ] `> mkdir frontend`

## 1. Setup SpringBoot

### initialize SpringBoot

- [ ] Initialize backend

Use https://start.spring.io/ to initialize SpringBoot. 

Select the following 

- [ ] Web

- [ ] Lombok

Unzip the downloaded package and move to backend folder. 

### Hello World

- [ ] Start server and return hellow world on http://localhost:9000/helloworld

1. change .../resources/application.properties to .../resources/application.yml
2. add hello world controller to the .../controller/HelloWorldController.java

```java
@RestController
public class HelloWorldController {
    
    @RequestMapping("/helloworld")
    public String HelloWorld() {
        return "Hello World";
    }
}
```

3. run application by `> ./gradlew bootRun`
4. Open browser and go to http://localhost:9000/helloworld

## 2. Setup Vue

1. initialize vue project `> vue create frontend`
2. test `> npm run serve`

## 3. Restful hellow world

### Backend

1. change annotation in HelloWorldController `@RequestMapping` to `@GetMapping`

```java
@RestController
public class HelloWorldController {
    
    @GetMapping("/helloworld")
    public String HelloWorld() {
        return "Hello World";
    }
}
```

### Frontend

In order to build a simplest hello world page, we remove the useless auto-generated code. 

HelloWorld.vue
```html
<template>
  <div class="hello">
    <h1>{{ body.msg }}</h1>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HelloWorld',
  data() {
    return {
      body: {
        msg: ""
      }
    }
  },
  async mounted() {
    console.log('Get data from backend');
    this.body.msg = (await axios.get(`http://localhost:8080/api/helloworld`)).data;
  }
}
</script>
```

App.vue
```html
<template>
  <div id="app">
    <HelloWorld />
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    HelloWorld
  }
}
</script>
```

When you start the backend and frontend servers and open http://localhost:8080, you will find that it didn't show anything and browser shown there was a 404 error. Because our backend port is 9000 for dev! If we change the 8080 to 9000, then browser will report a CORS error. Here we need to add a proxy. 

Create a file in the root directory 

vue.config.js
```javascript
module.exports = {
configureWebpack: {
    devServer: {
        proxy: {
          '/api': {
            target: 'http://localhost:9000/',
            changeOrigin: true,
            ws: true,
            pathRewrite: {
              '^/api': ''
            }
          }
        }
      }
  }
};
```

Here we have a very simple template for SpringBoot backend and Vue frontend. 

# Multi Projects Refect

Microservice system includes multiple SpringBoot projects and manage them by SpringCloud framework. But firstly, we want to refect the above simple project into a multi projects managed by a root probject. These sub projects (modules) will be deployed to different hosts in the real world or managed by Kubernetes. We will do it bit by bit. 

Here, in order to simplify the problem, we will implement two projects which return "hello world 1" and "hello world 2". Simple, ha! 

## Setup Gradle

Before doing anything, let's remove the backend code first. 

Remove /src, /build, /bin and ./gradle

Actually, I want to build a website which can manage users and scrap web pages for me. So I will name my sub projects by user-manager and web-scraper. 

Add user-manager and web-scraper to settings.gradle

```js
rootProject.name = 'full-stack-template'
include 'user-manager'
include 'web-scraper'
```

Now the root build.gradle file will be like 

```js
plugins {
	id 'io.spring.dependency-management' version '1.0.9.RELEASE'
	id 'java'
}

subprojects {
	group = 'com.bitforcestudio'
	version = '0.0.1-SNAPSHOT'
	sourceCompatibility = '1.8'

	repositories {
		mavenCentral()
	}

	dependencyManagement {
	}
}
```

Root gradle is used to define the dependencies for all the projects and versioning. Here we setup the java version apply some shared configurations to all the sub projects, such as current version, group id and java version. 

Use Spring Initializer to init two projects with web, actuator and lombok dependency. Remove the following

`gradlew* settings.gradle /gradle` 

and replace the build.gradle to 

```js
plugins {
	id 'org.springframework.boot' version '2.3.1.RELEASE'
	id 'io.spring.dependency-management'
	id 'java'
}

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation('org.springframework.boot:spring-boot-starter-test') {
		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	}
}

test {
	useJUnitPlatform()
}

```

A backend application has the following structure

`frontend <=> controller -> service -> dao -> database`

We will implement this bit by bit, but now lets focus on the logic before `dao`. 

Firstly, let's add service interface and impl. The structure will be like 

```
service
   |___ XxxxService.java   `interface`
   |___ impl
          |___ XxxxServiceImpl.java    `class`
```

The two main user functions are login and logout. Let's just implement these two and return some arbitory info to the website. 

UserManagerService.java
```java
package com.bitforcestudio.usermanager.service;

public interface UserManagerService {
    public String login(String username, String password);

    public String logout(String username);
}
```

UserManagerServiceImpl.java
```java
package com.bitforcestudio.usermanager.service.impl;

import com.bitforcestudio.usermanager.service.UserManagerService;
import org.springframework.stereotype.Service;

@Service
public class UserManagerServiceImpl implements UserManagerService {

    @Override
    public String login(String username, String password) {
        return username + " login " + password;
    }

    @Override
    public String logout(String username) {
        return username + " logout";
    }
    
}
```

And we need to add something to application.yml in resource directory. 

application.yml
```yaml
server:
  port: 8002
```

Then we need to add the controller, which will directly interact with the requests. Let's create Controller class like 

```
controller
    |___  XxxxController.java
```

UserManagerController.java
```java
package com.bitforcestudio.usermanager.controller;

import com.bitforcestudio.usermanager.service.impl.UserManagerServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class UserManagerController {

    @Autowired
    private UserManagerService userManagerService;

    @GetMapping(value="/user/login/{username}/{password}")
    public String login(@PathVariable("username") String username, 
                        @PathVariable("password") String password) {
        return userManagerService.login(username, password);
    }
    
    @GetMapping(value="/user/logout/{username}")
    public String logout(@PathVariable("username") String username) {
        return userManagerService.logout(username);
    }
}
```

Use ` ./gradlew :<project_name>:bootRun` to test each project. 

# Add Database connection

We want to create user table and job table. Let's focus on the user table firstly. 

The general process of adding database layer is 

* add dependency
* add DAO which expose the CRUD interfaces
* add corresponding Mapper (Mybatis)

## User Table

A use table should contain the following columns

* username
* password

Corresponding SQL code:

```sql
CREATE TABLE user(
  user_id INT UNSIGNED AUTO_INCREMENT NOT NULL,
  user_name VARCHAR(20) NOT NULL,
  password CHAR(32) NOT NULL,
  user_stats TINYINT NOT NULL,
  modified_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY pk_customerid(user_id)
) ENGINE = innodb;
```

In local dev we use docker to run MySQL server.

```bash
docker run --restart always --name mysql-spring-cloud --net spring-cloud-dev-network -v /to-local-disk-data/mysql:/var/lib/mysql -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=123456 mysql
```

Explain:
* `--restart always` to ensure this MySQL container will start when docker starts
* `--net spring-cloud-dev-network` ref [<- docker net ->](https://docs.docker.com/network/bridge/)

> User-defined bridges provide better isolation.
> All containers without a --network specified, are attached to the default bridge network. This can be a risk, as unrelated stacks/services/containers are then able to communicate.
> Using a user-defined network provides a scoped network in which only containers attached to that network are able to communicate.

* `-v /to-local-disk-data/mysql:/var/lib/mysql` to persistent the database
* `-p 3306:3306` expose port 3306 to the outside
* `-d ` run as daemon service

To test the MySQL container, try connect to MySQL from terminal. 

```bash
>mysql -h localhost -P 3306 -u root -p --protocol=tcp
```

Then we add an entity of user to the project 

entities/User.java
```java
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Integer userId;
    private String userName;
    private String password;
    private Integer userStats;
    private Date modifiedTime;

    public User(Integer userId, String userName, String password) {
        this(userId, userName, password, 0, new Date(System.currentTimeMillis()));
    }

    public User(String userName, String password) {
        this(-1, userName, password, 0, new Date(System.currentTimeMillis()));
    }
}
```

## Dependencies & Configurations

* Add MySQL and JDBC dependency to `build.gradle`

```groovy
	implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
  implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.3'
	runtimeOnly 'mysql:mysql-connector-java'
```

* Add MySQL and Mybatis configuration to application.yml

```yaml
server:
  port: 8002

spring:
  application:
    name: user-manager-service
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost/spring
    username: root
    password: 123456

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.bitforcestudio.usermanager.entities
```

Here we need to add dao and corresponding mapper. 

UserDao.java 
```java
@Mapper
public interface UserDao {
    public boolean createUserTable();

    public int createNewUser(User user);

    public boolean removeUser(Integer userId);

    public int updateUser(User user);

    public User getUserById(@Param("userId") Integer userId);

    public User getUserByUserName(@Param("userName") String userName);
}
```

XML mapper
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.bitforcestudio.usermanager.dao.UserDao">


    <sql id="createUserTable">
        create table if not exists user(user_id INT UNSIGNED AUTO_INCREMENT NOT NULL,
                        user_name VARCHAR(20) NOT NULL,
                        password CHAR(32) NOT NULL,
                        user_stats TINYINT NOT NULL,
                        modified_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY pk_customerid(user_id)
        ) engine=innodb;
    </sql>


    <insert id="createNewUser" parameterType="User" useGeneratedKeys="true" keyProperty="userId">
          insert into user(user_name, password, user_stats) values(#{userName}, #{password}, #{userStats});
    </insert>


    <delete id="removeUser" parameterType="Integer">
          delete from user where id=#{id};
    </delete>


    <update id="updateUser" parameterType="User" useGeneratedKeys="true" keyProperty="userId">
          update user set user_name=#{userName} password=#{password} user_stats=#{userStats} where userId=#{userId};
    </update>


    <select id="getUserById" parameterType="Integer" resultMap="UserBaseResultMap">
          select * from user where user_id=#{userId};
    </select>

    <resultMap id="UserBaseResultMap" type="com.bitforcestudio.usermanager.entities.User">
        <id column="user_id" property="userId" jdbcType="INTEGER"/>
        <id column="user_name" property="userName" jdbcType="VARCHAR"/>
        <id column="password" property="password" jdbcType="CHAR"/>
        <id column="user_stats" property="userStats" jdbcType="TINYINT"/>
        <id column="modified_time" property="modifiedTime" jdbcType="TIMESTAMP"/>
    </resultMap>


    <select id="getUserByUserName" parameterType="String" resultMap="UserBaseResultMap">
          select * from user where user_name=#{userName};
    </select>

</mapper>
```

Then add some simple logic in service and controller to test the CRUD

ServiceImpl
```java
    @Override
    public String signup(String username, String password) {
        int id = userDao.createNewUser(new User(username, password));

        return Integer.toString(id);
    }
```

Controller
```java
    @GetMapping(value="/user/signup/{username}/{password}")
    public String signup(@PathVariable("username") String username, 
                        @PathVariable("password") String password) {
        return userManagerService.signup(username, password);
    }
```

Finally, let's test the project by 

`./gradlew :user-manager:bootRun`

You can insert a user by `http://localhost:8002/user/signup/<username>/<password>`