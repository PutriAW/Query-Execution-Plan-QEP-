/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qepcomplete;

/**
 *
 * @author PUTRI
 */
public class qepInfo {
    private String qep;
    private double cost;

    public qepInfo(String qep, double cost) {
        this.qep = qep;
        this.cost = cost;
    }

    public String getQep() {
        return qep;
    }

    public double getCost() {
        return cost;
    }
    
}
