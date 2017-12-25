package com.hjonline.bigdata.controller;

import static com.hjonline.bigdata.utils.DateUtils.getFormat;
import static com.hjonline.bigdata.utils.DateUtils.getFormat2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.hjonline.bigdata.base.PageInfo;
import com.hjonline.bigdata.controller.em.SqlType;
import com.hjonline.bigdata.model.Datasources;
import com.hjonline.bigdata.model.Import;
import com.hjonline.bigdata.model.Monitor;
import com.hjonline.bigdata.model.request.CrYamlParams;
import com.hjonline.bigdata.model.request.SelectPageMonitorsParams;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.DatasourceService;
import com.hjonline.bigdata.service.ImportService;
import com.hjonline.bigdata.service.MonitorService;
import com.hjonline.bigdata.service.SyslogService;
import com.hjonline.bigdata.utils.MatchDate;
import com.hjonline.bigdata.utils.Utils;

/**
 * 
 * @author rick_lu
 *
 */
@RestController
@RequestMapping("monitor")
public class MonitorController {
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private SyslogService syslogService;
	@Autowired
	private ImportService importService;
	@Autowired
	private YamlController yamlController;
	@Autowired
	private DatasourceService datasourceService;

	/**
	 * 
	 * @param tn
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "downloadFailedsYaml/{taskName}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadFailedsYaml(@PathVariable("taskName") String tn)
			throws IOException {
		if (Utils.isEmpty(tn)) {
			return ResponseEntity.status(500).build();
		}
		List<Monitor> faileds = monitorService.selectFaileds(tn);
		CrYamlParams ps = new CrYamlParams();
		List<Import> datas = new ArrayList<>();
		faileds.forEach(f -> {
			List<Import> is = importService.selectByParams(getTableName(f.getTablename()),
					getTaskName(f.getTaskname()));
			is.forEach(s -> {
				if (f.getTablename().toUpperCase().contains(s.getTableName().toUpperCase())) {
					Datasources ds = datasourceService.selectById(s.getDatasourceId());
					s.setIncrementIf(MatchDate.getWhereSql(s.getIncrementIf(),
							(1 == ds.getSourceType()) ? SqlType.ORACLE : SqlType.MYSQL, f.getStime()));
					datas.add(s);
				}
			});
		});
		if (datas.size() <= 0) {
			return ResponseEntity.status(500).build();
		}
		ps.setType(0);
		ps.setDatas(datas);
		return yamlController.downloadYaml(Long.parseLong(yamlController.crYaml(ps).getData() + ""));
	}

	private String getTableName(String t) {
		if (!Utils.isEmpty(t) && t.indexOf("_") < t.length()) {
			return t.substring(t.indexOf("_") + 1);
		}
		return t;
	}

	private String getTaskName(String t) {
		if (!Utils.isEmpty(t) && t.indexOf("_") > 0 && t.indexOf("_") < t.length()) {
			return t.substring(t.indexOf("_") + 1);
		}
		return t;
	}

	@RequestMapping(value = "exportLog/{taskName}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> exportLog(@PathVariable("taskName") String tn) throws IOException {
		if (Utils.isEmpty(tn)) {
			return ResponseEntity.status(500).build();
		}

		HashMap<String, Object> ps = new HashMap<>();
		if (!Utils.isEmpty(tn)) {
			ps.put("taskName", tn);
		}
		List<Monitor> mGroupBys = monitorService.selectMonitors(ps);
		if (null == mGroupBys || mGroupBys.size() <= 0) {
			return ResponseEntity.status(500).build();
		}

		StringBuffer sb = new StringBuffer();
		mGroupBys.forEach(m -> {
			sb.append("\n");
			sb.append("任务运行详情").append("\n");
			long count = monitorService.countMonitors(m.getTaskname());
			List<Monitor> faileds = monitorService.selectFaileds(m.getTaskname());

			if (null == faileds || faileds.size() <= 0) {
				m.setResult("成功");
			} else if (count > faileds.size()) {
				m.setResult("部分成功");
			} else {
				m.setResult("失败");
			}

			sb.append("任务名称:").append(",").append(m.getTaskname()).append("\n");
			sb.append("执行结果:").append(",").append(m.getResult()).append("\n");
			sb.append("执行开始时间:").append(",").append(getFormat(m.getStime())).append("\n");
			sb.append("执行结束时间:").append(",").append(getFormat(m.getEtime())).append("\n");
			sb.append("执行用时:").append(",").append((float) Long.parseLong(m.getRtime()) / 60 / 1000).append("（分）")
					.append("\n");
			sb.append("\n");
			sb.append("序号,表名,开始时间,结束时间,执行时长（秒）,查询记录数,导入记录数,执行结果,数据顺差\n");

			List<Monitor> mds = monitorService.selectByTaskName(m.getTaskname());
			for (int i = 0; i < mds.size(); i++) {
				Monitor md = mds.get(i);
				sb.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n", i, md.getTablename(), getFormat2(md.getStime()),
						getFormat2(md.getEtime()),
						Utils.isEmpty(md.getRtime()) ? "0" : Long.parseLong(md.getRtime()) / 1000, md.getQcount(),
						md.getEcount(), md.getResult(), md.getDiff()));
			}
		});

		ByteArrayResource arrayResource = new ByteArrayResource(sb.toString().getBytes("GBK"));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s.csv\"", tn));
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(arrayResource.contentLength())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new InputStreamResource(arrayResource.getInputStream()));
	}

	@RequestMapping(value = "selectSyslog", method = RequestMethod.POST)
	public BaseResponse selectSyslog(@RequestBody SelectPageMonitorsParams ps) {
		if (null == ps || Utils.isEmpty(ps.getTableName()) || Utils.isEmpty(ps.getTaskName())) {
			return BaseResponseFactory.get500();
		}
		HashMap<String, Object> params = new HashMap<>();
		params.put("taskName", ps.getTaskName());
		params.put("tableName", ps.getTableName());

		StringBuffer sb = new StringBuffer();
		List<String> is = syslogService.selectByParams(params);
		if (null != is && is.size() > 0) {
			is.forEach(i -> {
				sb.append(i).append("\n").append("\n").append("\n").append("\n");
			});
		}
		return BaseResponseFactory.get200().setData(sb.toString());
	}

	@RequestMapping(value = "selectMonitorDetail", method = RequestMethod.POST)
	public BaseResponse selectMonitorDetail(@RequestBody SelectPageMonitorsParams params) {
		if (null == params || Utils.isEmpty(params.getTaskName())) {
			return BaseResponseFactory.get500();
		}
		List<Monitor> ms = monitorService.selectByTaskName(params.getTaskName());
		ms.forEach(m -> {
			m.setStime(getFormat2(m.getStime()));
			m.setEtime(getFormat2(m.getEtime()));
			m.setRtime(String.valueOf((float) Long.parseLong(m.getRtime()) / 1000));
		});

		return BaseResponseFactory.get200().setData(ms);
	}

	@RequestMapping(value = "selectMonitors", method = RequestMethod.POST)
	public BaseResponse selectMonitors(@RequestBody SelectPageMonitorsParams params) {
		HashMap<String, Object> ps = new HashMap<>();
		if (!Utils.isEmpty(params.getTaskName())) {
			ps.put("taskName", params.getTaskName());
		}
		if (params.getResult() == 1) {
			ps.put("result", 0);
		}
		if (params.getResult() == 2) {
			ps.put("result", 1);
		}
		if (params.getResult() == 3) {
			ps.put("result", 2);
		}
		if (null == params.getPage()) {
			params.setPage(2);
		}
		if (null == params.getSize()) {
			params.setSize(10);
		}
		Page<Monitor> ms = monitorService.selectMonitorInfos(ps, params.getPage(), params.getSize());
		ms.forEach(m -> {
			m.setStime(getFormat(m.getStime()));
			m.setEtime(getFormat(m.getEtime()));
		});
		return BaseResponseFactory.get200().setData(new PageInfo<>(ms));

		// if (null == params || Utils.isEmpty(params.getTaskName())) {
		// mGroupBys = monitorService.selectMonitors(null, params.getPage(),
		// params.getSize());
		// } else {
		// HashMap<String, Object> ps = new HashMap<>();
		// if (!Utils.isEmpty(params.getTaskName())) {
		// ps.put("taskName", params.getTaskName());
		// }
		// mGroupBys = monitorService.selectMonitors(ps, params.getPage(),
		// params.getSize());
		// }
		// int result = params.getResult();
		// mGroupBys.forEach(m -> {
		// m.setStime(getFormat(m.getStime()));
		// m.setEtime(getFormat(m.getEtime()));
		// m.setRtime(String.valueOf((float) Long.parseLong(m.getRtime()) / 1000));
		//
		// HashMap<String, Object> diffs = monitorService.selectByDiff(m.getTaskname());
		// if (diffs.containsKey("positive")) {
		// m.setPositive(Long.parseLong(String.valueOf(diffs.get("positive"))));
		// }
		// if (diffs.containsKey("negative")) {
		// m.setNegative(Long.parseLong(String.valueOf(diffs.get("negative"))));
		// }
		//
		// long count = monitorService.countMonitors(m.getTaskname());
		// List<Monitor> faileds = monitorService.selectFaileds(m.getTaskname());
		//
		// if (null == faileds || faileds.size() <= 0) {
		// m.setResult("1");
		// } else if (count > faileds.size()) {
		// m.setResult("2");
		// m.setFaileds(faileds.size());
		// } else {
		// m.setResult("0");
		// m.setFaileds(faileds.size());
		// }
		// });
		//
		// List<Monitor> rs = new ArrayList<>();
		// if (0 == result) {
		// rs = mGroupBys;
		// } else if (1 == result) {
		// for (Monitor m : mGroupBys) {
		// if (m.getResult().equals("1")) {
		// rs.add(m);
		// }
		// }
		// } else if (2 == result) {
		// for (Monitor m : mGroupBys) {
		// if (m.getResult().equals("2")) {
		// rs.add(m);
		// }
		// }
		// } else {
		// for (Monitor m : mGroupBys) {
		// if (m.getResult().equals("0")) {
		// rs.add(m);
		// }
		// }
		// }
		//
		// return BaseResponseFactory.get200().setData(rs);
	}

}
