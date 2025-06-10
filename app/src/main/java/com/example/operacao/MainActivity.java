package com.example.operacao;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Connection connect;
    SimpleAdapter adapter;
    Connection connexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnload = (Button) findViewById(R.id.txtmsg);
        Button btnfech = (Button) findViewById(R.id.fechado);
        Button btnfila = (Button) findViewById(R.id.aguardando);
        ListView list = (ListView) findViewById(R.id.lista);
        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String,String>> data = new ArrayList<Map<String,String>>();
                try {
                    connect = concClass();
                    connexao = conx();
                    if(connexao != null){
                        String query="SELECT *, UPPER(h6_xstatus) as status, FORMAT(h6_dataini, 'dd/MM/yyyy') AS data_inicial, convert(varchar, h6_horaini ,8) AS hora_inicial FROM MH6010 WHERE h6_op IS NOT NULL AND h6_datafin IS NULL ORDER BY id DESC;";
                        Statement st = connect.createStatement();
                        ResultSet resultSet = st.executeQuery(query);
                        while (resultSet.next()){
                            Map<String, String> tab = new HashMap<String, String>();
                                tab.put("textop", "Numero da OF:");
                                tab.put("Operador", "Operador:");
                                tab.put("data", "Data Inicial:");
                                tab.put("status", "Status:");
                                tab.put("sts", resultSet.getString("status"));
                                tab.put("maquina", "Equipamentos:");
                                tab.put("h6_op", resultSet.getString("h6_op"));
                                String maquinas="SELECT * FROM SH1010 where H1_CODIGO = '"+resultSet.getString("h6_recurso")+"';";
                                Statement sh = connexao.createStatement();
                                ResultSet resultSh = sh.executeQuery(maquinas);
                                while (resultSh.next()) {
                                    tab.put("h6_recurso", resultSh.getString("H1_DESCRI"));
                                }
                                tab.put("h6_dataini", resultSet.getString("data_inicial"));
                                tab.put("h6_horaini", resultSet.getString("hora_inicial"));
                                String nomes="SELECT * FROM SRA010 where RA_MAT = '"+resultSet.getString("h6_operado")+"';";
                                Statement sr = connexao.createStatement();
                                ResultSet resultSra = sr.executeQuery(nomes);
                                while (resultSra.next()){
                                    tab.put("h6_operado", resultSra.getString("RA_NOME"));
                                }
                                data.add(tab);


                        }
                        String[] from = {"textop","Operador","data","status","sts", "maquina","h6_op", "h6_recurso", "h6_operado", "h6_dataini", "h6_horaini"};
                        int[] to = {R.id.textop, R.id.Operador, R.id.data, R.id.status, R.id.sts, R.id.maquina, R.id.h6_op, R.id.h6_recurso,R.id.h6_operado, R.id.h6_dataini, R.id.h6_horaini};
                        adapter = new SimpleAdapter(MainActivity.this, data, R.layout.listlayouttempleat, from, to);
                        list.setAdapter(adapter);
                    }

                } catch (Exception ex) {
                    Log.e("set Error", ex.getMessage());
                }

            }
        });
        btnfila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String,String>> data = new ArrayList<Map<String,String>>();
                try {
                    connect = concClass();
                    connexao = conx();
                    if(connexao != null){
                        String query="SELECT *, convert(varchar, h6_xgravacao ,103) AS data_hora, convert(varchar, h6_xgravacao ,8) AS data_hora_grava FROM MTEMP6010 WHERE h6_op IS NOT NULL AND h6_dataini IS  NULL and h6_datafin IS  NULL ORDER BY id DESC;";
                        Statement st = connect.createStatement();
                        ResultSet resultSet = st.executeQuery(query);
                        while (resultSet.next()){
                            Map<String, String> tab = new HashMap<String, String>();
                            tab.put("textop", "Numero da OF:");
                            tab.put("Operador", "Operador:");
                            tab.put("data", "Data Gravação:");
                            tab.put("status", "Status:");
                            tab.put("sts", "FILA");
                            tab.put("maquina", "Equipamentos:");
                            tab.put("h6_op", resultSet.getString("h6_op"));
                            String maquinas="SELECT * FROM SH1010 where H1_CODIGO = '"+resultSet.getString("h6_recurso")+"';";
                            Statement sh = connexao.createStatement();
                            ResultSet resultSh = sh.executeQuery(maquinas);
                            while (resultSh.next()) {
                                tab.put("h6_recurso", resultSh.getString("H1_DESCRI"));
                            }
                            tab.put("data_hora", resultSet.getString("data_hora"));
                            tab.put("data_hora_grava", resultSet.getString("data_hora_grava"));
                            String nomes="SELECT * FROM SRA010 where RA_MAT = '"+resultSet.getString("h6_operado")+"';";
                            Statement sr = connexao.createStatement();
                            ResultSet resultSra = sr.executeQuery(nomes);
                            while (resultSra.next()){
                                tab.put("h6_operado", resultSra.getString("RA_NOME"));
                            }
                            data.add(tab);


                        }
                        String[] from = {"textop","Operador","data","status","sts", "maquina","h6_op", "h6_recurso", "h6_operado", "data_hora", "data_hora_grava"};
                        int[] to = {R.id.textop, R.id.Operador, R.id.data, R.id.status, R.id.sts, R.id.maquina, R.id.h6_op, R.id.h6_recurso,R.id.h6_operado, R.id.h6_dataini, R.id.h6_horaini};
                        adapter = new SimpleAdapter(MainActivity.this, data, R.layout.listlayouttempleat, from, to);
                        list.setAdapter(adapter);
                    }

                } catch (Exception ex) {
                    Log.e("set Error", ex.getMessage());
                }

            }
        });
        btnfech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String,String>> data = new ArrayList<Map<String,String>>();
                try {
                    connect = concClass();
                    connexao = conx();
                    if(connexao != null){
                        String query="SELECT *, UPPER(h6_xstatus) as status, FORMAT(h6_datafin, 'dd/MM/yyyy') AS data_final, convert(varchar, h6_horafin ,8) AS hora_final FROM MH6010 WHERE h6_op IS NOT NULL AND h6_datafin IS NOT NULL ORDER BY id DESC;";
                        Statement st = connect.createStatement();
                        ResultSet resultSet = st.executeQuery(query);
                        while (resultSet.next()){
                            Map<String, String> tab = new HashMap<String, String>();
                            tab.put("textop", "Numero da OF:");
                            tab.put("Operador", "Operador:");
                            tab.put("data", "Data Final:");
                            tab.put("status", "Status:");
                            tab.put("sts", resultSet.getString("status"));
                            tab.put("maquina", "Equipamentos:");
                            tab.put("h6_op", resultSet.getString("h6_op"));
                            String maquinas="SELECT * FROM SH1010 where H1_CODIGO = '"+resultSet.getString("h6_recurso")+"';";
                            Statement sh = connexao.createStatement();
                            ResultSet resultSh = sh.executeQuery(maquinas);
                            while (resultSh.next()) {
                                tab.put("h6_recurso", resultSh.getString("H1_DESCRI"));
                            }
                            tab.put("h6_dataini", resultSet.getString("data_final"));
                            tab.put("h6_horaini", resultSet.getString("hora_final"));
                            String nomes="SELECT * FROM SRA010 where RA_MAT = '"+resultSet.getString("h6_operado")+"';";
                            Statement sr = connexao.createStatement();
                            ResultSet resultSra = sr.executeQuery(nomes);
                            while (resultSra.next()){
                                tab.put("h6_operado", resultSra.getString("RA_NOME"));
                            }
                            data.add(tab);


                        }
                        String[] from = {"textop","Operador","data","status","sts", "maquina","h6_op", "h6_recurso", "h6_operado", "h6_dataini", "h6_horaini"};
                        int[] to = {R.id.textop, R.id.Operador, R.id.data, R.id.status, R.id.sts, R.id.maquina, R.id.h6_op, R.id.h6_recurso,R.id.h6_operado, R.id.h6_dataini, R.id.h6_horaini};
                        adapter = new SimpleAdapter(MainActivity.this, data, R.layout.listlayouttempleat, from, to);
                        list.setAdapter(adapter);
                    }

                } catch (Exception ex) {
                    Log.e("set Error", ex.getMessage());
                }

            }
        });
    }
    @SuppressLint("NewApi")
    public Connection concClass(){
        String ip = " ", port = " ", database = " ", un = " ", password = " ";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String conUrl = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conUrl = "jdbc:jtds:sqlserver://"+ip+":"+port+";"+ database;
            con = DriverManager.getConnection(conUrl, un, password);
        } catch (Exception ex) {
            Log.e("set Error", ex.getMessage());
        }
        return con;
    }
    @SuppressLint("NewApis")
    public Connection conx(){
        String ip = " ", port = "1433", database = " ", un = " ", password = " ";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String connUrl = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connUrl = "jdbc:jtds:sqlserver://"+ip+":"+port+";"+ database;
            conn = DriverManager.getConnection(connUrl, un, password);
        } catch (Exception ex) {
            Log.e("set Error", ex.getMessage());
        }
        return conn;
    }




}
