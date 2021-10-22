package com.deo.activitipro.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.springframework.stereotype.Component;

/**
 * {@link ActivitiEventListener}
 *
 * @author SL Zhou
 * @date 2021-10-22
 * @since 1.0.0
 */
@Component
public class MyActivitiEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            // 流程开始
            case PROCESS_STARTED:
                System.out.println("流程开始");
                break;
            // 流程结束
            case PROCESS_COMPLETED:
                System.out.println("流程结束");
                break;
            // 任务开始
            case TASK_CREATED:
                System.out.println("任务开始");
                break;
            // 任务完成
            case TASK_COMPLETED:
                System.out.println("任务完成");
                break;
            // 进程取消，删除实例
            case PROCESS_CANCELLED:
                System.out.println("进程取消，删除实例");
                break;
            default:
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
