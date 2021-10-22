package com.deo.activitipro.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * {@link TaskListener}
 *
 * @author SL Zhou
 * @date 2021-10-22
 * @since 1.0.0
 */
@Component
public class MySampleTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("getEventName"+delegateTask.getEventName());
        System.out.println("getCategory"+delegateTask.getCategory());
        System.out.println("getDelegationState"+delegateTask.getDelegationState());
    }
}
