package com.library.spring.config;

import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@EnableAsync
@Configuration
@ComponentScan({ "com.library.spring.scheduler" })
public class SchedulerConfig implements AsyncConfigurer {

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory(JobFactory jobFactory,
                                                 DataSource dataSource, Executor taskExecutor) throws SchedulerException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setSchedulerName("QuartzScheduler");
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("config/quartz.properties"));
        schedulerFactoryBean.setTaskExecutor(taskExecutor);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(false);
        return schedulerFactoryBean;
    }

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(6);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("emp-");
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
