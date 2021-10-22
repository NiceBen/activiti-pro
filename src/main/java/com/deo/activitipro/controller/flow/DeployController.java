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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

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

    @PostMapping(path = "/deployZIP")
    @ApiOperation(value = "根据ZIP压缩包部署流程", notes = "根据ZIP压缩包部署流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "zipName", value = "设计的流程图和图片的压缩包名称", dataType = "String", paramType = "query", example = "myProcess")
    })
    public RestMessage deployZIP(@RequestParam("zipName") String zipName) {
        RestMessage restMessage = new RestMessage();
        Deployment deployment = null;
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("processes/leaveProcess.zip");
             ZipInputStream zipInputStream = new ZipInputStream(in)) {

            repositoryService.createDeployment()
                    .name("请假流程2")
                    // 指定zip格式的文件完成部署
                    .addZipInputStream(zipInputStream)
                    .deploy();
        } catch (Exception e) {
            restMessage = RestMessage.fail("部署失败", e.getMessage());
            // TODO -- 上线时移除
            e.printStackTrace();
        }
        if (deployment != null) {
            Map<String, String> result = new HashMap<>();
            result.put("deployId", deployment.getId());
            result.put("deployName", deployment.getName());
            restMessage = RestMessage.success("部署成功", result);
        }
        return restMessage;
    }

    @PostMapping(path = "/deleteProcess")
    @ApiOperation(value = "根据部署ID删除流程", notes = "根据部署ID删除流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署ID", dataType = "String", paramType = "query", example = "myProcess")
    })
    public RestMessage deleteProcess(@RequestParam("deploymentId") String deploymentId) {
        RestMessage restMessage = new RestMessage();
        /** 不带级联的删除：只能删除没有启动的流程，如果流程启动，就会抛出异常 */
        try {
            repositoryService.deleteDeployment(deploymentId);
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
            // TODO -- 上线时移除
            e.printStackTrace();
        }

        /** 级联删除：不管流程是否启动，都能可以删除 */
//        repositoryService.deleteDeployment(deploymentId, true);

        restMessage = RestMessage.success("删除成功", null);
        return restMessage;
    }
}
