package com.deo.activitipro.service;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程引擎-definition（流程定义）
 *
 * @date 2021-10-21
 * @since 1.0.0
 */
//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class ProcessDefinitionTest {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Before
    public void init() {
        System.out.println("初始化！");
    }


    @Test
    public void test1() {
        DeploymentBuilder builder = processEngine.getRepositoryService().createDeployment();
        Deployment deploy = builder.addClasspathResource("processes/VacationRequest.bpmn20.xml")
                .deploy();
        System.out.println(deploy.getId());
    }

    @Test
    public void test2() {
        ProcessDefinitionQuery query = processEngine.getRepositoryService()
                .createProcessDefinitionQuery();

//        query.latestVersion();
        List<ProcessDefinition> list = query.list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println(processDefinition.getId() + "----------" + processDefinition.getName());
        }
    }

    @Test
    public void test3() {
        String pdId = getProcessDefinitionId();

        // 开启流程
//        String processDefinitionId = ""
        ProcessInstance pi = runtimeService.startProcessInstanceById(pdId);
        System.out.println("ProcessInstance Id========" + pi.getId());
    }

    @Test
    public void test4() {
        String pdId = getProcessDefinitionId();

        // 根据流程定义的 key 启动一个流程实例
        String businessKey = "bus_id";
        runtimeService.startProcessInstanceById(pdId, businessKey);
    }

    /**
     * 针对流程定义，进行挂起、激活
     */
    @Test
    public void suspendOrActivateProcessDefinition() {
        // 挂起激活流程定义——全部流程实例
        String processDefinitionId = "";
        processDefinitionId = getProcessDefinitionId();

        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();

        // 是否暂停
        boolean suspended = processDefinition.isSuspended();
        if (suspended) {
            // 如果暂停则激活，这里将流程定义下的所有流程实例全部激活
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义：" + processDefinitionId + "激活");
        } else {
            // 如果激活则挂起，这里将流程定义下的所有流程实例全部挂起
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义：" + processDefinitionId + "挂起");
        }
    }

    /**
     * 针对单个流程实例，进行挂起、激活
     */
    @Test
    public void suspendOrActiveProcessInstance() {
        // 流程实例ID
        String processInstanceId = "";
        // 根据流程实例id，查询流程实例
        processInstanceId = getProcessInstanceId();
        System.out.println("processInstanceId===" + processInstanceId);

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            // 如果暂停，则激活
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例：" + processInstanceId + "激活");
        } else {
            // 如果激活，挂起
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例：" + processInstanceId + "挂起");
        }
    }

    /**
     * 根据流程定义ID，获取流程实例
     * @return
     */
    private String getProcessInstanceId() {
        String processInstanceId = "";
        String businessKey = "bus_id";
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        List<ProcessInstance> list = processInstanceQuery.processInstanceBusinessKey(businessKey).list();
        if (list.size() > 0) {
            processInstanceId = list.get(0).getId();
        }
        return processInstanceId;
    }


    /**
     * 获取流程定义ID
     * @return
     */
    private String getProcessDefinitionId() {
        ProcessDefinitionQuery query = processEngine.getRepositoryService()
                .createProcessDefinitionQuery();
        List<ProcessDefinition> list = query.list();
        String pdId = "";
        if (list.size() > 0) {
            pdId = list.get(0).getId();
        }
        System.out.println("pdId========" + pdId);
        return pdId;
    }
}
