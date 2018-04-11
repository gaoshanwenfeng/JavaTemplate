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
import com.pek.codegenerator.pojo.pojo.Table;
import com.pek.codegenerator.util.CamelCaseUtil;



public class IMybatisGenerator {
    //变化config
    private static String tableName = "hotel_info";//表名
    private static String pkFieldName = "id";//主键名，通常为ID    
    private static String tableDesc = "价格计划";//表描述信息
    private static String mainPath = "G:\\CodegeneratorWork";//生成代码目录

    private static String pkg = "com.tims.hotel.domain";//项目包名
    private static String domain = "rp";//模块目录    
    //项目名称
    private static String projectName = "航旅云";
    private static String desc = "功能描述:" + tableDesc;
    private static String domainPkg = pkg ;
    private static String curDate = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
    private static String srcPath = mainPath + "/src";
    private static String domainPath = domainPkg.replace(".", "/");
    private static String path = null;
    private static String project = "项目名称:" + projectName + "系统";
    private static String history = "历史版本:\n *\t\t\t\t\t" + curDate + " v1.0.0 (hna) 创建";
    private static String get = "获取";
    private static String set = "设置";
    private static String insert = "【新增】（系统生成方法）";
    private static String update = "【修改】（系统生成方法）";
    private static String delete = "【删除】（系统生成方法）";
    private static String findByWhere = "【根据条件查询】（系统生成方法）";
    private static String findById = "【根据ID查询】（系统生成方法）";
    private static String checkUniqueness = "【校验唯一性】（系统生成方法）通常在insert,update前使用";
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
        Table table =  MetaDataLoader.loadMetaData(tableName, pkFieldName);
        		
        context.put("pkg", pkg);
        context.put("domainPkg", domainPkg);
        context.put("domainPath", domainPath);
        context.put("domain", domain);
        context.put("data", table);
        context.put("project", project);
        context.put("history", history);
        context.put("desc", desc);
        context.put("get", get);
        context.put("set", set);
        context.put("insert", insert);
        context.put("update", update);
        context.put("delete", delete);
        context.put("findByWhere", findByWhere);
        context.put("findById", findById);
        context.put("checkUniqueness", checkUniqueness);
        context.put("sqlMapName", tableName.toLowerCase());
        path = srcPath + "/" + domainPath;
        context.put("path", path);
        String className = CamelCaseUtil.getFirstUpperCamelCaseName(tableName);
        String porpName = className + "Mapper";

        outputCode(context, "vm/mybatis/service.vm", path + "/service/", "I" + className + "Service.java");
        outputCode(context, "vm/mybatis/mybatis/serviceImpl.vm", path + "/service/impl/", className + "ServiceImpl.java");

        outputCode(context, "vm/mybatis/dao.vm", path + "/dao/", "I" + className + "Dao.java");
        outputCode(context, "vm/mybatis/daoImpl.vm", path + "/dao/impl/", className + "DaoImpl.java");
        outputCode(context, "vm/mybatis/sqlMap.vm", path + "/dao/impl/", porpName + ".xml");

        outputCode(context, "vm/mybatis/domain.vm", path + "/domain/", className + ".java");
        outputCode(context, "vm/mybatis/spring-config.vm", mainPath + "/springconf/", "applicationContext_" + porpName + ".xml");



    }
}
