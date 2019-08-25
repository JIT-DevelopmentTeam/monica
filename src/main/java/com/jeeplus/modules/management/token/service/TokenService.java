/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.token.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.token.entity.Token;
import com.jeeplus.modules.management.token.mapper.TokenMapper;

/**
 * Token表Service
 * @author KicoChan
 * @version 2019-08-24
 */
@Service
@Transactional(readOnly = true)
public class TokenService extends CrudService<TokenMapper, Token> {

	public Token get(String id) {
		return super.get(id);
	}
	
	public List<Token> findList(Token token) {
		return super.findList(token);
	}
	
	public Page<Token> findPage(Page<Token> page, Token token) {
		return super.findPage(page, token);
	}
	
	@Transactional(readOnly = false)
	public void save(Token token) {
		super.save(token);
	}
	
	@Transactional(readOnly = false)
	public void delete(Token token) {
		super.delete(token);
	}
	
}