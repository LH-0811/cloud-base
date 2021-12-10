package com.cloud.base.modules.youji.code.dto;

import com.cloud.base.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.modules.youji.code.repository.entity.TaskWorker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/11/23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetWorkerDto {

    private TaskInfo taskInfo;

    private List<TaskWorker> taskWorkerList;
}
