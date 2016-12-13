package com.wehelp.wehelp.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class EventRequirement implements Serializable {
    private Date created_at;
    private Date updated_at;
    private int evento_id;
    private int id;
    private String descricao;
    private String un;
    private double quant;
    boolean selected = false;
    private double selectedQuant;
    private double quantidadeFaltante;
    private ArrayList<UserRequirement> usuarios_requisito;



    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public String getUn() { return un; }

    public void setUn(String un) { this.un = un; }

    public double getQuant() { return quant; }

    public void setQuant(double quant) { this.quant = quant; }

    public double getSelectedQuant() { return selectedQuant; }

    public void setSelectedQuant(double selectedQuant ) { this.selectedQuant = selectedQuant ; }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getEventoId() {
        return evento_id;
    }

    public void setEventoId(int evento_id) {
        this.evento_id = evento_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getQuantidadeFaltante() {
        return quantidadeFaltante;
    }

    public void setQuantidadeFaltante(double quantidadeFaltante) {
        this.quantidadeFaltante = quantidadeFaltante;
    }

    public ArrayList<UserRequirement> getUsuariosRequisito() {
        return usuarios_requisito;
    }

    public void setUsuariosRequisito(ArrayList<UserRequirement> usuarios_requisito) {
        this.usuarios_requisito = usuarios_requisito;
    }
}
