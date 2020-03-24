/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qepcomplete;

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author PUTRI
 */
public class main {
    
    public static void queryParser(readFile rd, int B, int P, int[] tbr, int[] tbN, int[] tbv){
        try{
            System.out.println("");
            procAndFunc f = new procAndFunc();
            Scanner scanner = new Scanner(System.in);
            System.out.print("SQL> "); 
            String sqlCommand = scanner.nextLine();
            sqlCommand = sqlCommand.toLowerCase();
            sqlCommand = f.removeUnusedSpace(sqlCommand);
            if(sqlCommand.charAt(sqlCommand.length()-1)==';'){
                String[] sqlcomSplit = sqlCommand.split(" ");
                String[] splitwhere = sqlCommand.split("where");
                if(sqlcomSplit[0].equals("select")){ 
                    if(sqlcomSplit[2].equals("from")){
                        String tableName = sqlcomSplit[3];
                        ArrayList<table> arTab = rd.getTabel(); 
                        ArrayList<Integer> tableCount = new ArrayList<Integer>();
                        int i = -1;
                        if(tableName.charAt(tableName.length()-1)==';'){
                            tableName = tableName.substring(0,tableName.length() - 1);
                        }
                        i = f.getIdxTable(tableName, arTab);
                        if(i==-1){
                            System.out.println("SQL Error (Unexpected '"+sqlcomSplit[3]+"')");
                        }else{
                            tableCount.add(i);
                            String[] colSplit = sqlcomSplit[1].split(",");
                            if(splitwhere[0].split(" ").length == 4){
                                f.checkCol(splitwhere, arTab, tableCount, new ArrayList<String>(),B,P,tbr,tbN,tbv,sqlCommand);
                            }else{
                                f.joinTable(sqlcomSplit, arTab, f, tableCount, colSplit, splitwhere,B,P,tbr,tbN,tbv,sqlCommand);
                            }
                        }
                    }else{
                        System.out.println("SQL Error (Unexpected '"+sqlcomSplit[2]+"')");
                    }
                }else{
                    System.out.println("SQL Error (Unexpected '"+sqlcomSplit[0]+"')");
                }
            }else{
                System.out.println("SQL Error (Missing ';')");
            }    
        }catch(Exception e){
            System.out.println(e);
//                System.out.println("SQL Error (Syntax Error)");
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        readFile rd = new readFile("E:\\files\\Teknik Informatika\\semester 4\\SBD\\Tubes QEP\\data.txt");
        ArrayList<table> arTab = rd.getTabel(); 
        ArrayList<String> infoDB = rd.getInfoAll(); //isinya P 4;B 8192
        ArrayList<String> infoTab = rd.getInfoTable(); //isinya R;n;V
        int[] tbBFR = new int[3];
        int[] tbN = new int[3] ;
        int[] tbr = new int[3];
        int[] tbv = new int[3];
        int B = parseInt(infoDB.get(1).split(" ")[1]);
        int P = parseInt(infoDB.get(0).split(" ")[1]);
        for (int i = 0; i < arTab.size(); i++) {
            int R = parseInt(infoTab.get(i).split(";")[0].split(" ")[1]);
            int N = parseInt(infoTab.get(i).split(";")[1].split(" ")[1]);
            int V = parseInt(infoTab.get(i).split(";")[2].split(" ")[1]); 
            int BFR = Math.floorDiv(B,R);
            try{
                tbr[i] = R;
                tbv[i] = V;
                tbN[i] = N;
                tbBFR[i] = BFR;
            }catch(Exception e){
                System.out.println(e);
            }
          
            
        }
        System.out.println("====================== Menu Utama ============================");
        System.out.println("1. Tampilkan BFR dan Fanout Ratio Setiaap Tabel");
        System.out.println("2. Tampilkan Total Blok Data + Blok Index Untuk Setiap Tabel");
        System.out.println("3. Tampilkan Jumlah Blok yang Diakses Untuk Pencarian Record");
        System.out.println("4. Tampilkan QEP dan Cost");
        System.out.println("5. Tampilkan Isi File Shared Pool");
        System.out.println("6. Keluar");
        Scanner sc = new Scanner(System.in);
        System.out.print(">> Masukkan Pilihan Anda: ");
        int pilihan = sc.nextInt();
        switch(pilihan) {
         case 1 :
            System.out.println("Menu I : BFR dan Fan Out Ratio");
            System.out.println("");
            for (int i = 0; i < arTab.size(); i++) {
                 int R = parseInt(infoTab.get(i).split(";")[0].split(" ")[1]);
                 int N = parseInt(infoTab.get(i).split(";")[1].split(" ")[1]);
                 int V = parseInt(infoTab.get(i).split(";")[2].split(" ")[1]);
                 System.out.println("BFR "+arTab.get(i).getTableName()+": "+Math.floorDiv(B,R));
                 System.out.println("Fan Out Ratio "+arTab.get(i).getTableName()+": "+Math.floorDiv(B,V+P));
             }
            System.exit(0);
            break;
         case 2 :
            System.out.println("Menu 2 : Total Blok Data + Blok Index");
            for (int i = 0; i < arTab.size(); i++) {
                int R = parseInt(infoTab.get(i).split(";")[0].split(" ")[1]);
                int n = parseInt(infoTab.get(i).split(";")[1].split(" ")[1]);
                int V = parseInt(infoTab.get(i).split(";")[2].split(" ")[1]);
                int BFR = Math.floorDiv(B,R);
                int FanRa = Math.floorDiv(B,V+P);
                float total_block = n/BFR; 
                float indeks = n/FanRa;
                System.out.printf("Tabel Data "+arTab.get(i).getTableName()+" : %,.0f Blok %n",total_block+1);
                System.out.printf("Index "+arTab.get(i).getTableName()+": %,.0f Block %n",indeks+1);    
             }
            System.exit(0);
            break;
         case 3 :
             System.out.println("Pencarian Record");
             System.out.println("");
             System.out.println("Input : ");
             System.out.print(">> Cari Record ke- : ");
             Scanner sci = new Scanner(System.in);
             int rc = sci.nextInt();
             int rec = rc;
             System.out.print(">> Nama Tabel : ");
             Scanner tabs = new Scanner(System.in);
             String tb = tabs.nextLine();
             System.out.println("");
             boolean found = false;
             double w_index = 0,wto_index = 0;
             for (int i = 0; i < arTab.size(); i++) {
                 if(tb.equalsIgnoreCase(arTab.get(i).getTableName())){
                    found = true;
                    int R = parseInt(infoTab.get(i).split(";")[0].split(" ")[1]);
                    int V = parseInt(infoTab.get(i).split(";")[2].split(" ")[1]);
                    int BFR = Math.floorDiv(B,R);
                    int FanRa = Math.floorDiv(B,V+P);
                    wto_index = rc/BFR;
                    w_index = (rc/FanRa) + 1;
//                     System.out.println(wto_index+" " +BFR +" "+FanRa);
//                     System.out.println(w_index);
                 }
             }
             System.out.println("Output : ");
             if(rec == 0){
                System.out.println("Menggunakan indeks, jumlah blok yang diakses : "+0+" Blok");
                System.out.println("Tanpa indeks, jumlah blok yang diakses : "+0+" Blok"); 
             }else{
                System.out.printf("Menggunakan indeks, jumlah blok yang diakses : %,.0f Blok %n",Math.ceil(w_index)+1);
                System.out.printf("Tanpa indeks, jumlah blok yang diakses : %,.0f Blok %n", Math.ceil(wto_index)+1);
                
             }
             
            break;
         case 4 :
            queryParser(rd,B,P,tbr,tbN,tbv);
            break;
         case 5 :
             File f = new File("sharedpool.txt");
              if(f.exists() && !f.isDirectory()) { 
                  String inside = "";
                  String file = "sharedpool.txt";
                  Scanner scanner = new Scanner(new File(file));
                  while (scanner.hasNext()){
                      System.out.println(scanner.nextLine());
                  }  
              }else{
                  System.out.println("file sharedpool belum ada");
              }
            break;
         case 6 :
            System.exit(0);
         default :
            System.out.println("Pilihan yang anda masukkan salah");
        }
        
    }
    
}
