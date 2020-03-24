/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PUTRI
 */
public class coba {
    public static void main(String[] args) {
        int[][] rray = new int[4][];
        int i = 0;
        while (i < 3) {
            rray[1][i] = 2 * (i + 1) ;
            System.out.print(rray[1][i] +" ");
            i++;
        }
        
        i = 0;
        for (int j = 0; j <= 14; j++) {
            if (j % 2 == 1){
                rray[2][i] = j;
                System.out.print(rray[2][i]+" ");
                i++;
            }
        }
        
        rray[3][0] = 0;
        System.out.println(rray[2][0]);
       
        for (int j = 0; j < 10; j++) {
            rray[3][j] = j + 1;
            System.out.println(rray[3][j]+" ");
        }
       
    }
    
}
