package com.example.activiti3.controller;

import com.example.activiti3.SecurityUtil;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/test")
    public void testComplete(){
        securityUtil.logInAs("ryandawsonuk");
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
