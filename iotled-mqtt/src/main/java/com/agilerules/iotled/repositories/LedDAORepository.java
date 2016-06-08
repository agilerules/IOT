package com.agilerules.iotled.repositories;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agilerules.iotled.model.LedModel;

/**
 * A DAO for the entity LedModel which extends Spring's JPARepository interface 
 * and the findTop10ByOrderByIdDesc will fetch the top 10 records from the table
 * led_details order by the primary key field Id in descending order.
 */
@Transactional
public interface LedDAORepository extends JpaRepository<LedModel, Long>{
	
	public List<LedModel> findTop10ByOrderByIdDesc();

}
