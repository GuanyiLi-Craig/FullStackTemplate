# FullStackTemplate
Vue and Spring framework 

- [ ] `> mkdir backend`

- [ ] `> mkdir frontend`

## Setup SpringBoot

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