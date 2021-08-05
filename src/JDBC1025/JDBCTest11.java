package JDBC1025;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
    sql脚本：
        droptable if exists t_act;
        create table t_act(
            actno int,
            balance double(7,2) //注意： 7表示有效数字的个数，2表示小数位的个数。
        );
        insert into t_act(actno.balance) values(111,20000);
        insert into t_act(actno,balance) values(222,0);
        commit;
        select * from t_act;

        重点三行代码？
            conn.setAutoCommit(false);
            conn.commit();
            conn.rollback();
 */
public class JDBCTest11 {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode?serverTimezone=UTC&useSSL=false","root","root");
            //将自动提交机制修改为手动提交
            conn.setAutoCommit(false);//开启事务

            String sql = "update t_act set balance=? where actno=?";
            ps=conn.prepareStatement(sql);
            ps.setDouble(1,10000);
            ps.setInt(2,111);
            int count = ps.executeUpdate();

            String s = null;
            s.toString();

            ps=conn.prepareStatement(sql);
            ps.setDouble(1,10000);
            ps.setInt(2,222);
            ps.executeUpdate();
            count += ps.executeUpdate();
            System.out.println(count==2?"转账成功":"转账失败");

            //程序能够走到这里说明以上程序没有异常，事务结束，手动提交
            conn.commit();//提交事务

        } catch (ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();//回滚事务
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
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
    }
}
