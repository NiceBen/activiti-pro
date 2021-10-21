package com.deo.activitipro.api;

import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.payloads.*;
import org.activiti.api.task.runtime.conf.TaskRuntimeConfiguration;
import org.activiti.engine.impl.Page;
import org.activiti.engine.task.Task;

/**
 * @date 2021-10-18
 * @since 1.0.0
 */
public interface TaskRuntime {
    TaskRuntimeConfiguration configuration();

    Task task(String taskId);

    Page tasks(Pageable pageable);

    Page tasks(Pageable pageable, GetTasksPayload payload);

    Task create(CreateTaskPayload payload);

    Task claim(ClaimTaskPayload payload);

    Task release(ReleaseTaskPayload payload);

    Task complete(CompleteTaskPayload payload);

    Task update(UpdateTaskPayload payload);

    Task delete(DeleteTaskPayload payload);

    // ...
}
