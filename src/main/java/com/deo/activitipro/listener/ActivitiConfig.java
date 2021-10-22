package com.deo.activitipro.listener;

import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration
 *
 * @author SL Zhou
 * @date 2021-10-22
 * @since 1.0.0
 */
@Configuration
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private MyActivitiEventListener myActivitiEventListener;

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        List<ActivitiEventListener> listeners = new ArrayList<>();

        // 配置全局监听器
        listeners.add(myActivitiEventListener);
        System.out.println("注册监听");
        processEngineConfiguration.setEventListeners(listeners);
    }
}
