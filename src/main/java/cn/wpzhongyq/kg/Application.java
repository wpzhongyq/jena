package cn.wpzhongyq.kg;

import cn.wpzhongyq.kg.service.jena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        jena je = new jena();
        je.define();
    }
}