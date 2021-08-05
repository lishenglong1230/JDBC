package JDBC1025;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/*
    解决sql注入问题？
        只要用户提供的信息不参与SQL语句的编译过程，问题就解决了。
        即使用户提供的信息中含有SQL语句的关键字，但是没有参与编译，不起作用
        要想用户信息不参与SQL语句的编译，那么必须使用java.sql.PrepareStatement
        PrepareStatement接口继承了java.sql.Statement
        PrepareStatement是属于预编译的数据库操作对象
        PrepareStatement的原理是：预先对SQL语句的框架进行编译，然后再给SQL语句传”值“。
    解决SQL注入的关键是什么？
        用户提供的信息中即使含有sql语句的关键字，但是这些关键字并没有参与编译，不起作用。
    对比一下Statement和PrepareStatement
        - Statement存在sql注入问题，PreparedStatement解决了SQL注入问题。
        - Statement是编译一次执行一次。PreparedStatement是编译一次，可执行N此。PreparedStatement效率较高一些。
        - PreparedStatement会在编译阶段做类似的安全检查。

        综上所述：PreparedStatement使用较多。只有极少数的情况下需要使用Statement
    什么情况下必须使用Statement呢?
        业务方面要求必须支持SQL注入的时候。
        Statement支持SQL注入，凡是业务方面要求是需要进行sql语句拼接的，必须使用Statement。
 */


public class JDBCTest07 {
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
        //单独定义变量
        String loginName = userLoginInfo.get("loginName");
        String loginPwd = userLoginInfo.get("loginPwd");
        //JDBC代码
        Connection conn = null;
        PreparedStatement ps = null;//这里使用PreparedStatement（预编译的数据库操作对象）
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode?serverTimezone=UTC&useSSL=false","root","root");
            //3、获取预编译的数据库操作对象
            //SQL语句的框子。其中一个？，表个一个占位符，一个?将来接受一个"值"，注意：占位符不能使用单引号括起来。
            String sql = "select * from t_user where loginName = ? and loginPwd = ?";
            //程序执行到此处，会发送sql语句框子给DBMS，然后DBMS进行sql语句的预先编译。
            ps = conn.prepareStatement(sql);
            //给占位符？传值（第1个问号下标是1，第2个问号下标是2，JDBC中所有下标从1开始。）
            ps.setString(1,loginName);
            ps.setString(2,loginPwd);
            //4、执行sql
            rs = ps.executeQuery();
            //5、处理结果集
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
            if (ps != null) {
                try {
                    ps.close();
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
        String loginPwd = s.nextLine();

        Map<String,String> userLoginInfo = new HashMap<>();
        userLoginInfo.put("loginName",loginName);
        userLoginInfo.put("loginPwd",loginPwd);

        return userLoginInfo;
    }
}
