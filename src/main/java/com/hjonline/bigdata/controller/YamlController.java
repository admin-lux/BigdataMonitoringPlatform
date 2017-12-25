package com.hjonline.bigdata.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.yaml.snakeyaml.Yaml;

import com.hjonline.bigdata.model.Datasources;
import com.hjonline.bigdata.model.DbBean;
import com.hjonline.bigdata.model.Import;
import com.hjonline.bigdata.model.LinkBean;
import com.hjonline.bigdata.model.SchemaBean;
import com.hjonline.bigdata.model.TableBean;
import com.hjonline.bigdata.model.Tasks;
import com.hjonline.bigdata.model.YamlModel;
import com.hjonline.bigdata.model.request.CrYamlParams;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.DatasourceService;
import com.hjonline.bigdata.service.ImportService;
import com.hjonline.bigdata.service.TaskService;
import com.hjonline.bigdata.service.YamlService;
import com.hjonline.bigdata.utils.Utils;

/**
 * 
 * @author rick_lu
 *
 */
@RestController
@RequestMapping("yaml")
public class YamlController {
	@Autowired
	private ImportService importService;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private YamlService yamlService;

	@RequestMapping(value = "crYaml", method = RequestMethod.POST)
	public BaseResponse crYaml(@RequestBody CrYamlParams ps) {
		if (null == ps || null == ps.getDatas() || ps.getDatas().size() <= 0) {
			return BaseResponseFactory.get500();
		}

		List<Import> ims = new ArrayList<>();
		for (Import im : ps.getDatas()) {
			if (Utils.isEmpty(im.getTableName()) || Utils.isEmpty(im.getHbaseName())) {
				ims.add(importService.selectById(im.getId()));
			} else {
				ims.add(im);
			}
		}
		if (ims.size() <= 0) {
			return BaseResponseFactory.get500();
		}

		if (ps.getType() == 1) {
			int tableNum = ims.size();
			int taskId = ims.get(0).getTaskId();
			for (Import im : ims) {
				if (taskId != im.getTaskId()) {
					return BaseResponseFactory.get500();
				}
			}

			HashMap<String, Object> params = new HashMap<>(10);
			params.put("taskId", taskId);
			if (tableNum != importService.countImports(params)) {
				return BaseResponseFactory.get500();
			}
		}

		List<Datasources> dss = new ArrayList<>();
		List<String> schemas = new ArrayList<>();
		Map<Integer, Object> lastDsIds = new HashMap<>();
		Datasources ds = null;
		for (Import im : ims) {
			if (!lastDsIds.containsKey(im.getDatasourceId())) {
				lastDsIds.put(im.getDatasourceId(), null);
				ds = datasourceService.selectById(im.getDatasourceId());
				dss.add(ds);
				schemas.add(ds.getSysName());
			}
		}

		DbBean dbBean = new DbBean();
		dbBean.setSchemas(schemas);

		List<SchemaBean> sbs = new ArrayList<>();
		SchemaBean sb = null;
		for (Datasources d : dss) {
			sb = new SchemaBean();
			sb.setName(null);
			sb.setSchema(d.getSysName());

			LinkBean lb = new LinkBean();
			lb.setDriver("");
			lb.setPassword(d.getPwd());
			lb.setUrl(d.getConnect());
			lb.setUsername(d.getUserName());
			sb.setLink(lb);

			List<String> tables = new ArrayList<>();
			List<TableBean> tls = new ArrayList<>();
			for (Import im : ims) {
				if (d.getId() == im.getDatasourceId()) {
					tables.add(im.getTableName());
					TableBean tb = new TableBean();
					tb.setAdd_row_key(String.valueOf(im.getAddRowKey()));
					tb.setBefore_import(im.getBeforeImport());
					tb.setColumn_family(im.getColumnFamily());
					tb.setColumns(im.getColumns());
					tb.setHbase_table(im.getHbaseName());
					tb.setInit(null);
					tb.setNum_mappers(im.getNumMappers());
					tb.setRow_key(im.getRowKey());
					tb.setSplit_by(im.getSplitBy());
					tb.setTable(im.getTableName());
					tb.setWhere(im.getIncrementIf());
					tls.add(tb);
				}
			}
			if (tables.size() > 0) {
				sb.setTables(tables);
				sb.setTable_list(tls);

				sbs.add(sb);
			}
		}
		dbBean.setSchemas_list(sbs);
		Yaml yaml = new Yaml();
		String result = yaml.dump(dbBean);

		int taskId = ims.get(0).getTaskId();
		Tasks tasks = taskService.selectById(taskId);
		YamlModel ym = new YamlModel();
		ym.setCrdate(new Date());
		ym.setUpdate(new Date());
		ym.setDataYaml(result.getBytes());
		if (ps.getType() == 1) {
			ym.setFileName(tasks.getConfName());
		} else {
			ym.setFileName(UUID.randomUUID().toString() + ".yml");
		}
		ym.setFilePath(tasks.getFilePath());
		ym.setUserId(0);
		yamlService.crYaml(ym);
		return BaseResponseFactory.get200().setData(ym.getId());
	}

	@RequestMapping(value = "releaseYaml", method = RequestMethod.POST)
	public BaseResponse releaseYaml(@RequestBody HashMap<String, Object> params) throws IOException {
		if (null == params || params.get("id") == null) {
			return BaseResponseFactory.get500();
		}

		YamlModel ym = yamlService.selectById((int) params.get("id"));
		
		File filepath = new File(ym.getFilePath());
		if(!filepath.exists() || !filepath.isDirectory()) {
			filepath.mkdirs();
		}
		String filePath = getConfPath(ym.getFilePath()) + ym.getFileName();

		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String uuid = String.valueOf(UUID.randomUUID().getLeastSignificantBits());
		File file = new File(filePath);

		if (file.exists()) {
			file.renameTo(new File(filePath + "-" + date + uuid));
		}
		try (FileOutputStream fos = new FileOutputStream(filePath);) {
			fos.write(ym.getDataYaml());
		}

		return BaseResponseFactory.get200();
	}

	private String getConfPath(String path) {
		if (Utils.isEmpty(path)) {
			return "";
		} else if (path.endsWith("\\") || path.endsWith("/")) {
			return path;
		} else if (path.contains("\\")) {
			return path + "\\";
		} else if (path.contains("/")) {
			return path + "/";
		} else {
			return path;
		}
	}

	@RequestMapping(value = "downloadYaml/{id}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadYaml(@PathVariable("id") Long id) throws IOException {
		if (null == id || id < 0) {
			return ResponseEntity.status(500).build();
		}

		YamlModel ym = yamlService.selectById(id);
		if (null == ym) {
			return ResponseEntity.status(500).build();
		}

		ByteArrayResource arrayResource = new ByteArrayResource(ym.getDataYaml());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", ym.getFileName()));
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(arrayResource.contentLength())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new InputStreamResource(arrayResource.getInputStream()));
	}

}
