/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qepcomplete;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author PUTRI
 */
public class readFile {
     private ArrayList<table> tabel;
     private ArrayList<String> infoAll;
     private ArrayList<String> infoTable;
    //File location contoh: E:/Program Files/readme.txt
    
    public readFile(String fileLocation){
        this.tabel = new ArrayList<table>();
        this.infoAll = new ArrayList<String>();
        this.infoTable = new ArrayList<String>();
        try {
            String file = fileLocation;
            Scanner scanner = new Scanner(new File(file));
            ArrayList<String> tableSplit = new ArrayList<String>();
            while (scanner.hasNext()){
               tableSplit.add(scanner.nextLine());
            }
                for (int i = 0; i < tableSplit.size(); i++) {
                    String[] rowSplit = tableSplit.get(i).split(";");
                    if(i == 0){
                        infoAll.add(rowSplit[0]);
                        infoAll.add(rowSplit[1]);
                    }else{
                        table tab = new table(rowSplit[0]);
                        String[] rows = rowSplit[1].split(",");
                        for (int k = 0; k < rows.length; k++) {
                            tab.insertCol(rows[k]);
                        }
                        this.infoTable.add(rowSplit[2]+";"+rowSplit[3]+";"+rowSplit[4]);
                        tabel.add(tab);
                    }

                }
        } catch (FileNotFoundException ex) {
            System.out.println(ex+" | File tidak ditemukan!");
        }
    }

    public ArrayList<table> getTabel() {
        return this.tabel;
    }

    public ArrayList<String> getInfoAll() {
        return infoAll;
    }

    public ArrayList<String> getInfoTable() {
        return infoTable;
    }
    
}
