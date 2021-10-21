package com.deo.activitipro.service;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 流程引擎-Task（任务）
 *
 * @author SL Zhou
 * @date 2021-10-21
 * @since 1.0.0
 */
//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class TaskTest {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Before
    public void init() {
        System.out.println("初始化....");
    }

    /**
     * 通过代码设置执行人
     */
    @Test
    public void setAssi() {

        // 流程流转过程中，动态的设置任务接收人
        /*
        ①可以通过组织机构，通过当其企业的角色查询出对应的人员，将人员填充到对应的任务ID的执行人
        ②是否可以填充觉，这种情况呢？
         */
        taskService.setAssignee("taskId", "userId");
    }

    @Test
    public void test4() {
        // 查询指派人下的任务
        TaskQuery taskQuery = taskService.createTaskQuery();




    }



}
