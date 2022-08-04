package com.example.roomdatabasedemo.Entities;

import java.io.Serializable;

public class Product implements Serializable {
    private int idproduct;
    private String nameproduct;

    // Constructor default
    public Product() {
    }

    // Constructor con parametros de entradas
    public Product(int idproduct, String nameproduct) {
        this.idproduct = idproduct;
        this.nameproduct = nameproduct;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idproduct=" + idproduct +
                ", nameproduct='" + nameproduct + '\'' +
                '}';
    }
}
