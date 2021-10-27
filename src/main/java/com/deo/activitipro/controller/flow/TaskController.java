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
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程任务相关
 *
 * @date 2021-10-26
 * @since 1.0.0
 */
@RestController
@Api(tags = "任务相关接口")
public class TaskController {
    private final TaskService taskService;

    private final RuntimeService runtimeService;

    public TaskController(TaskService taskService, RuntimeService runtimeService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
    }

    @PostMapping("/findTaskByAssignee")
    @ApiOperation(value = "根据流程assignee查询当前人的个人任务", notes = "根据流程assignee查询当前人的个人任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assignee", value = "代理人（当前用户）", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage findTaskByAssignee(@RequestParam("assignee") String assignee) {
        RestMessage restMessage = new RestMessage();
        // 创建任务查询对象
        List<Task> taskList;
        try {
            taskList = taskService.createTaskQuery()
                    // 指定个人任务查询
                    .taskAssignee(assignee)
                    .list();
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            e.printStackTrace();
            return restMessage;
        }

        List<Map<String, String>> resultList = new ArrayList<>();
        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                Map<String, String> resultMap = new HashMap<>();
                /* 任务ID */
                resultMap.put("taskId", task.getId());
                /* 任务名称 */
                resultMap.put("taskName", task.getName());
                /* 任务的创建时间 */
                resultMap.put("taskCreateTime", task.getCreateTime().toString());
                /* 任务的办理人 */
                resultMap.put("taskAssignee", task.getAssignee());
                /* 流程实例ID */
                resultMap.put("processInstanceId", task.getProcessInstanceId());
                /* 执行对象ID */
                resultMap.put("executionId", task.getExecutionId());
                /* 流程定义ID */
                resultMap.put("processDefinitionId", task.getProcessDefinitionId());
                resultList.add(resultMap);
            }
        }
        restMessage = RestMessage.success("查询成功", resultList);
        return restMessage;
    }

    @PostMapping("/completeTask")
    @ApiOperation(value = "完成任务", notes = "完成任务，任务进入下一个节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query", example = ""),
            @ApiImplicitParam(name = "days", value = "请假天数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage completeTask(@RequestParam("taskId") String taskId,
                                    @RequestParam("days") String days) {
        RestMessage restMessage = new RestMessage();
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("days", days);
            taskService.complete(taskId, variables);
        } catch (Exception e) {
            restMessage = RestMessage.fail("提交失败", e.getMessage());
            e.printStackTrace();
            return restMessage;
        }
        restMessage = RestMessage.success("提交成功", taskId);
        return restMessage;
    }


    // ------------------------------------------------------------------------------------------------

    /**
     * 功能描述:查询任务
     *
     * @param request
     * @param response
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/query")
    public String taskQuery(HttpServletRequest request) {
        try {
            List<Task> list = taskService.createTaskQuery().list();
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("*****************************************************************************");
            }
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:查询当前任务
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/get")
    public String getTask(HttpServletRequest request) {
        String processInstanceId = request.getParameter("processInstanceId");

        // 非空校验

        try {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            if (task == null) {
                System.out.println("流程已结束");
                System.out.println("流程实例ID:" + processInstanceId);
                System.out.println("*****************************************************************************");
                return "success";
            }
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务的创建时间:" + task.getCreateTime());
            System.out.println("任务的办理人:" + task.getAssignee());
            System.out.println("流程实例ID：" + task.getProcessInstanceId());
            System.out.println("执行对象ID:" + task.getExecutionId());
            System.out.println("流程定义ID:" + task.getProcessDefinitionId());
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:查询当前全部任务
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/list")
    public String listTask(HttpServletRequest request) {
        String processInstanceId = request.getParameter("processInstanceId");

        if (StringUtils.isEmpty(processInstanceId)) {
            return "param error";
        }

        try {
            List<Task> taskList = taskService.createTaskQuery()// 创建查询对象
                    .processInstanceId(processInstanceId)// 通过流程实例id来查询当前任务
                    .list();// 获取单个查询结果
            if (CollectionUtils.isEmpty(taskList)) {
                System.out.println("流程已结束");
                System.out.println("流程实例ID:" + processInstanceId);
                System.out.println("*****************************************************************************");
                return "success";
            }

            taskList.forEach(task -> {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("*****************************************************************************");
            });
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:查询进行中任务
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/get/run")
    public String getRun(HttpServletRequest request) {
        String processInstanceId = request.getParameter("processInstanceId");

        // 非空校验

        try {
            ProcessInstance process = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (process == null) {
                System.out.println("流程已结束");
                System.out.println("流程实例ID:" + processInstanceId);
                System.out.println("*****************************************************************************");
                return "success";
            } else {
                List<Task> list = taskService.createTaskQuery().list();
                // TODO -- 这里应该是查询的任务
            }
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:完成任务
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/complete")
    public String complete(HttpServletRequest request) {
        String taskId = request.getParameter("taskId");
        String variable = request.getParameter("variable");

        // 参数校验

        try {
            Map<String, Object> variables = new HashMap<>();
            if (StringUtils.hasText(variable)) {
                CommonVariable commonVariable = JSON.parseObject(variable, CommonVariable.class);
                variables = BeanUtil.beanToMap(variables);
            }
            // 设置流程参数（单）
//            taskService.complete(taskId, key, value);
            // 设置流程参数（多）
            taskService.complete(taskId, variables);

            // 如果是委托任务，先处理委托任务
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (DelegationState.PENDING.equals(task.getDelegationState())) {
                return "请先处理委托任务";
            }
            taskService.complete(taskId);
            System.out.println("任务完成");
            System.out.println("任务ID:" + taskId);
            System.out.println("任务处理结果:" + variables);
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:任务委托
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/assignee")
    public String assignee(HttpServletRequest request) {
        String taskId = request.getParameter("taskId");
        String assignee = request.getParameter("assignee");

        // 非空校验

        try {
            taskService.delegateTask(taskId, assignee);
            System.out.println("任务已委托给：" + assignee);
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:解决委托任务
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/resolve")
    public String resolve(HttpServletRequest request) {
        String taskId = request.getParameter("taskId");
        String variable = request.getParameter("variable");

        // 非空校验

        try {
            Map<String, Object> variables = new HashMap<>();
            if (!StringUtils.isEmpty(variable)) {
                CommonVariable variablesEntity = JSON.parseObject(variable, CommonVariable.class);
                variables = BeanUtil.beanToMap(variablesEntity);
            }
//			// 设置流程参数（单）
//			taskService.setVariable(taskId, key, value);
            // 设置流程参数（多）
            taskService.setVariables(taskId, variables);

            // 根据taskId提取任务
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task.getOwner() != null && !task.getOwner().equals("null")) {
                DelegationState delegationState = task.getDelegationState();
                if (delegationState.equals(DelegationState.RESOLVED)) {
                    System.out.println("此委托任务已是完结状态");
                } else if (delegationState.equals(DelegationState.PENDING)) {
                    // 如果是委托任务需要做处理
                    taskService.resolveTask(taskId, variables);
                } else {
                    System.out.println("此任务不是委托任务");
                }
            }
            System.out.println("委托任务处理完毕");
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }
}
