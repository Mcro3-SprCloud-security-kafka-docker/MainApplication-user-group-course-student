package com.quanzip.filemvc;

import com.quanzip.filemvc.entity.Course;
import com.quanzip.filemvc.entity.Score;
import com.quanzip.filemvc.entity.Student;
import com.quanzip.filemvc.entity.User;
import com.quanzip.filemvc.service.dto.StudentDTO;
import com.quanzip.filemvc.service.mapper.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
// trc khi them, sua xoa,.. co the can thiep dc vao cac transaction nay cua jpa
public class FilemvcApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(FilemvcApplication.class);
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FilemvcApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        ConfigurableEnvironment environment = context.getEnvironment();

        log.info("\n\n\t\t----------------------------------------------------------------------------------\n" +
                        "\t\t\t\t\tApplication {} is running on port {}" +
                        "\n\t\t\t\t\tAccess: http://localhost:{}/sw" +
                        "\n\t\t\t\t\tAccess: http://localhost:{}/user/search" +
                        "\n\t\t\t\t\tAccess: http://localhost:{}/role/search" +
                        "\n\t\t----------------------------------------------------------------------------------\n",
                environment.getProperty("spring.application.name"),
                environment.getProperty("server.port"),
                environment.getProperty("server.port"),
                environment.getProperty("server.port"),
                environment.getProperty("server.port")
        );
    }

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello");
//
//        Student student = new Student();
//        student.setCode("code");
//        student.setId(4l);
//
//        User user = new User();
//        user.setId(4l);
//        student.setUser(user);
//
//        List<Score> list = new ArrayList<Score>(){{
//            Score score = new Score();
//            score.setScore(5d);
//            score.setId(1l);
//            score.setStudent(student);
//
//            Course course =new Course();
//            course.setId(1l);
//            course.setName("Math");
//            course.setScores(Collections.singletonList(score));
//
//            score.setCourse(course);
//            add(score);
//        }};
//
//
//        student.setScores(list);
//
//        StudentDTO studentDTO = studentMapper.toDto(student);
//        System.out.println(studentDTO);
    }
}
