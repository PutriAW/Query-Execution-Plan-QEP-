/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qepcomplete;

import java.io.File;
import java.io.PrintWriter;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author PUTRI
 */
public class procAndFunc {
    ArrayList<String> pk = new ArrayList<String>();
    ArrayList<qepInfo> listqep = new ArrayList<qepInfo>();
    String hasilqep = "";
    
    public String removeUnusedSpace(String a){
        String b = a;
        b = b.trim().replaceAll("using", "using ");
        b = b.trim().replaceAll(" +", " ");
        b = b.trim().replaceAll(", ", ",");
        b = b.trim().replaceAll(" ,", ",");
        b = b.trim().replaceAll(" ;", ";");
        return b;
    }
    
    public String removeUnusedSpaceWhere(String a){
        String b = a;
        b = b.trim().replaceAll("< ", "<");
        b = b.trim().replaceAll(" <", "<");
        b = b.trim().replaceAll(" < ", "<");
        b = b.trim().replaceAll("> ", ">");
        b = b.trim().replaceAll(" >", ">");
        b = b.trim().replaceAll(" > ", ">");
        b = b.trim().replaceAll("= ", "=");
        b = b.trim().replaceAll(" =", "=");
        b = b.trim().replaceAll(">", " > ");
        b = b.trim().replaceAll("<", " < ");
        b = b.trim().replaceAll("=", " = ");
        b = b.trim().replaceAll("<  =", "<=");
        b = b.trim().replaceAll(">  =", ">=");
        return b;
    }
    
     public boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
    
    String[] checkColSpec(String[] colAr,ArrayList<table> colDB, ArrayList<Integer> idxTable) throws Exception{
        String [] col = new String[idxTable.size()];
        for (int j = 0; j < colAr.length; j++) {
            int cek=0;
            for (int k = 0; k < idxTable.size(); k++) {
                String stCol = "";
                for (int l = 0; l < colDB.get(idxTable.get(k)).getCol().size(); l++) {
                    if(colDB.get(idxTable.get(k)).getCol().get(l).equals(colAr[j]) || colAr[j].equals("*")){
                        if(col[k]==null){
                            col[k]="";
                        }
                        col[k] = col[k]+colDB.get(idxTable.get(k)).getCol().get(l)+", ";
                        cek += 1;
                    }  
                }
            }
            if (cek==0){
                System.out.println("SQL Error (Some column not found)");
            }
        }
        return col;
    }
    
  
    public boolean checkPK(ArrayList<table> tabDB, ArrayList<Integer> idx, ArrayList<String> pklist){
        boolean success = true;
        if(!pklist.isEmpty()){
            for (String pk:pklist) {
                int a=0;
                for (table tab:tabDB) {
                    for (String col:tab.getCol()) {
                        if(col.equals(pk)){
                            a+=1;
                        }

                    }
                }
                if(a<=1){
                    return false;
                }
            }
            return success;
        }else{
            return false;
        }
    }
    
    public int getIdxTable(String tab, ArrayList<table> as){
        for(int j=0;j<as.size();j++){
            if(as.get(j).getTableName().toLowerCase().equals(tab) ){
                return j;
            }
        }
        return -1;
    }
   
    public double costA1(double br){
        return br;
    }
    public double costA1withKey(double br){
        return br/2;
    }
    public double costA2(int B,int V, int P,int bfr, int n){
        double y = Math.floor(B/(V+P));
        double hi = Math.ceil(log(Math.ceil(n/bfr))/log(y));
        return hi+1;
    }
    public double costBNLJ(double br, double bs){
        return (br*bs)+bs;
    }
    
    public ArrayList<String> getPk() {
        return pk;
    }
    
    public void showQEPJoin(String[] splitwhere,ArrayList<table> colDB, ArrayList<Integer> idxTable,ArrayList<String> pklist,String[] col,int B,int P){
        hasilqep += ("PROJECTION ");
        for (int j = 0; j < idxTable.size(); j++) {
            hasilqep += (col[j]);
        }
        hasilqep += ("\n");
        hasilqep += ("   JOIN ");
        for(String pkname:pklist){
            String as = "";
            for(int i:idxTable){
                for(String colName:colDB.get(i).getCol()){
                    if(colName.equals(pkname)){
                        if(as.equals("")){
                            as+=colDB.get(i).getTableName()+"."+pkname+" = ";
                        }else{
                            as+=colDB.get(i).getTableName()+"."+pkname+" ";
                        }
                    }
                }
            }
            hasilqep+=(as);
        }
        hasilqep +=(" -- BNLJ");
        hasilqep += ("\n");
        for (int j = 0; j < idxTable.size(); j++) {
            hasilqep += (colDB.get(idxTable.get(j)).getTableName()+"        ");
        }
    }
    
    public void checkWhere(String[] splitwhere,ArrayList<Integer> idxTable,ArrayList<table> colDB,String [] col,int B,int P,int checkWhereQEP){
        try{
            if(splitwhere.length == 2){
                hasilqep="";
                hasilqep += ("PROJECTION ");
                for (int j = 0; j < idxTable.size(); j++) {
                    hasilqep += (col[j]);
                }
                hasilqep += (" -- on the fly");
                hasilqep += ("\n");
                 String where = splitwhere[1];
                 where = this.removeUnusedSpaceWhere(where);
                 String[] splitWhereName = where.split(" ");
                 int i = 0;
                 hasilqep += "SELECTION ";
                 while (i<splitWhereName.length){
                     String kon1 = splitWhereName[i];
                     String op = splitWhereName[i+1];
                     String kon2 = splitWhereName[i+2];
                     String after = "";
                     if(kon2.charAt(kon2.length()-1)==';'){
                         kon2 = kon2.trim().replaceAll(";", "");
                         i = splitWhereName.length;
                     }else{
                         after = splitWhereName[i+3];
                         i+=2;
                     }
                     if(!(isStringInt(kon1) && isStringInt(kon2))){
                         String colName = "";
                         String colName2 = "";
                         int check = 0;
                         if(!isStringInt(kon1)){
                             for (int a:idxTable){
                                 for(String st:colDB.get(a).getCol()){
                                     if(st.equals(kon1)){
                                         check +=1;
                                     }
                                 }
                             }
                         }
                         if(check >= 1){
                             hasilqep += kon1;
                         }else{
                             hasilqep += " Error ";
                             break;
                         }
                         hasilqep += (" "+op+" ");
                         if(!isStringInt(kon2)){
                             check = 0;
                             for (int a:idxTable){
                                 for(String st:colDB.get(a).getCol()){
                                     if(st.equals(kon1)){
                                         check +=1;
                                     }
                                 }
                             }
                         }
                         if(check >= 1){
                             hasilqep += (kon2);
                         }else{
                             hasilqep += " Error "; 
                             break;
                         }
                         after = after.trim().replaceAll("and", "Ʌ");
                         after = after.trim().replaceAll("or", "V");
                         hasilqep += (" "+after+" ");
                     }else{
                         hasilqep += " Error "; 
                         break;
                     }
                     i+=2;
                 }
                 if(checkWhereQEP==1){
                     hasilqep += (" -- A"+checkWhereQEP+" key");
                     hasilqep += ("\n");
                     hasilqep += (colDB.get(idxTable.get(0)).getTableName()+"        ");
                     
                 }else{
                     hasilqep += (" -- A"+checkWhereQEP);
                     hasilqep += ("\n");
                     hasilqep += (colDB.get(idxTable.get(0)).getTableName()+"        ");
                 }
                 checkWhereQEP+=1;
             }
        }catch(Exception e){
            hasilqep += " Error ";
        }
    }
    
    public void checkCol(String[] splitwhere,ArrayList<table> colDB, ArrayList<Integer> idxTable,ArrayList<String> pklist,
            int B,int P,int[] tbr, int[] tbN,int[] tbv,String sqlCommand) throws Exception{
        String[] colAr = splitwhere[0].split(" ")[1].split(",");
        String [] col = new String[idxTable.size()];
        for (int j = 0; j < colAr.length; j++) {
            int cek=0;
            for (int k = 0; k < idxTable.size(); k++) {
                String stCol = "";
                for (int l = 0; l < colDB.get(idxTable.get(k)).getCol().size(); l++) {
                    if(colDB.get(idxTable.get(k)).getCol().get(l).equals(colAr[j]) || colAr[j].equals("*")){
                        if(col[k]==null){
                            col[k]="";
                        }
                        col[k] = col[k]+colDB.get(idxTable.get(k)).getCol().get(l)+",";
                        cek += 1;
                    }  
                }
            }
            if (cek==0){
                System.out.println("SQL Error (Some column not found)");
                return;
            }
        }
        for (int j = 0; j < idxTable.size(); j++) {
            System.out.println("Tabel ("+(j+1)+") : "+colDB.get(idxTable.get(j)).getTableName());
            System.out.print("List Kolom : ");
            System.out.println(col[j]);
            System.out.println("");
        }
        if(!pklist.isEmpty()){
            for (int i = 0; i < idxTable.size(); i++) {
                for (int j = 0; j < idxTable.size(); j++) {
                    hasilqep = "";
                    if(idxTable.get(i)!=idxTable.get(j)){
                        this.showQEPJoin(splitwhere,colDB, idxTable, pklist, col, B, P);
                        int bfr = Math.floorDiv(B ,tbr[i]);
                        double br = Math.ceil(tbN[i]/bfr)+1;
                        int bfs = Math.floorDiv(B ,tbr[j]);
                        double bs = Math.ceil(tbN[j]/bfs)+1;
                        listqep.add(new qepInfo(hasilqep,this.costBNLJ(br,bs)));
                    }
                }
            }
        }else{
            if(splitwhere.length==1){
                hasilqep = "";
                hasilqep += ("PROJECTION ");
                for (int j = 0; j < idxTable.size(); j++) {
                    hasilqep += (col[j]);
                }
                hasilqep += (" -- on the fly");
                hasilqep += ("\n");
                for (int j = 0; j < idxTable.size(); j++) {
                    hasilqep += (colDB.get(idxTable.get(j)).getTableName()+"        ");
                }
                listqep.add(new qepInfo(hasilqep,this.costA1(Math.ceil(tbN[0]/Math.floorDiv(B ,tbr[0]))+1)));
            }else{
                int checkWhereQEP = 1;
                this.checkWhere(splitwhere, idxTable, colDB, col,B,P,checkWhereQEP);
                listqep.add(new qepInfo(hasilqep,this.costA1withKey(Math.ceil(tbN[0]/Math.floorDiv(B ,tbr[0]))+1)));
                checkWhereQEP += 1;
                this.checkWhere(splitwhere, idxTable, colDB, col,B,P,checkWhereQEP);
                listqep.add(new qepInfo(hasilqep,this.costA2(B, tbv[0],P,Math.floorDiv(B, tbr[0]),tbN[0])));
            }
        }
        if(hasilqep.contains("Error")){
            System.out.println("SQL Error (Syntax Error)");
        }else{
            for (int i = 0; i < listqep.size(); i++) {
                qepInfo q = listqep.get(i);
                System.out.println("QEP #"+(i+1));
                System.out.println(q.getQep());
                System.out.println("Cost : "+q.getCost()+" Blok");
            }
            int idxmincost = 0;
            int i = 0;
            for (qepInfo q:listqep) {
                if(listqep.get(i).getCost()<listqep.get(idxmincost).getCost()){
                    idxmincost = i;
                }
                i++;
            }
            System.out.println("QEP optimal : QEP#"+(idxmincost+1));
            File f = new File("sharedpool.txt");
            if(f.exists() && !f.isDirectory()) { 
                String inside = "";
                String file = "sharedpool.txt";
                Scanner scanner = new Scanner(new File(file));
                while (scanner.hasNext()){
                    inside+=scanner.nextLine()+"\n";
                }
                inside+="\n";
                inside+=("Query :"+sqlCommand);
                inside+="\n";
                inside+=listqep.get(idxmincost).getQep();
                inside+="\n";
                inside+=("Cost : "+listqep.get(idxmincost).getCost()+" Blok");
                PrintWriter writer = new PrintWriter("sharedpool.txt", "UTF-8");
                writer.println(inside);
                writer.close();
            }else{
                PrintWriter writer = new PrintWriter("sharedpool.txt", "UTF-8");
                writer.println("Query :"+sqlCommand);
                writer.println(listqep.get(idxmincost).getQep());
                writer.close();
            }
            
        }
    }
    
     public void joinTable(String[] sqlcomSplit, ArrayList<table> as, procAndFunc f, ArrayList<Integer> tableCount,
             String[] colSplit,String[] splitwhere,int B,int P,int[]tbr,int[] tbN,int[] tbv,String sqlCommand) throws Exception{
        ArrayList<String> pk = new ArrayList<String>();
        for (int i = 0; i < sqlcomSplit.length; i++) {
            if(sqlcomSplit[i].equals("join")){
                String joinTab = sqlcomSplit[i+1];
                int p = -1;
                p = f.getIdxTable(joinTab, as);
                if (p == -1){
                    System.out.println("SQL Error (Unexpected '"+sqlcomSplit[i+1]+"')");
                    break;
                }else{
                    tableCount.add(p);
                    if(sqlcomSplit[i+2].equals("using")){
                        int ikb = sqlcomSplit[i+3].indexOf("(");
                        int ikt = sqlcomSplit[i+3].indexOf(")");
                        String joinPK = sqlcomSplit[i+3].substring(ikb+1,ikt);
                        pk.add(joinPK);                       
                    }else{
                        System.out.println("SQL Error (Using tidak ditemukan)");
                        break;
                    }
                }
            }
        }
        if(f.checkPK(as, tableCount, pk)){
            f.checkCol(splitwhere, as, tableCount,pk,B,P,tbr,tbN,tbv,sqlCommand);
        }else{
            System.out.println("SQL Error (Syntax Error)"); 
            
        }

    }
    
}
