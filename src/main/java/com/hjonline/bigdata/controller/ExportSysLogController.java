package com.hjonline.bigdata.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hjonline.bigdata.model.request.ExportSysLogParams;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.SyslogService;
import com.hjonline.bigdata.utils.Utils;

/**
 * @author rick_lu
 *
 */
@RestController
@RequestMapping("exportSysLog")
public class ExportSysLogController {

	@Autowired
	private SyslogService syslogService;

	@RequestMapping(value = "exportSysLog", method = RequestMethod.POST)
	public BaseResponse exportSysLog(@RequestBody ExportSysLogParams ps) throws IOException {
		if (null == ps || Utils.isEmpty(ps.getAbstractPaths()) || Utils.isEmpty(ps.getIndexDate())) {
			return BaseResponseFactory.get500();
		}
		File file = new File(ps.getAbstractPaths());
		if (!file.isDirectory()) {
			return BaseResponseFactory.get500();
		}
		
		File[] listFiles = file.listFiles();
		for (File pf : listFiles) {
			if (ps.getType() == 1 && !pf.getName().equals(ps.getIndexDate())) {
				continue;
			}
			System.out.println("...");

			if (!pf.isDirectory()) {
				continue;
			}
			for (File f : pf.listFiles()) {
				String info = readLog(f).replace("'", "\\'");
				String fullname = f.getName().substring(0, f.getName().lastIndexOf(".sys"));
				String taskname = pf.getName().replace("-", "") + "_" /*+ a.get(fullname)*/;
				String tablename = fullname;

				String sql = String.format(
						"INSERT IGNORE INTO `bigdata`.`export_sys_log` (`id`, `taskname`, `tablename`, `info`, `createtime`, `updatetime`) "
								+ "VALUES (null, '%s', '%s', \"%s\", now(), now());\r\n",
						taskname, tablename, info);
			}
		}
		
		
		
		
		return BaseResponseFactory.get200();
	}
	

	private String readLog(File file) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		br.close();
		return sb.toString();
	}
}
