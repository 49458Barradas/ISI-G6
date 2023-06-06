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
        String SQL = "INSERT INTO TRABALHO (data_planeada, data_execucao, estado, id_obra, inspector VALUES(?, ? , ? , ? , ?) RETURNING id";
        String INSERT_CMD = "INSERT INTO INSPECAO_PRINCIPAL (id_trabalho, indice_condicao, estado_conservacao) VALUES(?, ?, ?)";
        try {
            Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt1 = con.prepareStatement(SQL);
            pstmt1.setDate(1, pi.getPlanedDate());
            pstmt1.setDate(2, pi.getExecutionDate());
            pstmt1.setString(3, pi.getState());
            pstmt1.setInt(4, pi.getIDObra());
            pstmt1.setString(5, pi.getInspector());
            pstmt1.setString(6, pi.getManager());
            pstmt1.setString(7, pi.getArtDisc());
            con.setAutoCommit(false);
            ResultSet rs = pstmt1.executeQuery();
            int id_trabalho = rs.getInt(1);
            PreparedStatement pstmt2 = con.prepareStatement(INSERT_CMD);
            pstmt2.setInt(1, id_trabalho);
            pstmt2.setInt(2, pi.getCondition());
            pstmt2.setInt(3, pi.getConservationState());
            pstmt2.executeUpdate();
            ResultSet rs2 = pstmt2.executeQuery();
            con.commit();
            rs2.close();
            pstmt2.close();
            rs.close();
            pstmt1.close();
            con.close();
        }
        catch(Exception ignored){}
    }
}
