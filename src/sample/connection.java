package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class connection {

    public Connection con;
    public Statement st;
    public ResultSet rs;
    public Connection connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/mynewdb?autoreconnect=true&useSSL=false&useLegacyDatetimeCode=false&&serverTimezone=UTC","root","password");
            return con;
        }catch(Exception ex){
            System.out.println(ex);
        }
        return null;
    }

}