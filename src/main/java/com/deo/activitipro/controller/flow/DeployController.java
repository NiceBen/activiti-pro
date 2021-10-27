package com.deo.activitipro.controller.flow;

import com.deo.activitipro.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
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



    // ------------------------------------------------------------------------------------------------

    /**
     *
     * 功能描述:classpath部署流程
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/deploy2")
    public String deploy(HttpServletRequest request) {
        // TODO -- ①优化参数；②优化非空判断；③优化返回结果；
        String name = request.getParameter("name");
        String resource = request.getParameter("resource");

        // 非空判断
//        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(resource)) {
//            return "param error";
//        }

        try {
            // 创建一个部署对象
            Deployment deploy = repositoryService.createDeployment().name(name)
                    .addClasspathResource("processes/" + resource)
                    .deploy();
            System.out.println("部署成功：" + deploy.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:zip部署流程
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/deploy/zip")
    public String deployZip(HttpServletRequest request) {
        String name = request.getParameter("name");
        String zip = request.getParameter("zip");

        // 非空判断

        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("processes/" + zip);
            ZipInputStream zipInputStream = new ZipInputStream(in);
            Deployment deploy = repositoryService.createDeployment()
                    .name(name)
                    .addZipInputStream(zipInputStream)
                    .deploy();
            System.out.println("部署ID：" + deploy.getId());
            System.out.println("部署名称：" + deploy.getName());
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:查询流程定义
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/definition/query")
    public String query(HttpServletRequest request) {

        try {
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                    /* 指定查询条件,where条件 */
                    // .deploymentId(deploymentId)//使用部署对象ID查询
                    // .processDefinitionId(processDefinitionId)//使用流程定义ID查询
                    // .processDefinitionKey(processDefinitionKey)//使用流程定义的KEY查询
                    // .processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询

                    /* 排序 */
                    .orderByProcessDefinitionVersion().asc()// 按照版本的升序排列
                    // .orderByProcessDefinitionName().desc()//按照流程定义的名称降序排列
                    .list();// 返回一个集合列表，封装流程定义
            // .singleResult();//返回唯一结果集
            // .count();//返回结果集数量
            // .listPage(firstResult, maxResults)//分页查询
            if (list != null && list.size() > 0) {
                for (ProcessDefinition processDefinition : list) {
                    System.out.println("流程定义ID:" + processDefinition.getId());// 流程定义的key+版本+随机生成数
                    System.out.println("流程定义名称:" + processDefinition.getName());// 对应HelloWorld.bpmn文件中的name属性值
                    System.out.println("流程定义的key:" + processDefinition.getKey());// 对应HelloWorld.bpmn文件中的id属性值
                    System.out.println("流程定义的版本:" + processDefinition.getVersion());// 当流程定义的key值相同的情况下，版本升级，默认从1开始
                    System.out.println("资源名称bpmn文件:" + processDefinition.getResourceName());
                    System.out.println("资源名称png文件:" + processDefinition.getDiagramResourceName());
                    System.out.println("部署对象ID:" + processDefinition.getDeploymentId());
                    System.out.println("*****************************************************************************");
                }
            }
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:删除流程定义
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/deployment/delete")
    public String deleteDeployment(HttpServletRequest request) {
        String deploymentId = request.getParameter("deployId");

        // 非空校验

        try {
//			// 不带级联的删除：只能删除没有启动的流程，如果流程启动，则抛出异常
//			repositoryService.deleteDeployment(deploymentId);
            // 能级联的删除：能删除启动的流程，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
            repositoryService.deleteDeployment(deploymentId, true);
            System.out.println("删除成功:" + deploymentId);
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }


    /**
     * 功能描述:流程定义挂起
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("definition/suspend")
    public String suspendProcessDefinition(HttpServletRequest request) {
        String processDefinitionId = request.getParameter("processDefinitionId");

        // 非空校验

        try {
            // 根据一个流程定义的id挂起该流程实例
            repositoryService.suspendProcessDefinitionById(processDefinitionId);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionId).singleResult();
            System.out.println("流程定义ID:" + processDefinition.getId());
            System.out.println("流程定义状态:" + processDefinition.isSuspended());
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    /**
     * 功能描述:流程定义激活
     *
     * @param request
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @PostMapping("/definition/activate")
    public String activateProcessDefinition(HttpServletRequest request) {
        String processDefinitionId = request.getParameter("processDefinitionId");

        // 非空校验

        try {
            repositoryService.activateProcessDefinitionById(processDefinitionId);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionId).singleResult();
            System.out.println("流程定义ID:" + processDefinition.getId());
            System.out.println("流程定义状态:" + processDefinition.isSuspended());
            System.out.println("*****************************************************************************");
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }
}
