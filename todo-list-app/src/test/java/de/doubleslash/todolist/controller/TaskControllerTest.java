package de.doubleslash.todolist.controller;

import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.model.TaskStatus;
import de.doubleslash.todolist.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

   private MockMvc mockMvc;

   @Mock
   private TaskService taskService;

   @InjectMocks
   private TaskController taskController;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
   }

   @Test
   void testGetAllTasks() throws Exception {
      Task task = new Task(1L, "Task 1", TaskStatus.OPEN);
      when(taskService.getAllTasks()).thenReturn(Collections.singletonList(task));

      mockMvc.perform(get("/tasks"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$[0].title").value("Task 1"));

      verify(taskService, times(1)).getAllTasks();
   }

   @Test
   void testCreateTaskWithValidData() throws Exception {
      Task task = new Task("New Task", TaskStatus.OPEN);
      when(taskService.createTask(anyString(), any(TaskStatus.class))).thenReturn(task);

      mockMvc.perform(post("/tasks")
                   .contentType("application/json")
                   .content("{\"title\":\"New Task\", \"status\":\"OPEN\"}"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.title").value("New Task"));

      verify(taskService, times(1)).createTask(anyString(), any(TaskStatus.class));
   }

   @Test
   void testCreateTaskWithInvalidData() throws Exception {
      mockMvc.perform(post("/tasks")
                   .contentType("application/json")
                   .content("{\"title\":\"\", \"status\":\"OPEN\"}"))
             .andExpect(status().isBadRequest());

      verify(taskService, times(0)).createTask(anyString(), any(TaskStatus.class));
   }

   @Test
   void testMarkAllTasksAsCompleted() throws Exception {
      mockMvc.perform(patch("/tasks"))
             .andExpect(status().isOk());

      verify(taskService, times(1)).markTasksAsCompleted();
   }

   @Test
   void testMarkTaskAsCompletedWhenTaskExists() throws Exception {
      Task task = new Task(1L, "Task 1", TaskStatus.OPEN);
      when(taskService.markTaskAsCompleted(1L)).thenReturn(Optional.of(task));

      mockMvc.perform(patch("/tasks/1"))
             .andExpect(status().isOk());

      verify(taskService, times(1)).markTaskAsCompleted(1L);
   }

   @Test
   void testMarkTaskAsCompletedWhenTaskDoesNotExist() throws Exception {
      when(taskService.markTaskAsCompleted(1L)).thenReturn(Optional.empty());

      mockMvc.perform(patch("/tasks/1"))
             .andExpect(status().isNotFound());

      verify(taskService, times(1)).markTaskAsCompleted(1L);
   }
}