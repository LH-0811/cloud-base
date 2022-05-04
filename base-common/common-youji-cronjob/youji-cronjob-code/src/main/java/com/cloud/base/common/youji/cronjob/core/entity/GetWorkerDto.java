package com.cloud.base.common.youji.cronjob.core.entity;

import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskInfo;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2TaskWorker;
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

    private Youji2TaskInfo taskInfo;

    private List<Youji2TaskWorker> taskWorkerList;
}
