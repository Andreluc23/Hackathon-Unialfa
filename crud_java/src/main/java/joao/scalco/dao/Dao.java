package joao.scalco.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dao {

    private Connection connection;

    public Dao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/eventosunialfafinal?useTimezone=true&serverTimezone=UTC",
                    "root",
                    "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Connection getConnection() {
        return connection;
    }
}
