package com.deo.activitipro.controller.flow;

import com.deo.activitipro.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 部署、删除流程
 *
 * @author SL Zhou
 * @date 2021-10-22
 * @since 1.0.0
 */
@RestController
@Api(tags = "部署、删除流程")
public class DeployController {

    private final RepositoryService repositoryService;

    public DeployController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping(path = "/deploy")
    @ApiOperation(value = "根据bpmnName部署流程", notes = "根据bpmnName部署流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bpmnName", value = "设计的流程图名称", dataType = "String", paramType = "query", example = "myProcess")
    })
    public RestMessage deploy(@RequestParam("bpmnName") String bpmnName) {
        RestMessage restMessage = new RestMessage();
        // 创建一个部署对象
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().name("请假流程");
        Deployment deployment = null;
        try {
            deploymentBuilder
                    .addClasspathResource("processes/" + bpmnName + ".bpmn20.xml")
                    .addClasspathResource("processes/" + bpmnName + ".png")
                    .deploy();
        } catch (Exception e) {
            restMessage = RestMessage.fail("部署失败", e.getMessage());
            e.printStackTrace();
        }
        if (deployment != null) {
            Map<String, String> result = new HashMap<>(2);
            result.put("deployId", deployment.getId());
            result.put("deployName", deployment.getName());
            restMessage = RestMessage.success("部署成功", result);

        }
        return restMessage;
    }



}
