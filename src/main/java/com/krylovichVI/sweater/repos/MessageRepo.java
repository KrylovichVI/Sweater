package com.krylovichVI.sweater.repos;

import com.krylovichVI.sweater.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface MessageRepo extends CrudRepository<Message, Long> {
    List<Message> findByTag(String tag);
}
