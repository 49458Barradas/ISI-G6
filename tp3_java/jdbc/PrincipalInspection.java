/*
 * @Author: Matilde Pato (mpato)
 * @Date:   2023-05-26 18:55:00
 * @Last Modified time: 2023-05-26 18:55:00
 * ISEL - DEETC
 * Introdução a Sistemas de Informação
 * MPato, 2022-2023
 * 
 * Classe para o objecto inspecção principal referente à tabela INSPECAO_PRINCIPAL
 * Já se encontra implementada.
 */ 

package jdbc;

import java.sql.Date;

class PrincipalInspection {
    private int workid; //id trabalho
    private int idx_condition;
    private int conservation_state; //estado de conservação

    private Date planed_date;
    private Date execution_date;
    private String state;
    private int id_obra;
    private String inspector;
    private String manager;
    private String art_disc;

    
    PrincipalInspection(String values, String otherValues){
        String[] attr = values.split(",");
        workid = Integer.parseInt(attr[0]);
        idx_condition = Integer.parseInt(attr[1]);
        conservation_state = Integer.parseInt(attr[2]);
        String[] attr2 = otherValues.split(",");
        String[] attr3 = attr2[0].split("-");
        planed_date = new Date(Integer.parseInt(attr3[2]), Integer.parseInt(attr3[1]), Integer.parseInt(attr3[0]));
        String[] attr4 = attr2[1].split("-");
        execution_date = new Date(Integer.parseInt(attr4[2]), Integer.parseInt(attr4[1]), Integer.parseInt(attr4[0]));
        state = attr2[2];
        id_obra = Integer.parseInt(attr2[3]);
        inspector = attr2[4];
        manager = attr2[5];
        art_disc = attr2[6];
    }

    public Integer getWorkID() { return workid; }

    public Integer getCondition(){ return  idx_condition; }

    public Integer getConservationState() { return conservation_state; }

    public Date getPlanedDate() { return planed_date; }

    public Date getExecutionDate() { return execution_date; }

    public String getState() { return state; }

    public Integer getIDObra() { return id_obra; }

    public String getInspector() { return inspector; }

    public String getManager() { return manager; }

    public String getArtDisc() { return art_disc; }

 
    public void setWorkID(int workid) { this.workid = workid; }

    public void setCondition(int idx_condition) { this.idx_condition = idx_condition; }

    public void setState(int state) { this.conservation_state = state; }

}
