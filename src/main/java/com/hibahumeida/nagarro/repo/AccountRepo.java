/**
 * 
 */
package com.hibahumeida.nagarro.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hibahumeida.nagarro.dao.Account;


/**
 * @author h.humeida
 *
 */
@Repository
public interface AccountRepo extends CrudRepository<Account, String> {

}
