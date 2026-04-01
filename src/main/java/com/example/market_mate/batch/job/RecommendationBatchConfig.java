package com.example.market_mate.batch.job;

import com.example.market_mate.domain.recommendation.service.RecommendationService;
import com.example.market_mate.domain.user.entity.User;
import com.example.market_mate.domain.user.entity.UserStatus;
import com.example.market_mate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RecommendationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserRepository userRepository;
    private final RecommendationService recommendationService;

    @Bean
    public Job recommendationJob() {
        return new JobBuilder("recommendationJob", jobRepository)
                .start(recommendationStep())
                .build();
    }

    @Bean
    public Step recommendationStep() {
        return new StepBuilder("recommendationStep", jobRepository)
                .tasklet(recommendationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet recommendationTasklet() {
        return (contribution, chunkContext) -> {
            log.info("===== 발주 추천 배치 시작 =====");

            // 활성 유저 전체 조회
            List<User> activeUsers = userRepository.findAll()
                    .stream()
                    .filter(u -> u.getStatus() == UserStatus.ACTIVE)
                    .toList();

            for (User user : activeUsers) {
                try {
                    recommendationService.generateRecommendations(user.getId());
                    log.info("유저 {} 발주 추천 생성 완료", user.getLoginId());
                } catch (Exception e) {
                    log.error("유저 {} 발주 추천 생성 실패: {}",
                            user.getLoginId(), e.getMessage());
                }
            }

            log.info("===== 발주 추천 배치 완료 =====");
            return RepeatStatus.FINISHED;
        };
    }
}