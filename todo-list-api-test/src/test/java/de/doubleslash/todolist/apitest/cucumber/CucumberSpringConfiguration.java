package de.doubleslash.todolist.apitest.cucumber;

import de.doubleslash.todolist.apitest.cucumber.service.TaskApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CucumberSpringConfiguration {

   @Autowired
   protected TaskApiService taskApiService;
}