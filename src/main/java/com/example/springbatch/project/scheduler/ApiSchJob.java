package com.example.springbatch.project.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ApiSchJob extends QuartzJobBean {

    /**
     * 배치를 실행시키는 구문
     * @param context
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    }
}
