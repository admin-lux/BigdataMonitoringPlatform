# BigdataMonitoringPlatform
大数据运行监控工具；配置文件管理工具；数据源管理工具；


### sql 建表语句
users

``` sql
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `account` varchar(100) NOT NULL COMMENT '账号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `status` varchar(2) NOT NULL COMMENT '状态（0：删除；1：正常）',
  `admin` varchar(2) NOT NULL COMMENT '管理员（1：是；0：否）',
  `lastdate` datetime DEFAULT NULL COMMENT '最近一次登陆',
  `crdate` datetime NOT NULL COMMENT '创建时间',
  `update` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
```


login_log

``` sql
CREATE TABLE `login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `account` varchar(100) DEFAULT NULL COMMENT '账号',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `logindate` datetime NOT NULL COMMENT '登陆时间',
  `ip` varchar(20) NOT NULL COMMENT 'ip地址',
  `state` int(2) NOT NULL COMMENT '状态（0：登陆失败；1：登陆成功）',
  `desc` varchar(100) DEFAULT NULL COMMENT '描述',
  `crdate` datetime NOT NULL COMMENT '创建时间',
  `update` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8;
```

tasks

``` sql
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(100) NOT NULL COMMENT '任务名称',
  `conf_name` varchar(100) NOT NULL COMMENT '配置文件名称',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `state` int(2) NOT NULL DEFAULT '1' COMMENT '任务状态',
  `rundate` varchar(50) DEFAULT NULL COMMENT '运行时间',
  `desc` varchar(200) NOT NULL COMMENT '说明',
  `cr_date` datetime NOT NULL COMMENT '创建时间',
  `up_date` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
```

opes

```sql
CREATE TABLE `opes` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `opdate` datetime NOT NULL COMMENT '操作时间',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `ip` varchar(20) NOT NULL COMMENT 'ipaddr',
  `fun_name` varchar(100) NOT NULL COMMENT '功能模块名称',
  `state` int(2) NOT NULL COMMENT '操作类型（0：删除；1：新增；2：修改）',
  `desc` varchar(200) NOT NULL COMMENT '日志描述',
  `cr_date` datetime NOT NULL,
  `up_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8;
```

datasources

``` sql
CREATE TABLE `datasources` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sys_name` varchar(100) NOT NULL COMMENT '源系统名',
  `database_name` varchar(100) DEFAULT NULL COMMENT '数据库名',
  `source_type` int(2) NOT NULL COMMENT '数据源类型（1：Oracle；2：MYSQL）',
  `user_name` varchar(100) NOT NULL COMMENT '用户名',
  `pwd` varchar(100) NOT NULL COMMENT '密码',
  `connect` text NOT NULL COMMENT '数据库连接信息',
  `user_id` int(11) NOT NULL COMMENT '最近更新人',
  `lastdate` datetime NOT NULL COMMENT '最近更新时间',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '状态（0：删除；1：正常）',
  `cr_date` datetime NOT NULL,
  `up_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_sysname` (`sys_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
```


imports

``` sql
CREATE TABLE `imports` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `datasource_id` int(11) NOT NULL COMMENT '数据源表id',
  `task_id` int(11) NOT NULL COMMENT '任务表id',
  `table_name` varchar(255) NOT NULL COMMENT '表名',
  `hbase_name` varchar(255) NOT NULL COMMENT 'hbase表名',
  `columns` varchar(2040) DEFAULT NULL COMMENT '需要的字段，空表示全部字段',
  `num_mappers` int(11) NOT NULL DEFAULT '1' COMMENT 'map数量',
  `column_family` varchar(255) NOT NULL DEFAULT 'info' COMMENT '列族',
  `row_key` varchar(255) NOT NULL COMMENT 'hbase主键',
  `split_by` varchar(255) NOT NULL COMMENT '指定分片字段',
  `add_row_key` int(11) NOT NULL DEFAULT '0' COMMENT '是否将row_key在hbase中添加为一列(0:不添加;1:添加)',
  `before_import` varchar(255) DEFAULT NULL COMMENT '导数前置执行命令',
  `import_type` int(2) NOT NULL DEFAULT '0' COMMENT '接入类型(0:全量;1:增量)',
  `increment_if` varchar(2040) DEFAULT NULL COMMENT '增量条件',
  `state` int(11) NOT NULL DEFAULT '1' COMMENT '状态（0：删除；1：正常）',
  `last_user_id` int(11) NOT NULL COMMENT '最近更新人',
  `lastdate` datetime NOT NULL COMMENT '最近更新时间',
  `cr_date` datetime NOT NULL,
  `up_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_taskid_tablename_datasourceid` (`task_id`,`table_name`,`datasource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=953 DEFAULT CHARSET=utf8;
```


yamls

``` sql
CREATE TABLE `yamls` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `data_yaml` blob NOT NULL COMMENT '配置文件',
  `file_name` varchar(100) NOT NULL COMMENT '文件名',
  `file_path` varchar(255) NOT NULL COMMENT '文件发布路径',
  `user_id` int(11) NOT NULL COMMENT '生成者',
  `cr_date` datetime NOT NULL,
  `up_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
```


export_info

``` sql
CREATE TABLE `export_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `taskname` varchar(255) DEFAULT NULL COMMENT '任务名',
  `tablename` varchar(255) DEFAULT NULL COMMENT '表名',
  `stime` varchar(255) DEFAULT NULL COMMENT '开始时间',
  `etime` varchar(255) DEFAULT NULL COMMENT '结束时间',
  `rtime` varchar(255) DEFAULT NULL COMMENT '运行时长',
  `qcount` int(11) DEFAULT NULL COMMENT '查询数量',
  `ecount` int(11) DEFAULT NULL COMMENT '导出数量',
  `result` varchar(255) DEFAULT NULL COMMENT '完成状态（success，failed）',
  `diff` int(11) DEFAULT NULL COMMENT '数据差',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `taskname_tablename` (`taskname`,`tablename`),
  KEY `taskname` (`taskname`),
  KEY `stime` (`stime`),
  KEY `etime` (`etime`),
  KEY `rtime` (`rtime`)
) ENGINE=InnoDB AUTO_INCREMENT=4576 DEFAULT CHARSET=utf8;

```


export_sys_log

``` sql
CREATE TABLE `export_sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskname` varchar(255) DEFAULT NULL,
  `tablename` varchar(255) DEFAULT NULL,
  `info` longtext,
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_taskname_tablename` (`taskname`,`tablename`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17041 DEFAULT CHARSET=utf8;
```


token_verify

```
CREATE TABLE `token_verify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `token` text NOT NULL,
  `state` int(11) NOT NULL,
  `lastdate` datetime NOT NULL,
  `cr_date` datetime NOT NULL,
  `up_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
```