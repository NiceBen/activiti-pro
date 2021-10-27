package com.deo.activitipro.controller.flow;

import io.swagger.annotations.Api;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 历史
 *
 * @date 2021-10-27
 * @since 1.0.0
 */
@RestController
@Api(tags = "历史")
public class HistoricController {
    private final HistoryService historyService;

    public HistoricController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * 功能描述:查询历史流程实例
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/process")
    public String historicProcess(HttpServletRequest request) {
        try {
            List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                    .orderByProcessInstanceStartTime().asc()    // 排序
                    .list();
            if (list != null && list.size() > 0) {
                for (HistoricProcessInstance hpi : list) {
                    System.out.println("流程定义ID：" + hpi.getProcessDefinitionId());
                    System.out.println("流程实例ID：" + hpi.getId());
                    System.out.println("开始时间：" + hpi.getStartTime());
                    System.out.println("结束时间：" + hpi.getEndTime());
                    System.out.println("流程持续时间：" + hpi.getDurationInMillis());
                    System.out.println("*****************************************************************************");
                }
            }
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:查询流程历史步骤
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/activity")
    public String historicActivity(HttpServletRequest request) {
        String processInstanceId = request.getParameter("processInstanceId");

        // 非空校验

        try {
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId).list();
            if (list != null && list.size() > 0) {
                for (HistoricActivityInstance hai : list) {
                    System.out.println(hai.getId());
                    System.out.println("步骤ID：" + hai.getActivityId());
                    System.out.println("步骤名称：" + hai.getActivityName());
                    System.out.println("执行人：" + hai.getAssignee());
                    System.out.println("*****************************************************************************");
                }
            }
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }


    /**
     * 功能描述:查询流程执行任务记录
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/task")
    public String historicTask(HttpServletRequest request) {
        String processInstanceId = request.getParameter("processInstanceId");

        // 非空校验

        try {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId).list();
            if (list != null && list.size() > 0) {
                for (HistoricTaskInstance hti : list) {
                    System.out.println("任务ID:" + hti.getId());
                    System.out.println("任务名称:" + hti.getName());
                    System.out.println("流程定义ID:" + hti.getProcessDefinitionId());
                    System.out.println("办理人:" + hti.getAssignee());
                    System.out.println("*****************************************************************************");
                }
            }
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:查询流程历史变量
     *
     * @param request
     * @param response
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/variable")
    public String historicVariable(HttpServletRequest request) {
        String processInstanceId = request.getParameter("processInstanceId");

        // 非空校验

        try {
            List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId).list();
            if (list != null && list.size() > 0) {
                System.out.println("流程实例ID:" + processInstanceId);
                for (HistoricVariableInstance hvi : list) {
                    System.out.println("任务ID:" + hvi.getTaskId());
                    System.out.println("变量:[" + hvi.getVariableName() + "=" + hvi.getValue() + "]");
                    System.out.println("*****************************************************************************");
                }
            }

        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }
}
