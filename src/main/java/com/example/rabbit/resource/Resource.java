package com.example.rabbit.resource;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: huyang
 * @Version: 1.0
 * @Date: 16:01 2018/5/29
 */
@Data
@Entity
@Table(name = "sync_data")
public class Resource {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Integer id;
    private String data_type;
}
