package com.hibahumeida.nagarro.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hibahumeida.nagarro.dao.Statement;

/**
 * @author h.humeida
 *
 */
@Repository
public interface StatementRepo extends CrudRepository<Statement, String> {

	List<Statement> findByAccountId(String accountID);
}
