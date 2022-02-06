package com.example.springbatch.project.scheduler;

import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileSchJob extends QuartzJobBean {

    @Autowired
    private Job fileJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer; // jobRepository의 read 기능만 가지고 있음

    /**
     * 배치를 실행시키는 구문
     * @param context
     * @throws JobExecutionException
     */
    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        String requestDate = (String) context.getJobDetail().getJobDataMap().get("requestDate");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .addString("requestDate", requestDate)
                .toJobParameters();

        // 모든 job의 인스턴스 갯수를 가져올수 있음.
        int jobInstanceCount = jobExplorer.getJobInstanceCount(fileJob.getName());

        // 모든 job의 인스턴스 정보를 가져올수 있음. 0번째 부터 카운트 갯수까지
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(fileJob.getName(), 0, jobInstanceCount);

        if(jobInstances.size() > 0) {
            for (JobInstance jobInstance : jobInstances) {
                // 여러개의 jobExecution을 가져온다.
                List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
                List<JobExecution> jobExecutionList = jobExecutions.stream()
                        .filter(jobExecution -> jobExecution.getJobParameters().getString("requestDate").equals(requestDate))
                        .collect(Collectors.toList());

                // 해당하는 날짜가 1개 이상인경우 배치를 실행하지 않는다.
                if(jobExecutionList.size() > 0) {
                    throw new JobExecutionException(requestDate + " already exists");
                }
            }

        }

        jobLauncher.run(fileJob, jobParameters);
    }

}