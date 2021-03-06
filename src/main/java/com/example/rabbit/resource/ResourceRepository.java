package com.example.rabbit.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: huyang
 * @Version: 1.0
 * @Date: 16:00 2018/5/29
 */
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    @Query(value = "select id,data_type from sync_data where data_type=?1 and source=?2 limit ?3,?4",nativeQuery=true)
    List<Resource> findAllByType(String type,String sourceType,
                                 Integer start,
                                 Integer count);
}
