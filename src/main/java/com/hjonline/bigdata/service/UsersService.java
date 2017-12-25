package com.hjonline.bigdata.service;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hjonline.bigdata.mapper.LoginLogMapper;
import com.hjonline.bigdata.mapper.UsersMapper;
import com.hjonline.bigdata.model.LoginLog;
import com.hjonline.bigdata.model.Users;
import com.hjonline.bigdata.utils.Utils;

@Service
public class UsersService {
	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private LoginLogMapper loginLogMapper;
	
	@Autowired
	private OpeService opeService;

	@Transactional
	public void addUser(Users u) {
		opeService.addOpe("用户管理", String.format("增加了用户[%s]记录", u.getName()), 1);
		usersMapper.insert(u);
	}

	public long countUsers(HashMap<String, Object> parmas) {
		return usersMapper.countUsers(parmas);
	}

	public Page<Users> selectAll(int page, int rows, HashMap<String, Object> parmas) {
		// 1、设置分页信息，包括当前页数和每页显示的总计数
		PageHelper.startPage(page, rows, true);
		// 2、执行查询
		return usersMapper.selectAllUsers(parmas);
	}

	@Transactional
	public int upUser(Users users) {
		if(users.getStatus() == 0) {
			opeService.addOpe("用户管理", String.format("删除了用户[%s]记录", users.getName()), 1);
		}else {
			opeService.addOpe("用户管理", String.format("修改了用户[%s]记录", users.getName()), 1);
		}
		return usersMapper.updateByPrimaryKey(users);
	}

	public Users selectByPrimaryKey(long id) {
		return usersMapper.selectByPrimaryKey(id);
	}

	public Users selectByAccountAndPwd(HashMap<String, Object> params) {
		return usersMapper.selectByAccountAndPwd(params);
	}

	@Transactional
	public boolean login(String ip, String account, String pwd) {
		boolean result = false;
		Users users = null;

		HashMap<String, Object> params = new HashMap<>();
		if (!Utils.isEmpty(account)) {
			params.put("account", account);
			users = selectByAccountAndPwd(params);
		}
		LoginLog log = new LoginLog();
		log.setCrdate(new Date());
		log.setUpdate(new Date());
		log.setLogindate(new Date());
		log.setIp(ip);
		log.setAccount(account);
		log.setPassword(pwd);
		if (null == users) {
			log.setState(0);
			log.setDesc("无此用户");
			result = false;
		} else {
			if (users.getStatus() == 0) {
				log.setState(0);
				log.setDesc("用户已删除");
				result = false;
			} else if (!Utils.isEmpty(pwd) && users.getPassword().equals(pwd)) {
				log.setUser_id(users.getId());
				log.setState(1);
				log.setDesc("");
				result = true;
				users.setLastdate(new Date());
				upUser(users);
			} else {
				log.setState(0);
				log.setDesc("密码错误");
				result = false;
			}
		}
		loginLogMapper.addLog(log);
		return result;
	}

}
