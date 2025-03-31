package com.example.operacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItem {
    Connection connect;
    String  ConnectionResult="";
    Boolean isSucess = false;
    public List<Map<String,String>>getlist()
    {
        List<Map<String,String>> data = null;
        data = new ArrayList<Map<String,String>>();
        try {
            conexao connectionHelper = new conexao();
            connect = connectionHelper.CONN();
            if (connect != null)
            {
                String qu = "SELECT * FROM MH6010";
                Statement statemente = connect.createStatement();
                ResultSet resultSet = statemente.executeQuery(qu);
                while (resultSet.next())
                {
                    Map<String, String> dtname = new HashMap<String, String>();
                    dtname.put("textOF", resultSet.getString("h6_op"));
                    dtname.put("textOperador", resultSet.getString("h6_operado"));
                    dtname.put("textInicio", resultSet.getString("h6_dataini"));
                    data.add(dtname);

                }
                ConnectionResult="Sucess";
                isSucess=true;
                connect.close();
            }
            else {
                ConnectionResult = "Failed";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
