package com.pek.codegenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.pek.codegenerator.jdbc.MetaDataLoader;
import com.pek.codegenerator.util.CamelCaseUtil;



public class IbatisGenerator {
    // 变化配置
    private static String mainPath = "D:\\Work\\genproject";//生成代码目录
    // 表配置
    private static String tableName = "hotel_info";//表名
    private static String pkFieldName = "id";//主键名，通常为ID    
    private static String tableDesc = "酒店信息表";//表描述信息
    // 项目配置
    private static String frameworkPkg = "com.pek";//公司框架包名
    private static String domain = "com.pek.hotel";//模块目录
    private static String projectName = "酒店系统";//项目名称
    
    // 固定配置
    private static String projectPkg = "com.pek.hotel";//项目包名
    
    private static String desc = "功能描述:" + tableDesc;
    private static String domainPkg = projectPkg + "." + domain;
    private static String curDate = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
    private static String srcPath = mainPath + "/src";
    private static String domainPath = domainPkg.replace(".", "/");
    private static String path = null;
    private static String project = "项目名称:" + projectName;
    private static String createDate = "创建日期:" + curDate;
    private static String get = "获取";
    private static String set = "设置";
    private static String insert = "新增数据";
    private static String update = "修改数据";
    private static String delete = "删除数据";
    private static String findByWhere = "根据条件查询数据";
    private static String findById = "根据主键查询数据";
    private static VelocityContext context = new VelocityContext();

    private static void outputCode(VelocityContext context, String templateFile, String path, String fileName) throws Exception {
        File file = new File(path);
        file.mkdirs();
        Template template = Velocity.getTemplate(templateFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + fileName), "UTF-8"));
        if (template != null) {
            template.merge(context, writer);
        }
        writer.flush();
        writer.close();
        System.out.println("生成[" + path + fileName + "]");
    }

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("input.encoding", "UTF-8");
        props.put("output.encoding", "UTF-8");
        Velocity.init(props);
        context.put("frameworkPkg", frameworkPkg);
        context.put("projectPkg", projectPkg);
        context.put("domainPkg", domainPkg);
        context.put("domainPath", domainPath);
        context.put("domain", domain);
        context.put("table", MetaDataLoader.loadMetaData(tableName, pkFieldName));
        context.put("project", project);
        context.put("createDate", createDate);
        context.put("desc", desc);
        context.put("get", get);
        context.put("set", set);
        context.put("insert", insert);
        context.put("update", update);
        context.put("delete", delete);
        context.put("findByWhere", findByWhere);
        context.put("findById", findById);
        path = srcPath + "/" + domainPath;
        context.put("path", path);
        String className = CamelCaseUtil.getFirstUpperCamelCaseName(tableName);

        System.out.println("开始生成代码[" + mainPath + "]:");

        outputCode(context, "vm/ibatis/action.vm", path + "/web/action/", className + "Action.java");

        outputCode(context, "vm/ibatis/service.vm", path + "/service/", "I" + className + "Service.java");
        outputCode(context, "vm/ibatis/serviceImpl.vm", path + "/service/impl/", className + "ServiceImpl.java");

        outputCode(context, "vm/ibatis/dao.vm", path + "/dao/", "I" + className + "Dao.java");
        outputCode(context, "vm/ibatis/daoImpl.vm", path + "/dao/impl/", className + "DaoImpl.java");
        outputCode(context, "vm/ibatis/sqlMap.vm", path + "/dao/impl/", className + "DaoImpl.xml");

        outputCode(context, "vm/ibatis/domain.vm", path + "/domain/", className + ".java");

        outputCode(context, "vm/ibatis/ibatis-sqlmap.vm", mainPath + "/conf/", "ibatis-sqlmap.xml");
        outputCode(context, "vm/ibatis/applicationContext.vm", mainPath + "/springconf/", "applicationContext_" + className + ".xml");

        System.out.println("完成代码生成。");
    }
}
