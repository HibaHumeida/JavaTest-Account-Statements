package com.hibahumeida.nagarro.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hibahumeida.nagarro.constant.ResponseCodes;
import com.hibahumeida.nagarro.constant.StatementSearchCriteria;
import com.hibahumeida.nagarro.dao.Statement;
import com.hibahumeida.nagarro.dto.AppResponse;
import com.hibahumeida.nagarro.dto.StatementSearchParams;
import com.hibahumeida.nagarro.repo.StatementRepo;

/**
 * @author h.humeida
 *
 */
@Service
public class StatementService {

	private static final Logger log = LogManager.getLogger(StatementService.class);

	@Autowired
	StatementRepo statementRepo;

	@Autowired
	AccountService accountService;

	public AppResponse<List<Statement>> getAccountStatement(String accountID, StatementSearchParams params) {

		List<Statement> statements = statementRepo.findByAccountId(accountID);
		if (statements.isEmpty()) {
			return new AppResponse<>(ResponseCodes.NO_STATEMENTS_FOUND, statements);
		}
		List<Predicate<Statement>> allPredicates = getAllPredicates(params);
		statements = statements.stream().filter(allPredicates.stream().reduce(x -> true, Predicate::and))
				.collect(Collectors.toList());
		return new AppResponse<>(ResponseCodes.SUCCESS, statements);

	}

	private List<Predicate<Statement>> getAllPredicates(StatementSearchParams params) {
		List<Predicate<Statement>> allPredicates = new ArrayList<>();

		if (!Objects.isNull(params.getMinAmount())) {
			Predicate<Statement> predicateMinAmount = getPredicate(params, StatementSearchCriteria.MINIMUM_AMOUNT);
			allPredicates.add(predicateMinAmount);
		}
		if (!Objects.isNull(params.getMaxAmount())) {
			Predicate<Statement> predicateMaxAmount = getPredicate(params, StatementSearchCriteria.MAXIMUM_AMOUNT);
			allPredicates.add(predicateMaxAmount);
		}
		if (!Objects.isNull(params.getFromDate())) {
			Predicate<Statement> predicateFromDate = getPredicate(params, StatementSearchCriteria.FROM_DATE);
			allPredicates.add(predicateFromDate);
		}
		if (!Objects.isNull(params.getToDate())) {
			Predicate<Statement> predicatetoDate = getPredicate(params, StatementSearchCriteria.TO_DATE);
			allPredicates.add(predicatetoDate);

		}
		return allPredicates;

	}

	private Predicate<Statement> getPredicate(StatementSearchParams params,
			StatementSearchCriteria statementSearchCriteria) {
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		Predicate<Statement> predicate;
		switch (statementSearchCriteria) {
		case MINIMUM_AMOUNT:
			predicate = s -> (params.getMinAmount() <= s.getAmount());
			break;
		case MAXIMUM_AMOUNT:
			predicate = s -> (s.getAmount() <= params.getMaxAmount());
			break;
		// parse statement dates and compare it with search date
		case FROM_DATE:
			predicate = s -> {
				try {
					if (format.parse(s.getDatefield()).after(params.getFromDate())) {
						return true;
					}
					return false;
				} catch (ParseException e) {
					log.catching(e);
				}
				return false;
			};
			break;

		case TO_DATE:
			predicate = s -> {
				try {
					if (format.parse(s.getDatefield()).before(params.getToDate())) {
						return true;
					}
					return false;
				} catch (ParseException e) {
					log.catching(e);
				}
				return false;
			};
			break;
		default:
			predicate = s -> true;
			break;
		}
		return predicate;
	}
}
