package com.hjonline.bigdata.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hjonline.bigdata.base.LocalManager;
import com.hjonline.bigdata.base.LocalManagerType;
import com.hjonline.bigdata.base.PageInfo;
import com.hjonline.bigdata.base.conf.Audience;
import com.hjonline.bigdata.base.jwt.AccessToken;
import com.hjonline.bigdata.base.jwt.JwtHelper;
import com.hjonline.bigdata.model.TokenVerify;
import com.hjonline.bigdata.model.Users;
import com.hjonline.bigdata.model.request.SelectAllUsersParams;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.TokenVerifyService;
import com.hjonline.bigdata.service.UsersService;
import com.hjonline.bigdata.utils.Utils;

@RestController
@RequestMapping("users")
public class UsersController {

	@Autowired
	private UsersService usersService;

	@Autowired
	private Audience audienceEntity;

	@Autowired
	private TokenVerifyService tokenVerifyService;

	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public BaseResponse logout(@RequestBody Users users) {
		if (null == users || users.getId() <= 0) {
			return BaseResponseFactory.getParamError_500();
		}

		HashMap<String, Object> ps = new HashMap<>();
		ps.put("userId", users.getId());
		ps.put("token", LocalManager.getVal(LocalManagerType.TOKEN));
		ps.put("state", 1);
		TokenVerify ty = tokenVerifyService.selectByParams(ps);
		if (null != ty) {
			ty.setState(0);
			tokenVerifyService.upTokenLog(ty);
		}

		return BaseResponseFactory.get200();
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public BaseResponse login(@RequestBody Users users) {
		if (null == users || (null == users.getAccount() || "".equals(users.getAccount()))
				|| (null == users.getPassword() || "".equals(users.getPassword()))) {
			return BaseResponseFactory.getParamError_500();
		}
		boolean result = usersService.login(LocalManager.getVal(LocalManagerType.IP), users.getAccount(),
				users.getPassword());
		if (!result) {
			return BaseResponseFactory.getErrorResponse("500", "账号，密码，用户状态，有误！");
		}

		HashMap<String, Object> params = new HashMap<>();
		if (!Utils.isEmpty(users.getPassword())) {
			params.put("password", users.getPassword());
		}
		if (!Utils.isEmpty(users.getAccount())) {
			params.put("account", users.getAccount());
		}
		Users user = usersService.selectByAccountAndPwd(params);

		if (user.getAdmin() == 1) {
			// 验证管理员是否登陆
			if (!tokenVerifyService.verifyAdmin(user)) {
				return BaseResponseFactory.getErrorResponse("500", "账号使用中");
			}
		}

		// 拼装accessToken
		String accessToken = JwtHelper.createJWT(user.getAccount(), String.valueOf(user.getId()),
				String.valueOf(user.getAdmin()), audienceEntity.getClientId(), audienceEntity.getName(),
				audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());

		TokenVerify tv = new TokenVerify();
		tv.setUserId(user.getId());
		tv.setToken(accessToken);
		tv.setState(1);
		tokenVerifyService.addTokenLog(tv);

		// 返回accessToken
		AccessToken accessTokenEntity = new AccessToken();
		accessTokenEntity.setAccess_token(accessToken);
		accessTokenEntity.setExpires_in(audienceEntity.getExpiresSecond());
		accessTokenEntity.setToken_type("bearer");
		Object[] objects = new Object[] { accessTokenEntity, user };
		return BaseResponseFactory.get200().setData(objects);
	}

	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	public BaseResponse addUser(@RequestBody Users users) {
		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getParamError_500();
		}

		if (null == users || (null == users.getName() || "".equals(users.getName()))
				|| (null == users.getAccount() || "".equals(users.getAccount()))
				|| (null == users.getPassword() || "".equals(users.getPassword()))) {
			return BaseResponseFactory.getParamError_500();
		}
		users.setCrdate(new Date());
		users.setUpdate(new Date());
		usersService.addUser(users);
		return BaseResponseFactory.get200().setData(users);
	}

	@RequestMapping(value = "upUser", method = RequestMethod.POST)
	public BaseResponse upUser(@RequestBody Users users) {
		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getParamError_500();
		}
		if (null == users || (null == users.getName() || "".equals(users.getName())) || (users.getId() < 0)) {
			return BaseResponseFactory.getParamError_500();
		}
		String name = users.getName();

		users = usersService.selectByPrimaryKey(users.getId());
		users.setName(name);
		usersService.upUser(users);
		return BaseResponseFactory.get200().setData(users);
	}

	@RequestMapping(value = "upUserPwd", method = RequestMethod.POST)
	public BaseResponse upUserPwd(@RequestBody Users users) {
		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getParamError_500();
		}

		if (null == users || (null == users.getPassword() || "".equals(users.getPassword())) || (users.getId() < 0)) {
			return BaseResponseFactory.getParamError_500();
		}
		String pwd = users.getPassword();

		users = usersService.selectByPrimaryKey(users.getId());
		users.setPassword(pwd);
		usersService.upUser(users);
		return BaseResponseFactory.get200().setData(users);
	}

	@RequestMapping(value = "upUserStatus", method = RequestMethod.POST)
	public BaseResponse upUserStatus(@RequestBody Users users) {
		if (null == users || (users.getId() < 0) || (users.getStatus() < 0)) {
			return BaseResponseFactory.getParamError_500();
		}

		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getParamError_500();
		}
		if (currentUser.getId() == users.getId()) {
			return BaseResponseFactory.getParamError_500();
		}

		int status = users.getStatus();
		users = usersService.selectByPrimaryKey(users.getId());
		users.setStatus(status);
		usersService.upUser(users);
		return BaseResponseFactory.get200().setData(users);
	}

	@RequestMapping(value = "upUsersStatus", method = RequestMethod.POST)
	public BaseResponse upUsersStatus(@RequestBody List<Users> users) {
		if (null == users || users.size() <= 0) {
			return BaseResponseFactory.getParamError_500();
		}

		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getParamError_500();
		}

		List<Users> result = new ArrayList<>();
		for (Users u : users) {
			if (currentUser.getId() == u.getId()) {
				continue;
			}
			if (null == u || (u.getId() < 0) || (u.getStatus() < 0)) {
				continue;
			}
			int status = u.getStatus();
			Users user = usersService.selectByPrimaryKey(u.getId());
			user.setStatus(status);
			usersService.upUser(user);
			result.add(user);
		}
		return BaseResponseFactory.get200().setData(result);
	}

	@RequestMapping(value = "selectAll", method = RequestMethod.POST)
	public BaseResponse selectAll(@RequestBody SelectAllUsersParams usersParams) {
		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getParamError_500();
		}

		if (null == usersParams.getPage()) {
			usersParams.setPage(1);
		}
		if (null == usersParams.getSize()) {
			usersParams.setSize(10);
		}
		HashMap<String, Object> params = new HashMap<>();
		if (!Utils.isEmpty(usersParams.getName())) {
			params.put("name", usersParams.getName());
		}
		if (!Utils.isEmpty(usersParams.getAccount())) {
			params.put("account", usersParams.getAccount());
		}
		if (null != usersParams.getStatus()) {
			if (1 == usersParams.getStatus()) {
				params.put("status", 1);
			}
			if (2 == usersParams.getStatus()) {
				params.put("status", 0);
			}
		}
		return BaseResponseFactory.get200()
				.setData(new PageInfo<>(usersService.selectAll(usersParams.getPage(), usersParams.getSize(), params)));
	}

}
