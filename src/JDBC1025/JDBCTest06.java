package JDBC1025;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
实现功能：
    1、需求：
        模拟用户登录功能的实现。
    2、业务描述：
        程序运行的时候，提供一个输入的入口，可以让用户输入用户名和密码
        用户输入用户名和密码之后，提交信息，java程序收集到用户信息
        Java程序连接数据库验证用户名和密码是否合法
        合法：显示登陆成功
        不合法：显示登陆失败
    3、数据的准备：
        在实际开发中，表的设计会使用专业的建模工具，我们这里安装一个建模工具：PowerDesigner
        使用PD工具来进行数据库表的设计。
    4、当前程序存在的问题：
        用户名：fdsa
        密码：fdsa' or '1'='1
        登录成功
        这种现象被称为SQL注入（安全隐患）。（黑客经常使用）
    5、导致SQL注入的根本原因是什么？
        用户输入的信息中心含有sql语句的关键字，并且这些关键字参与sql语句的编译过程，
        导致sql语句的原意被扭曲，进而达到sql注入。
 */
public class JDBCTest06 {
    public static void main(String[] args) {
        //初始化一个界面
        Map<String,String> userLoginInfo = initUI();
        //验证用户名和密码
        boolean loginSuccess = login(userLoginInfo);
        //最后输出结果
        System.out.println(loginSuccess?"登录成功":"登陆失败");
    }

    private static boolean login(Map<String, String> userLoginInfo) {
        //打标记
        boolean  loginSuccess = false;
        //JDBC代码
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode?serverTimezone=UTC&useSSL=false","root","root");
            stmt = conn.createStatement();
            String sql = "select * from t_user where loginName = '"+userLoginInfo.get("loginName")+"' and loginPwd = '"+userLoginInfo.get("loginPwd")+"'";
            rs = stmt.executeQuery(sql);
            if (rs.next()){
                //登陆成功
                loginSuccess =true;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return loginSuccess ;
    }

    /**
     * 初始化登录页面
     * @return 用户输入的用户名和密码等登录信息
     */
    private static Map<String,String> initUI() {
        Scanner s = new Scanner(System.in);
        System.out.println("用户名： ");
        String loginName = s.nextLine();

        System.out.println("密码： ");
        String loginpwd = s.nextLine();

        Map<String,String> userLoginInfo = new HashMap<>();
        userLoginInfo.put("loginName",loginName);
        userLoginInfo.put("loginPwd",loginpwd);

        return userLoginInfo;
    }
}










