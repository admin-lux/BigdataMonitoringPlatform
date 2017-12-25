package com.hjonline.bigdata.service;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjonline.bigdata.mapper.TokenVerifyMapper;
import com.hjonline.bigdata.model.TokenVerify;
import com.hjonline.bigdata.model.Users;
import com.hjonline.bigdata.utils.Utils;

@Service
public class TokenVerifyService {

	@Autowired
	private TokenVerifyMapper tokenVerifyMapper;

	@Transactional
	public int addTokenLog(TokenVerify tokenVerify) {
		return tokenVerifyMapper.addTokenLog(tokenVerify);
	}

	public int upTokenLog(TokenVerify ty) {
		return tokenVerifyMapper.upTokenLog(ty);
	}

	public TokenVerify selectByParams(HashMap<String, Object> ps) {
		return tokenVerifyMapper.selectByParams(ps);
	}

	public boolean verifyUser(String token, Users user) {
		if (Utils.isEmpty(token)) {
			return false;
		}
		HashMap<String, Object> ps = new HashMap<>();
		ps.put("token", token);
		ps.put("userId", user.getId());
		ps.put("state", 1);
		TokenVerify ty = tokenVerifyMapper.selectByParams(ps);
		if (null == ty) {
			return false;
		} else {
			ty.setLastdate(new Date());
			upTokenLog(ty);
		}
		return true;
	}

	public boolean verifyAdmin(Users user) {
		HashMap<String, Object> ps = new HashMap<>();
		ps.put("userId", user.getId());
		ps.put("state", 1);
		TokenVerify ty = tokenVerifyMapper.selectByParams(ps);
		if (null != ty) {
			long diff = ty.getLastdate().getTime() - ty.getCrdate().getTime();
			if (diff < (30 * 60 * 1000)) {
				return false;
			} else {
				ty.setState(0);
				upTokenLog(ty);
				return true;
			}
		}
		return true;
	}

}
