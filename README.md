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

