package com.hjonline.bigdata.service;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hjonline.bigdata.base.LocalManager;
import com.hjonline.bigdata.base.LocalManagerType;
import com.hjonline.bigdata.mapper.OpeMapper;
import com.hjonline.bigdata.model.Ope;
import com.hjonline.bigdata.model.Users;

@Service
public class OpeService {
	@Autowired
	private OpeMapper opeMapper;
	@Autowired
	private UsersService usersService;
	
	public int addOpe(String funName, String desc, int state) {
		Users users = usersService.selectByPrimaryKey(1);
		Ope ope = new Ope();
		ope.setOpdate(new Date());
		ope.setUser_id(users.getId());
		ope.setIp(LocalManager.getVal(LocalManagerType.IP));
		ope.setFunName(funName);
		ope.setState(state);
		ope.setDesc(desc);
		ope.setCrdate(new Date());
		ope.setUpdate(new Date());
		return addOpe(ope);
	}

	public int addOpe(Ope ope) {
		return opeMapper.addOpe(ope);
	}

	public Page<Ope> selectOpes(int page, int size, HashMap<String, Object> params) {
		PageHelper.startPage(page, size, true);
		PageHelper.orderBy("opdate desc");
		return opeMapper.selectOpes(params);
	}
}
