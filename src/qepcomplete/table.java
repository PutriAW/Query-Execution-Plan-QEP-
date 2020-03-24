/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qepcomplete;

import java.util.ArrayList;

/**
 *
 * @author PUTRI
 */
class table {
    private String table_name;
    private ArrayList<String> col;

    public table(String table_name) {
        this.table_name = table_name;
        this.col = new ArrayList();
    }
    public void insertCol(String columnName){
        this.col.add(columnName);
    }
    public String getTableName(){
        return this.table_name;
    }

    public ArrayList<String> getCol() {
        return col;
    }
}
