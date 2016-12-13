package com.wehelp.wehelp.classes;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rael on 29/10/2016.
 */
public class User implements Serializable{
    private int id;
    private String email;
    private int pessoa_id;
    private int ong_id;
    private Date created_at;
    private Date updated_at;
    private Person pessoa;
    private Ong ong;
    private String password;
    private String nome;


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

    public void setPessoaId(int pessoaId) {
        this.pessoa_id = pessoaId;
    }

    public int getOngId() {
        return ong_id;
    }

    public void setOngId(int ongId) {
        this.ong_id = ongId;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date createdAt) {
        this.created_at = createdAt;
    }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updated_at = updatedAt;
    }

    public Person getPessoa() {
        return pessoa;
    }

    public void setPessoa(Person person) {
        this.pessoa = person;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    public String getPassword() {
        return password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
