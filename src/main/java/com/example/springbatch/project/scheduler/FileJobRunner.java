package com.example.springbatch.project.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 스케줄러 설정
 */
@Component
public class FileJobRunner extends JobRunner {

    @Autowired
    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {

        String[] sourceArgs = args.getSourceArgs();

        JobDetail jobDetail = buildJobDetail(FileSchJob.class, "fileJob", "batch", new HashMap());
        Trigger trigger = buildJobTrigger("0/30 * * * * ?"); // 30초마다 실행
        jobDetail.getJobDataMap().put("requestDate", sourceArgs[0]);

        try{
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}