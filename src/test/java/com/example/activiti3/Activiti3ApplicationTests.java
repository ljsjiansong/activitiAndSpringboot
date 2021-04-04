package com.example.activiti3;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.ClaimTaskPayload;
import org.activiti.api.task.runtime.TaskRuntime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Activiti3ApplicationTests {

    @Autowired
    private ProcessRuntime processRuntime; // 流程定义相关操作

    @Autowired
    private TaskRuntime taskRuntime; // 任务相关操作

    @Autowired
    private SecurityUtil securityUtil;

    @Test
    public void contextLoads() {
        securityUtil.logInAs("salaboy");
        // 分页查询流程定义信息（springboot启动时，已经部署了）
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        System.out.println(processDefinitionPage.getTotalItems()); // 流程定义个数
        // 得到当前流程定义信息
        for(ProcessDefinition processDefinition :processDefinitionPage.getContent()){
            System.out.println("流程定义"+processDefinition);
        }

    }
    @Test
    public void startInstance(){
        securityUtil.logInAs("salaboy");
        ProcessInstance instance = processRuntime.start(ProcessPayloadBuilder.start().withProcessDefinitionKey("myProcess_1").build());
        System.out.println("流程定义key："+instance.getProcessDefinitionKey());
    }

    @Test
    public void completeInstance(){
        securityUtil.logInAs("erdemedeiros");
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
        if (tasks.getTotalItems()>0){
            // 有任务
            for (Task task:tasks.getContent()){
                System.out.println("任务信息:"+task);

                //如果是组任务。先拾取任务
               // taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            }
        }

        // 再次查询任务
        tasks = taskRuntime.tasks(Pageable.of(0, 10));
        if (tasks.getTotalItems()>0){
            for (Task task:tasks.getContent()){
                System.out.println("任务信息:"+task);
            }
        }
    }

}
