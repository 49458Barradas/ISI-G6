/*
 * @Author: Matilde Pato (mpato)
 * @Date:   2023-05-26 18:55:00
 * @Last Modified time: 2023-05-26 18:55:00
 * ISEL - DEETC
 * Introdução a Sistemas de Informação
 * MPato, 2022-2023
 * 
 * NOTA:
 * Nesta classe deverá implementar todas as opções enumeradas no
 * enunciado. Poderá ter necessidade de importar mais classes.
 */

package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

class Model {

    static String inputData(String str){
        // IMPLEMENTED
        java.util.Scanner key = new Scanner(System.in);
        System.out.print("Enter corresponding values, separated by commas, \n" + str);
        String values = key.nextLine();
        return values;
    }

    /**
    To implement from this point forward. Do not need to change the code above.
    -------------------------------------------------------------------------------     
        IMPORTANT:
    --- DO NOT MOVE IN THE CODE ABOVE. JUST HAVE TO IMPLEMENT THE METHODS BELOW ---
    -------------------------------------------------------------------------------
    
    */

    static void registerInspection(PrincipalInspection pi)
    {
        String SELECT_CMD = "SELECT * FROM INSPECAO_PRINCIPAL";
        String INSERT_CMD = "INSERT INTO INSPECAO_PRINCIPAL (id_trabalho, indice_condicao, estado_conservacao) VALUES(" + pi.getWorkID() + "," + pi.getCondition() + "," + pi.getState() + ")";
        try (
                Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
                PreparedStatement pstmt1 = con.prepareStatement(SELECT_CMD);
                PreparedStatement pstmt2 = con.prepareStatement(INSERT_CMD);

        ) {
            con.setAutoCommit(false);
            int rowsInserted = pstmt2.executeUpdate();
            ResultSet rs = pstmt1.executeQuery();
            if (rowsInserted > 0) {
                System.out.println("Principal inspection registered!!!!");
                App.printResults(rs);
            } else {
                System.out.println("Failed to register principal inspection.");
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error on insert values");
        }
    }
    

}
