package com.deo.activitipro.controller.flow;

import com.deo.activitipro.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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
}
