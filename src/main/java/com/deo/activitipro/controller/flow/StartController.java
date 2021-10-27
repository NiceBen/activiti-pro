package com.deo.activitipro.controller.flow;

import com.alibaba.fastjson.JSON;
import com.deo.activitipro.model.entity.CommonVariable;
import com.deo.activitipro.utils.BeanUtil;
import com.deo.activitipro.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 启动流程实例
 *
 * @date 2021-10-26
 * @since 1.0.0
 */
@RestController
@Api(tags = "启动流程实例")
public class StartController {

    private final RuntimeService runtimeService;

    public StartController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @PostMapping("/start")
    @ApiOperation(value = "根据流程key启动流程", notes = "每一个流程有对应的一个key这个是某一个流程内固定的卸载bpmn内的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程key", dataType = "String", paramType = "query", example = ""),
            @ApiImplicitParam(name = "user", value = "启动流程的用户", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage start(@RequestParam("processKey") String processKey,
                             @RequestParam("user") String userKey) {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("userKey", userKey);

        RestMessage restMessage = new RestMessage();
        ProcessInstance instance = null;

        try {
            instance = runtimeService.startProcessInstanceByKey(processKey, userKey);
        } catch (Exception e) {
            restMessage = RestMessage.fail("启动失败", e.getMessage());
            e.printStackTrace();
        }

        if (instance != null) {
            Map<String, String> result = new HashMap<>();
            // 流程实例ID
            result.put("processID", instance.getId());

            // 流程定义ID
            result.put("processDefinitionKey", instance.getProcessDefinitionId());
            restMessage = RestMessage.success("启动成功", result);
        }
        return restMessage;
    }


    @PostMapping("/searchByKey")
    @ApiOperation(value = "根据流程定义key查询流程实例", notes = "根据流程定义key查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程key", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage searchProcessInstance(@RequestParam("processKey") String processDefinitionKey) {
        RestMessage restMessage = new RestMessage();
        List<ProcessInstance> runningList =new ArrayList<>();

        try {
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            e.printStackTrace();
        }

        int size = runningList.size();
        if (size > 0) {
            List<Map<String, String>> resultList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ProcessInstance pi = runningList.get(i);
                Map<String, String> resultMap = new HashMap<>();
                // 流程实例ID
                resultMap.put("processId", pi.getId());
                // 流程定义ID
                resultMap.put("processDefinitionKey", pi.getProcessDefinitionId());
                resultList.add(resultMap);
            }
            restMessage = RestMessage.success("查询成功", resultList);
        }
        return restMessage;
    }

    @PostMapping("/searchByID")
    @ApiOperation(value = "根据流程实例ID查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage searchByID(@RequestParam("processInstanceId") String processInstanceId) {
        RestMessage restMessage = new RestMessage();
        ProcessInstance pi = null;

        try {
            pi = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            e.printStackTrace();
        }

        if (pi != null) {
            Map<String, String> resultMap = new HashMap<>();
            // 流程实例ID
            resultMap.put("processInstanceId", pi.getId());
            // 流程定义ID
            resultMap.put("processDefinitionKey", pi.getProcessDefinitionId());
        }
        return restMessage;
    }

    @PostMapping("/deleteProcessInstanceByKey")
    @ApiOperation(value = "根据流程定义Key删除流程实例", notes = "根据流程定义Key删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程定义Key", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage deleteProcessInstanceByKey(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        RestMessage restMessage = new RestMessage();
        List<ProcessInstance> runningList = new ArrayList<>();

        try {
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
            e.printStackTrace();
        }

        int size = runningList.size();
        if (size > 0) {
            List<Map<String, String>> resultList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ProcessInstance pi = runningList.get(i);
                runtimeService.deleteProcessInstance(pi.getId(), "删除");
            }
            restMessage = RestMessage.success("删除成功", resultList);
        }
        return restMessage;
    }

    @PostMapping("/deleteProcessInstanceByID")
    @ApiOperation(value = "根据流程实例ID删除流程实例", notes = "根据流程实例ID删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceID", value = "流程实例ID", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage deleteProcessInstanceByID(@RequestParam("processInstanceID") String processInstanceID) {
        RestMessage restMessage = new RestMessage();
        try {
            runtimeService.deleteProcessInstance(processInstanceID, "删除" + processInstanceID);
            restMessage = RestMessage.success("删除成功", "");
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
        }
        return restMessage;
    }



    // ------------------------------------------------------------------------------------------------

    /**
     * 功能描述:启动流程
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/start2")
    public String start(HttpServletRequest request) {
        String processDefinitionKey = request.getParameter("processId");
        String variable = request.getParameter("variable");

        // 非空校验

        try {
            Map<String, Object> variables = new HashMap<>();
            if (!StringUtils.isEmpty(variable)) {
                CommonVariable commonVariable = JSON.parseObject(variable, CommonVariable.class);
                variables = BeanUtil.beanToMap(commonVariable);
            }

            ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
//			// Businesskey:业务标识，通常为业务表的主键，业务标识和流程实例一一对应。业务标识来源于业务系统。存储业务标识就是根据业务标识来关联查询业务系统的数据
//			ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey,
//					variables);

            System.out.println("流程实例ID:" + instance.getId());
            System.out.println("流程定义ID:" + instance.getProcessDefinitionId());
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }


    /**
     * 功能描述:删除流程
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/delete")
    public String deleteProcess(HttpServletRequest request) {
        String processId = request.getParameter("processId");

        // 非空检验

        try {
            runtimeService.deleteProcessInstance(processId, "流程已完毕");
            System.out.println("终止流程");
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:流程实例挂起
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/instance/suspend")
    public String suspendProcessInstance(HttpServletRequest request) {
        String processInstanceId = request.getParameter("processInstanceId");

        // 非空校验

        try {
            // 根据一个流程实例的id挂起该流程实例
            runtimeService.suspendProcessInstanceById(processInstanceId);
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            System.out.println("流程实例ID:" + processInstance.getId());
            System.out.println("流程定义ID:" + processInstance.getProcessDefinitionId());
            System.out.println("流程实例状态:" + processInstance.isSuspended());
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:流程实例激活
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/instance/activate")
    public String activateProcessInstance(HttpServletRequest request) {
        String processInstanceId = request.getParameter("processInstanceId");

        // 非空校验

        try {
            // 根据一个流程实例id激活该流程
            runtimeService.activateProcessInstanceById(processInstanceId);
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            System.out.println("流程实例ID:" + processInstance.getId());
            System.out.println("流程定义ID:" + processInstance.getProcessDefinitionId());
            System.out.println("流程实例状态:" + processInstance.isSuspended());
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }




}
