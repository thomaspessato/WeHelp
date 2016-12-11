package com.wehelp.wehelp.classes;

import java.io.Serializable;

/**
 * Created by Rael on 11/12/2016.
 */

public class UserRequirement implements Serializable {
    private int id;
    private String email;
    private  int pessoa_id;
    private int ong_id;
    private double quant;
    private String un;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPessoaId() {
        return pessoa_id;
    }

    public void setPessoaId(int pessoa_id) {
        this.pessoa_id = pessoa_id;
    }

    public int getOngId() {
        return ong_id;
    }

    public void setOngId(int ong_id) {
        this.ong_id = ong_id;
    }

    public double getQuant() {
        return quant;
    }

    public void setQuant(double quant) {
        this.quant = quant;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }
}
