package com.example.market_mate.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class RecommendationScheduler {

    private final JobLauncher jobLauncher;
    private final Job recommendationJob;

    // 매일 아침 6시 자동 실행
    @Scheduled(cron = "0 0 6 * * *")
    public void runRecommendationJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(recommendationJob, jobParameters);
            log.info("발주 추천 배치 실행 완료");
        } catch (Exception e) {
            log.error("발주 추천 배치 실행 실패: {}", e.getMessage());
        }
    }
}