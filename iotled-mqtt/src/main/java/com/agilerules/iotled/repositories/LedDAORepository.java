package com.agilerules.iotled.repositories;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agilerules.iotled.model.LedModel;

/**
 * A DAO for the entity User is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 * 
 */
@Transactional
public interface LedDAORepository extends JpaRepository<LedModel, Long>{
	
	public List<LedModel> findTop10ByOrderByIdDesc();

}
