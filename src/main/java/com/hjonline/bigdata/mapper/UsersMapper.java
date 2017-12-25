package com.hjonline.bigdata.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.hjonline.bigdata.model.Users;

@Mapper
public interface UsersMapper {

	public int insert(Users users);

	public Page<Users> selectAllUsers(HashMap<String, Object> params);

	public long countUsers(HashMap<String, Object> params);
	
	public int updateByPrimaryKey(Users users);
	
	public Users selectByPrimaryKey(long id);
	
	public Users selectByAccountAndPwd(HashMap<String, Object> params);

}
