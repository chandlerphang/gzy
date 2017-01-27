<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
	<classPathEntry location="__classPathEntry__" />

	<context id="mysqlTables" targetRuntime="MyBatis3">
		<jdbcConnection 
			driverClass="__jdbcDriverClass__"
			connectionURL="__jdbcUrl__"
		    userId="__jdbcUser__"
			password="__jdbcPassword__" />

		<javaTypeResolver>
			<property name="forceBigDecimals" value="false" />
		</javaTypeResolver>

		<javaModelGenerator targetPackage="com.cactus.guozy.domain"
			targetProject="src\main\java">
			<property name="enableSubPackages" value="true" />
			<property name="trimStrings" value="true" />
		</javaModelGenerator>

		<sqlMapGenerator targetPackage="com.cactus.guozy.dao"
			targetProject="src\main\java">
			<property name="enableSubPackages" value="true" />
		</sqlMapGenerator>

		<javaClientGenerator targetPackage="com.cactus.guozy.dao"
			targetProject="src\main\java" type="XMLMAPPER">
			<property name="enableSubPackages" value="true" />
		</javaClientGenerator>

		<table tableName="admin_user" domainObjectName="AdminUser"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		<table tableName="admin_role" domainObjectName="AdminRole"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		<table tableName="admin_user_role_xref" domainObjectName="AdminUserRole"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		<table tableName="permission" domainObjectName="Permission"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		<table tableName="admin_fun" domainObjectName="AdminFunction"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		<table tableName="user" domainObjectName="User"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		<table tableName="shop" domainObjectName="Shop"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		<table tableName="category" domainObjectName="Category"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		<table tableName="goods" domainObjectName="Goods"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
		</table>
		
		
	</context>
</generatorConfiguration>
