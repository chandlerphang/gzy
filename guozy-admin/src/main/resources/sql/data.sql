INSERT INTO `admin_user`(`id`, `account`, `password`, `name`, `email`) VALUES(1, '123456789@qq.com', '$2a$10$5ZGa6XShNg5zsIVHgAX/Defno1lH3Am0RPR//vQv.O4RxtkMyPslW', '果大大', '123456789@qq.com');
INSERT INTO `admin_user`(`id`, `account`, `password`, `name`, `email`) VALUES(2, '987654321@qq.com', '$2a$10$5ZGa6XShNg5zsIVHgAX/Defno1lH3Am0RPR//vQv.O4RxtkMyPslW', '果小小', '987654321@qq.com');

INSERT INTO `admin_role`(`id`, `name`, `description`) VALUES(1, 'ROLE_SUPER', '最高权限');
INSERT INTO `admin_role`(`id`, `name`, `description`) VALUES(2, 'ROLE_SHOP', '店铺管理员');

INSERT INTO `admin_user_role_xref`(`user_id`, `role_id`) VALUES(1, 1);
INSERT INTO `admin_user_role_xref`(`user_id`, `role_id`) VALUES(2, 1);

INSERT INTO `admin_func` VALUES (1,'yonghuguanli','用户管理','/user','fa-user',NULL,0);
INSERT INTO `admin_func` VALUES (2,'dianpuguanli','店铺管理','/shop','fa-institution',NULL,1);
INSERT INTO `admin_func` VALUES (3,'dianyuanguanli','店员管理','/saler','fa-group',NULL,2);
INSERT INTO `admin_func` VALUES (4,'leimuguanli','类目管理','/category','fa-list',NULL,3);
INSERT INTO `admin_func` VALUES (5,'shangpinguanli','商品管理','/goods','fa-apple',NULL,4);
INSERT INTO `admin_func` VALUES (6,'dingdanguanli','订单管理','/user','fa-file',NULL,5);
INSERT INTO `admin_func` VALUES (7,'appsettings','App 设置','/appsettings','fa-cog',NULL,5);
INSERT INTO `admin_func` VALUES (9,'tuisong','App 通知','/appnotification','fa-bell',NULL,8);

INSERT INTO `permission`(`id`, `name`, `type`, `is_composite`) VALUES(1, 'PERMISSION_OTHER_DEFAULT', 'OTHER', b'0');

INSERT INTO `admin_func_perm_xref`(`admin_func_id`, `perm_id`) VALUES(1, 1);
INSERT INTO `admin_func_perm_xref`(`admin_func_id`, `perm_id`) VALUES(2, 1);
INSERT INTO `admin_func_perm_xref`(`admin_func_id`, `perm_id`) VALUES(3, 1);
INSERT INTO `admin_func_perm_xref`(`admin_func_id`, `perm_id`) VALUES(4, 1);
INSERT INTO `admin_func_perm_xref`(`admin_func_id`, `perm_id`) VALUES(5, 1);
INSERT INTO `admin_func_perm_xref`(`admin_func_id`, `perm_id`) VALUES(6, 1);
INSERT INTO `admin_func_perm_xref`(`admin_func_id`, `perm_id`) VALUES(7, 1);
INSERT INTO `admin_func_perm_xref`(`admin_func_id`, `perm_id`) VALUES(9, 1);
