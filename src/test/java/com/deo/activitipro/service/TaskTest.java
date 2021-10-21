package com.deo.activitipro.service;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

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
        // 指定人就是当前人下的任务，不指定人就是所有任务
        String assignee = "zhangsan";
//        taskQuery.taskAssignee(assignee);

        taskQuery.orderByTaskCreateTime().desc();

        List<Task> list = taskQuery.list();
        for (Task task : list) {
            String id = task.getId();
            String name = task.getName();
            String assignee1 = task.getAssignee();
            System.out.println(id + "---" + name + "----" + assignee1);
        }
    }

    /**
     * 拾取任务
     */
    @Test
    public void chaimTask() {
        // 要拾取的任务ID
        String taskId = "15002";
        // 任务候选人ID
        String userId = "zhangsan";
        // 拾取任务
        // 即使该用户不是候选人也能拾取（建议拾取时校验是否有资格）
        // 校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery().taskId(taskId)
                .taskCandidateUser(userId)  // 根据候选人查询
                .singleResult();

        if (task != null) {
            taskService.claim(taskId, userId);
            System.out.println("任务拾取成功");
        }
    }

    /**
     * 拾取任务后，进行取消操作
     */
    @Test
    public void setAssigneeToGroupTask() {
        // 查询任务使用

        // 当前待办任务
        String taskId = "15002";
        // 任务负责人
        String userId = "zhangsan";
        // 验证userId是否是taskId的负责人，如果是负责人才可以归还组任务
        Task task = taskService.createTaskQuery().taskId(taskId)
                .taskAssignee(userId).singleResult();

        if (task != null) {
            // 如果设置为 null，归还组任务，该任务没有负责人
            taskService.setAssignee(taskId, null);
        }
    }
}
