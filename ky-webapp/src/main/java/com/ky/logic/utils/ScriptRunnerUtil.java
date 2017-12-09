package com.ky.logic.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by yl on 2017/8/6.
 */
public enum ScriptRunnerUtil {

    INSTANCE;

    public Connection getConn() throws Exception {
        String url = null;
        String driver = null;
        String username = null;
        String password = null;
        try {
            Properties props = Resources.getResourceAsProperties("persistence-mysql.properties");
            url = props.getProperty("hibernate.connection.url");
            driver = props.getProperty("hibernate.connection.driverClass");
            username = props.getProperty("hibernate.connection.username");
            password = props.getProperty("hibernate.connection.password");
        } catch (IOException e) {
            throw new Exception("读取数据库配置文件异常");
        }

        Connection conn = null;
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new Exception("建立数据库链接异常");
        }

        return conn;
    }

    public ScriptRunner getRunner(Connection conn) {
        ScriptRunner runner = new ScriptRunner(conn);

        runner.setLogWriter(null);   //设置是否输出日志 null 不输出

        // 注释掉 则日志打印到控制台
        //runner.setErrorLogWriter(new PrintWriter(WebPathUtil.getClassRootPath()+ "sqlErr.log"));

        return runner;
    }

    public void executeSql(File[] sqlFiles) {
        try {

            Connection conn = getConn();
            ScriptRunner runner = getRunner(conn);

            for (File f : sqlFiles) {
                try {
                    if (f.getName().contains("sql")) {
                        runner.runScript(new InputStreamReader(new FileInputStream(f), "UTF-8"));//一个是全路径
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            runner.closeConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void executeSql(String sqlPath) {
        try {
            Connection conn = getConn();
            ScriptRunner runner = getRunner(conn);

            Resources.setCharset(Charset.forName("UTF-8")); //设置字符集,不然中文乱码插入错误
            runner.runScript(Resources.getResourceAsReader(sqlPath)); //一个是src下的路径

            runner.closeConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*public static void main(String[] args) {
        try {
            System.out.println(WebPathUtil.getRootPath());
            System.out.println(WebPathUtil.getClassRootPath());
            File file = new File(WebPathUtil.getClassRootPath() + "sql");

            if (file.isDirectory()) {
                File[] sqlFiles = file.listFiles();
                if (sqlFiles.length < 0) {
                    return;
                }
                ScriptRunnerUtil.INSTANCE.executeSql(sqlFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/
}
