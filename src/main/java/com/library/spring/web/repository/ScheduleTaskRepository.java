package com.library.spring.web.repository;

import com.library.spring.web.domain.ScheduleTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, Long> {

    @Query(value = "select * from #{#entityName} b where b.collection=?1", nativeQuery = true)
    List<ScheduleTask> findByCollection(String collection);

    @Query(value = "select collection,service,refreshDuration,executionDate from ScheduleTask b where b.collection = :collection AND b.service=:service")
    List<ScheduleTask> findByCollectionAndService(@Param("collection") String collection, @Param("service") String service);
}
