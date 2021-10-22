package com.deo.activitipro.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * {@link TaskListener}
 * {@link ExecutionListener}
 *
 * @author SL Zhou
 * @date 2021-10-22
 * @since 1.0.0
 */
@Component
public class MyTaskListener implements TaskListener, ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("xml流程：" + execution.getId() + " ActivitiListenner" + this.toString());
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("xml任务：" + delegateTask.getId() + " ActivitiListenner" + this.toString());
    }
}
