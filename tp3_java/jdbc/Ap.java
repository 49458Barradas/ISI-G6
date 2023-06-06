/*
 * @Author: Matilde Pato (mpato)
 * @Date:   2023-05-26 18:55:00
 * @Last Modified time: 2023-05-26 18:55:00
 * ISEL - DEETC
 * Introdução a Sistemas de Informação
 * MPato, 2022-2023
 * 
 * NOTA: O código encontra-se dividido por classes, não deve adicionar mais classes,
 * nem alterar a sua configuração inicial.
 * 1) a classe PrincipalInspection é uma classe que contém os atributos da tabela 
 * INSPECCAOPRINCIPAL. Já está implementada!
 * 2) a classe Model é onde deverão implementar todos os métodos da aplicação
 * 3) a classe Restriction deve conter as restrições ao modelo de dados. Ela
 * é executada, apenas, quando existe uma nova entrada nas tabelas a que está
 * afecta.
 * 
 */
package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
interface DbWorker
{
    void doWork();
}
class App
{
    private enum Option
    {
        // DO NOT CHANGE ANYTHING!
        Unknown,
        Exit,
        novelInspection,
        removeInspector,
        totalCost,
        listWorks,
        listContentionWorks,
        listInspectors,
    }
    private static App __instance = null;
    private String __connectionString;

    private HashMap<Option,DbWorker> __dbMethods;

    private App()
    {
        // DO NOT CHANGE ANYTHING!
        __dbMethods = new HashMap<Option,DbWorker>();
        __dbMethods.put(Option.novelInspection, () -> App.this.novelInspection());    
        __dbMethods.put(Option.removeInspector, () -> App.this.removeInspector()); 
        __dbMethods.put(Option.totalCost, () -> App.this.totalCost());  
        __dbMethods.put(Option.listWorks, () -> App.this.listWorks());    
        __dbMethods.put(Option.listContentionWorks, new DbWorker() {public void doWork() {App.this.listContentionWorks();}});
        __dbMethods.put(Option.listInspectors, new DbWorker() {public void doWork() {App.this.listInspectors();}});
    }

    public static App getInstance()
    {
        if(__instance == null)
        {
            __instance = new App();
        }
        return __instance;
    }

    private Option DisplayMenu()
    {
        Option option = Option.Unknown;
        try
        {
            // DO NOT CHANGE ANYTHING!
            System.out.println("Company management");
            System.out.println();
            System.out.println("1. Exit");
            System.out.println("2. Novel inspection");
            System.out.println("3. Remove inspector");
            System.out.println("4. Cost total");
            System.out.println("5. List works");
            System.out.println("6. List contention works");
            System.out.println("7. List inspector");
            System.out.print(">");
            Scanner s = new Scanner(System.in);
            int result = s.nextInt();
            option = Option.values()[result];
        }
        catch(RuntimeException ex)
        {
            //nothing to do.
        }
        return option;

    }
    private static void clearConsole() throws Exception
    {
        for (int y = 0; y < 25; y++) //console is 80 columns and 25 lines
            System.out.println("\n");

    }
    private void Login() throws java.sql.SQLException
    {
        Connection con = DriverManager.getConnection(getConnectionString());
        if(con != null)
            con.close();
    }

    public void Run() throws Exception
    {
        Login();
        Option userInput;
        do
        {
            clearConsole();
            userInput = DisplayMenu();
            clearConsole();
            try
            {
                __dbMethods.get(userInput).doWork();
                System.in.read();

            }
            catch(NullPointerException ex)
            {
                //Nothing to do. The option was not a valid one. Read another.
            }

        }while(userInput!=Option.Exit);
    }

    public String getConnectionString()
    {
        return __connectionString;
    }
    public void setConnectionString(String s)
    {
        __connectionString = s;
    }

    /**
    To implement from this point forward. Do not need to change the code above.
    -------------------------------------------------------------------------------     
        IMPORTANT:
    --- DO NOT MOVE IN THE CODE ABOVE. JUST HAVE TO IMPLEMENT THE METHODS BELOW ---
    -------------------------------------------------------------------------------
    
    */

    private static final int TAB_SIZE = 24;
    static void printResults(ResultSet dr) throws SQLException {
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

    private void novelInspection() { //adicionar de inspeção principal
        // IMPLEMENTED
        System.out.println("novelInspection()");
        String values = Model.inputData( "work id, condition index and state of conservation.\n");
        // validate all entries!
        String value = Model.inputData( "planed date, execution date, state, id da obra, inspector, manager, atributo discriminante [data sobre o formato dd-mm-yyyy].\n");
        PrincipalInspection pi = new PrincipalInspection(values, value);
        Model.registerInspection(pi);
    }
      
    private void removeInspector()
    {
        Scanner s = new Scanner(System.in);
        System.out.println("Mekie meu puto queres apagar como: ");
        System.out.print(">");
        String inp = s.nextLine();
        try{
            Connection con = DriverManager.getConnection(getConnectionString());
            Statement stmt = con.createStatement();
            String SQL = "DELETE FROM UTILIZADOR WHERE email='" + inp + "';";
            stmt.executeQuery(SQL);
            con.close();
        }
        catch(Exception ignored){}
        System.out.println("Delete terminado");
        //System.out.println("removeInspector()");
    }

    private void totalCost()
    {
        System.out.println("Insere o tipo de estrutura [muro/parede/aterro/talude/barreira/solução mista]: ");
        System.out.print(">");
        String structure_type = readln();
        try{
            Connection conn = DriverManager.getConnection(getConnectionString());
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT custo, te.tipo FROM OBRA_CONTENCAO oc JOIN TIPO_ESTRUTURA te ON oc.tipo_estrutura = te.id WHERE te.tipo = '" + structure_type + "'");
            printResults(rset);
            rset.close();
            stmt.close();
            conn.close();
        }
        catch(Exception ignored){}
        System.out.println("totalCostContentionWork()");
    }

    private void listInspector()
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
        //System.out.println("listInspector");
    }

    private void listWorks() {
        listGestor();
        System.out.println("");
        System.out.println("Insere o Gestor [que realizou os trabalhos]: ");
        System.out.print(">");
        String gestor = readln();
        System.out.println("");
        listInspectors();
        System.out.println("");
        System.out.println("Insere o Inspetor [que não inspecionou o trabalho]: ");
        System.out.print(">");
        System.out.println("");
        String inspetor = readln();
        System.out.println("");
        try{
            Connection conn = DriverManager.getConnection(getConnectionString());
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select  t.id as \"id_trabalho\"\n" +
                    "from trabalho t\n" +
                    "join utilizador gestor on gestor.email = t.gestor\n" +
                    "join utilizador inspetor on inspetor.email = t.inspetor\n" +
                    "where  gestor.nome = '" + gestor + "' and inspetor.nome <> '" + inspetor +"';");
            conn.close();
            printResults(rset);
        }
        catch(Exception ignored){}
        System.out.println("allWorksDoneBy()");
    }

    public static String[] separateStringByComma(String input) {
        String[] separatedStrings = input.split(",");
        return separatedStrings;
    }

    private void listContentionWorks()
    {
        System.out.println("Insere o atributo discriminante de TRABALHO [CM/IP/IR] : ");
        System.out.print(">");
        String tipo = readln();
        System.out.println("");
        System.out.println("Insere o(s) estado(s) dos trabalhos [executado/validado/planeado]: ");
        System.out.print(">");
        String temp = readln();
        String[] estados = separateStringByComma(temp);
        System.out.println("");
        try{
            Connection conn = DriverManager.getConnection(getConnectionString());
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
            conn.close();
            printResults(rset);
        }
        catch(Exception ignored){}
        System.out.println("listContentionWorks()");
    }

    private void listGestor()
    {
        try{;
            Connection conn = DriverManager.getConnection(getConnectionString());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT u.email FROM UTILIZADOR u \n JOIN TRABALHO t ON u.email = t.gestor");
            conn.close();
            printResults(rs);
        }
        catch(Exception ignored){}
    }

    private void listInspectors()
    {
        listGestor();
        System.out.println("");
        System.out.println("Insere email do Gestor: ");
        System.out.print(">");
        String gestor = readln();
        System.out.println("");
        try{
            Connection conn = DriverManager.getConnection(getConnectionString());
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
            conn.close();
            printResults(rset);
        }
        catch(Exception ignored){}
        System.out.println("inspectorsThatWorkWith()");
    }

    private static String readln() {
        Scanner s = new Scanner(System.in);
        String inp = s.nextLine();
        return inp;
    }

}

public class Ap{
    public static void main(String[] args) throws Exception{
        String url = "jdbc:postgresql://10.62.73.58:5432/?user=ab6&password=ab6&ssl=false";
        App.getInstance().setConnectionString(url);
        App.getInstance().Run();
        //ADICIONAR AQUI O ELIMINAR CLASSES QUE NAO RESPEITAM
    }
}