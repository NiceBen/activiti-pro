package com.deo.activitipro.service;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.jws.Oneway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程变量
 *
 * @author SL Zhou
 * @date 2021-10-22
 * @since 1.0.0
 */
//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class VariablesTest {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    @Before
    public void init() {
        System.out.println("初始化中...");
    }

    /**
     * 情动流程设置
     */
    @Test
    public void test1() {
        // 开启流程
        String processDefinitionId = getProcessDefinitionId();
        // 流程变量
        String variables = "流程变量";

        Map map = new HashMap<>();
        map.put("variables", variables);
        map.put("employeeName", "zhangsan");
        map.put("numberOfDay", 12);

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, map);
        System.out.println(processInstance.getId());
    }

    @Test
    public void test2() {
        // 开启流程
        String processDefinitionId = getProcessDefinitionId();
        String variables = "hello world";
        Map map = new HashMap();
        map.put("variables", variables);

        //在任务中设置流程变量
        String taskId = getTaskId();

        taskService.setVariables(taskId, map);

        System.out.println("-------------");

        Object variables1 = taskService.getVariable(taskId, "variables");
        System.out.println(variables1.toString());
    }

    /**
     * 单线运行实例设置
     */
    @Test
    public void test3() {
        // 开启流程
        String processDefinitionId = getProcessDefinitionId();
        String variables = "this is a variable";
        Map map = new HashMap();
        map.put("variables", variables);
        // 在任务中设置流程变量
        runtimeService.setVariables("执行ID", map);
    }




    /**
     * 获取 任务ID
     * @return
     */
    private String getTaskId() {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.orderByTaskCreateTime().desc();
        String taskId = "";
        List<Task> list = taskQuery.list();
        if (list.size() > 0) {
            taskId = list.get(0).getId();
        }
        return taskId;
    }


    /**
     * 获取 流程定义ID
     * @return
     */
    private String getProcessDefinitionId() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        String processDefinitionId = "";
        if (list.size() > 0) {
            processDefinitionId = list.get(0).getId();
        }
        return processDefinitionId;
    }
}
