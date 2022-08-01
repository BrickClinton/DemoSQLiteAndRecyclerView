package com.example.roomdatabasedemo;

import java.io.Serializable;

public class ListRow implements Serializable {
    private int idproduct;
    private String nameproduct;

    public ListRow() {
    }

    public ListRow(int idproduct, String nameproduct) {
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
        return "ListRow{" +
                ", idproduct=" + idproduct +
                ", nameproduct='" + nameproduct + '\'' +
                '}';
    }
}
