-- 用户表
CREATE TABLE `cms_user` (
    `id` varchar(32) NOT NULL COMMENT '用户ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
    `role` varchar(20) NOT NULL COMMENT '角色',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `status` varchar(20) NOT NULL COMMENT '状态',
    `login_attempts` int DEFAULT '0' COMMENT '登录尝试次数',
    `last_login_attempt_time` datetime DEFAULT NULL COMMENT '最后登录尝试时间',
    `reset_code` varchar(100) DEFAULT NULL COMMENT '重置密码验证码',
    `reset_code_expire_time` datetime DEFAULT NULL COMMENT '重置密码验证码过期时间',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 文章表
CREATE TABLE `cms_article` (
    `id` varchar(32) NOT NULL COMMENT '文章ID',
    `title` varchar(200) NOT NULL COMMENT '标题',
    `content` text NOT NULL COMMENT '内容',
    `summary` varchar(500) DEFAULT NULL COMMENT '摘要',
    `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图',
    `source` varchar(100) DEFAULT NULL COMMENT '来源',
    `source_url` varchar(255) DEFAULT NULL COMMENT '来源URL',
    `keywords` varchar(255) DEFAULT NULL COMMENT '关键词',
    `category_id` varchar(32) DEFAULT NULL COMMENT '栏目ID',
    `author` varchar(50) DEFAULT NULL COMMENT '作者',
    `view_count` bigint DEFAULT '0' COMMENT '浏览次数',
    `like_count` bigint DEFAULT '0' COMMENT '点赞数',
    `is_top` tinyint(1) DEFAULT '0' COMMENT '是否置顶',
    `is_recommend` tinyint(1) DEFAULT '0' COMMENT '是否推荐',
    `status` varchar(20) NOT NULL COMMENT '状态',
    `creator_id` varchar(32) NOT NULL COMMENT '创建者ID',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- 栏目表
CREATE TABLE `cms_category` (
    `id` varchar(32) NOT NULL COMMENT '栏目ID',
    `name` varchar(50) NOT NULL COMMENT '栏目名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `parent_id` varchar(32) DEFAULT NULL COMMENT '父级ID',
    `sort` int DEFAULT '0' COMMENT '排序',
    `path` varchar(255) DEFAULT NULL COMMENT '路径',
    `level` int DEFAULT '1' COMMENT '层级',
    `is_show` tinyint(1) DEFAULT '1' COMMENT '是否显示',
    `icon` varchar(255) DEFAULT NULL COMMENT '图标',
    `template` varchar(100) DEFAULT NULL COMMENT '模板',
    `article_template` varchar(100) DEFAULT NULL COMMENT '文章模板',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='栏目表';

-- 文件表
CREATE TABLE `cms_file` (
    `id` varchar(32) NOT NULL COMMENT '文件ID',
    `name` varchar(100) NOT NULL COMMENT '文件名',
    `original_name` varchar(100) NOT NULL COMMENT '原始文件名',
    `path` varchar(255) NOT NULL COMMENT '文件路径',
    `store_path` varchar(255) NOT NULL COMMENT '存储路径',
    `access_url` varchar(255) DEFAULT NULL COMMENT '访问URL',
    `type` varchar(50) NOT NULL COMMENT '文件类型',
    `size` bigint NOT NULL COMMENT '文件大小',
    `mime_type` varchar(100) NOT NULL COMMENT 'MIME类型',
    `creator_id` varchar(32) NOT NULL COMMENT '创建者ID',
    `uploader_ip` varchar(50) DEFAULT NULL COMMENT '上传者IP',
    `download_count` int DEFAULT '0' COMMENT '下载次数',
    `last_download_time` datetime DEFAULT NULL COMMENT '最后下载时间',
    `md5` varchar(32) NOT NULL COMMENT '文件MD5',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

-- 模板表
CREATE TABLE `cms_template` (
    `id` varchar(32) NOT NULL COMMENT '模板ID',
    `name` varchar(100) NOT NULL COMMENT '模板名称',
    `code` varchar(50) NOT NULL COMMENT '模板代码',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图',
    `type` varchar(50) NOT NULL COMMENT '模板类型',
    `category` varchar(50) DEFAULT NULL COMMENT '模板分类',
    `structure` text DEFAULT NULL COMMENT '模板结构',
    `main_file` varchar(100) DEFAULT NULL COMMENT '主文件',
    `data_schema` text DEFAULT NULL COMMENT '数据结构',
    `default_data` text DEFAULT NULL COMMENT '默认数据',
    `seo_config` text DEFAULT NULL COMMENT 'SEO配置',
    `version` varchar(20) DEFAULT NULL COMMENT '版本',
    `author` varchar(50) DEFAULT NULL COMMENT '作者',
    `status` varchar(20) NOT NULL COMMENT '状态',
    `creator_id` varchar(32) NOT NULL COMMENT '创建者ID',
    `hits` int DEFAULT '0' COMMENT '点击次数',
    `last_used_time` datetime DEFAULT NULL COMMENT '最后使用时间',
    `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统模板',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板表';

-- 模板权限表
CREATE TABLE `cms_template_permission` (
    `id` varchar(32) NOT NULL COMMENT '权限ID',
    `template_id` varchar(32) NOT NULL COMMENT '模板ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `permission` varchar(20) NOT NULL COMMENT '权限类型',
    `granted_by` varchar(32) NOT NULL COMMENT '授权人ID',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板权限表';

-- 模板版本表
CREATE TABLE `cms_template_version` (
    `id` varchar(32) NOT NULL COMMENT '版本ID',
    `template_id` varchar(32) NOT NULL COMMENT '模板ID',
    `version` varchar(20) NOT NULL COMMENT '版本号',
    `content` text NOT NULL COMMENT '版本内容',
    `description` varchar(500) DEFAULT NULL COMMENT '版本描述',
    `changelog` text DEFAULT NULL COMMENT '变更日志',
    `compatibility` varchar(500) DEFAULT NULL COMMENT '兼容性说明',
    `release_notes` text DEFAULT NULL COMMENT '发布说明',
    `status` varchar(20) NOT NULL COMMENT '状态',
    `creator_id` varchar(32) NOT NULL COMMENT '创建者ID',
    `publisher_id` varchar(32) DEFAULT NULL COMMENT '发布者ID',
    `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板基本表';

-- 模板文件表
CREATE TABLE `cms_template_file` (
    `id` varchar(32) NOT NULL COMMENT '文件ID',
    `template_id` varchar(32) NOT NULL COMMENT '模板ID',
    `path` varchar(255) NOT NULL COMMENT '文件路径',
    `type` varchar(50) NOT NULL COMMENT '文件类型',
    `content` text DEFAULT NULL COMMENT '文件内容',
    `mime_type` varchar(100) DEFAULT NULL COMMENT 'MIME类型',
    `encoding` varchar(20) DEFAULT NULL COMMENT '编码',
    `version` varchar(20) DEFAULT NULL COMMENT '版本',
    `size` bigint NOT NULL COMMENT '文件大小',
    `hash` varchar(64) NOT NULL COMMENT '文件哈希',
    `creator_id` varchar(32) NOT NULL COMMENT '创建者ID',
    `last_modified_by` varchar(32) DEFAULT NULL COMMENT '最后修改者ID',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板文件表';