package jdbc;

import java.sql.*;
import java.util.Scanner;

public class ex2 {

    public void totalCostContentionWork(){
        System.out.println("Mete aí o meu tipo de estrutura oh mano: ");
        System.out.print(">");
        String structure_type = readln();
        try{
            Connection conn = DriverManager.getConnection("jdbc:postgresql://10.62.73.58:5432/?user=ab6&password=ab6&ssl=false");
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT custo, te.tipo FROM OBRA_CONTENCAO oc JOIN TIPO_ESTRUTURA te ON oc.tipo_estrutura = te.id WHERE te.tipo = '" + structure_type + "'");
            printResults(rset);
            rset.close();
            stmt.close();
            conn.close();
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("totalCostContentionWork()");
    }

    private void listInspectors()
    {
        try{;
            Connection conn = DriverManager.getConnection("jdbc:postgresql://10.62.73.58:5432/?user=ab6&password=ab6&ssl=false");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT u.nome FROM UTILIZADOR u \n JOIN TRABALHO t ON u.email = t.inspetor");
            printResults(rs);
        }
        catch(Exception e){
            System.out.println(e);
        }
        //System.out.println("listInspectors");
    }

    private void listGestor()
    {
        try{;
            Connection conn = DriverManager.getConnection("jdbc:postgresql://10.62.73.58:5432/?user=ab6&password=ab6&ssl=false");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT u.nome FROM UTILIZADOR u \n JOIN TRABALHO t ON u.email = t.gestor");
            printResults(rs);
        }
        catch(Exception e){
            System.out.println(e);
        }
        //System.out.println("listInspectors");
    }

    public void allWorksDoneBy(){
        listGestor();
        System.out.println("");
        System.out.println("Mete aí o gestis (alcunha bacana que dei): ");
        System.out.print(">");
        String gestor = readln();
        System.out.println("");
        listInspectors();
        System.out.println("");
        System.out.println("Mete aí o inspeties (alcunha bacana que dei): ");
        System.out.print(">");
        System.out.println("");
        String inspetor = readln();
        System.out.println("");
        try{
            Connection conn = DriverManager.getConnection("jdbc:postgresql://10.62.73.58:5432/?user=ab6&password=ab6&ssl=false");
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select  t.id as \"id_trabalho\"\n" +
                    "from trabalho t\n" +
                    "join utilizador gestor on gestor.email = t.gestor\n" +
                    "join utilizador inspetor on inspetor.email = t.inspetor\n" +
                    "where  gestor.nome = '" + gestor + "' and inspetor.nome <> '" + inspetor +"';");
            printResults(rset);
        }
        catch(Exception ignored){}
        System.out.println("allWorksDoneBy()");
    }

    public void workWithoutEstate(){
        System.out.println("Inserir um [CM/IP/IR] para não aparecer: ");
        System.out.print(">");
        String tipo = readln();
        System.out.println("");
        System.out.println("Mete aí o(s) estado(s) oh putx [somos bue LBGTQ+ friendly] (sem maiar xe xe xe damx): ");
        System.out.print(">");
        String temp = readln();
        String[] estados = separateStringByComma(temp);
        System.out.println("");
        try{
            Connection conn = DriverManager.getConnection("jdbc:postgresql://10.62.73.58:5432/?user=ab6&password=ab6&ssl=false");
            Statement stmt = conn.createStatement();
            Integer idx = 0;
            String SQL = "select distinct  oc.id\n" +
                    "from obra_contencao oc \n" +
                    "join trabalho t on oc.id = t.id_obra \n" +
                    "where t.atrdisc <> '" + tipo +  "' or t.atrdisc = '" + tipo + "' ";
            while(idx<estados.length){
                SQL += "AND t.estado <> '" + estados[idx] + "' ";
                idx++;
            }
            SQL += "\norder by oc.id asc;";
            System.out.println(SQL);
            ResultSet rset = stmt.executeQuery(SQL);
            printResults(rset);
        }
        catch(Exception ignored){}
        System.out.println("workWithoutEstate()");
    }

    public static String[] separateStringByComma(String input) {
        String[] separatedStrings = input.split(",");
        return separatedStrings;
    }

    public void inspectorsThatWorkWith(){
        listGestor();
        System.out.println("");
        System.out.println("Mete aí o gestis (alcunha bacana que dei): ");
        System.out.print(">");
        String gestor = readln();
        System.out.println("");
        try{
            Connection conn = DriverManager.getConnection("jdbc:postgresql://10.62.73.58:5432/?user=ab6&password=ab6&ssl=false");
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select  distinct  u.nome\n" +
                    "from utilizador u\n" +
                    "join trabalho t on u.email = t.inspetor\n" +
                    "where  t.gestor  in (\n" +
                    "  select  t2.gestor \n" +
                    "  from trabalho t2\n" +
                    "  join utilizador u2 on t2.gestor = u2.email\n" +
                    "  where  u2.nome = '" + gestor + "'\n" +
                    ");");
            printResults(rset);
        }
        catch(Exception ignored){}
        System.out.println("inspectorsThatWorkWith()");
    }

    private static void printResults(ResultSet dr) throws SQLException {
        ResultSetMetaData metaData = dr.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Print column names
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println("\n-----------------------------------------------------");

        // Print row data
        while (dr.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(dr.getString(i) + "\t\t");
            }
            System.out.println();
        }
    }

    private static String readln() {
        Scanner s = new Scanner(System.in);
        String inp = s.nextLine();
        return inp;
    }

}

