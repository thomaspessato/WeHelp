package com.wehelp.wehelp.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Serializable {

    private int id;
    private int categoria_id;
    private int usuario_id;
    private String pais;
    private String uf;
    private String cidade;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private Double lat;
    private Double lng;
    private String descricao;
    private Date data_inicio;
    private Date data_fim;
    private int ranking;
    private String status;
    private boolean certificado;
    private Date created_at;
    private Date updated_at;
    private User usuario;
    private Category categoria;
    private ArrayList<EventRequirement> requisitos;
    private String cep;
    private String nome;
    private int numero_participantes;
    private boolean isParticipating;
    private boolean isComplete;
    private ArrayList<User> participantes;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuario_id;
    }

    public void setUsuarioId(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getCategoriaId() {
        return categoria_id;
    }

    public void setCategoriaId(int categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return data_inicio;
    }

    public void setDataInicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    public Date getDataFim() {
        return data_fim;
    }

    public void setDataFim(Date data_fim) {
        this.data_fim = data_fim;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public boolean isParticipating() {
        return isParticipating;
    }

    public void setParticipating(boolean participating) {
        isParticipating = participating;
    }

    public Category getCategoria() {
        return categoria;
    }

    public void setCategoria(Category categoria) {
        this.categoria = categoria;
    }


    public ArrayList<EventRequirement> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(ArrayList<EventRequirement> requisitos) {
        this.requisitos = requisitos;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public ArrayList<User> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<User> participantes) {
        this.participantes = participantes;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroParticipantes() {
        return numero_participantes;
    }

    public void setNumeroParticipantes(int numero_participantes) {
        this.numero_participantes = numero_participantes;
    }
}
