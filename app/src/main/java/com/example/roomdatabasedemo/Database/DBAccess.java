package com.example.roomdatabasedemo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.roomdatabasedemo.Entities.Product;

public class DBAccess extends SQLiteOpenHelper {

    // CONSTANTES
    private static final String NAME_DB = "PRODUCTDB";
    private static final int VERSION_DB = 1;
    private static final String TABLE_PRODUCTS = "CREATE TABLE products (idproduct INTEGER NOT NULL, nameproduct TEXT NOT NULL, PRIMARY KEY(idproduct AUTOINCREMENT))";

    public DBAccess(@Nullable Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    // CREACIÓN DE LA BASE DE DATOS
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_PRODUCTS);
    }

    // ACTUALIZADO DE TABLA
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS products");
        sqLiteDatabase.execSQL(TABLE_PRODUCTS);
    }

    // MÉTODO PARA INSERTAR DATOS
    public long registerProduct(Product product){
        // Instancia de objeto que permite la escritura en la BD
        SQLiteDatabase db = getWritableDatabase();

        // Almacenar ID generado al realizar el registro
        long idproduct = 0;

        // Validate access
        if(db != null){
            // Instancia del objeto que contiene los datos a enviar a la TABLA
            ContentValues contentValues = new ContentValues();

            // DATOS ALMACENADOS POR CLAVE : VALOR
            contentValues.put("nameproduct", product.getNameproduct());

            // OPCIONALMENTE OBTENER EL ID PK, GENRADO
            idproduct = db.insert("products", "idproduct", contentValues);

            Log.i("info", "ID-GENERADO: " + idproduct);

            // Cerrar conexión con la base de datos
            db.close();
        }

        // Retornar id generado
        return idproduct;
    }
}
