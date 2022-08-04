package com.example.roomdatabasedemo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.roomdatabasedemo.Entities.Product;
import com.example.roomdatabasedemo.ListRow;

import java.util.ArrayList;

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

        onCreate(sqLiteDatabase);
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

    // Consultas a la base de datos
    public ArrayList<Product> selectAllData(){
        // Solicitar acceso de tipo lectura
        SQLiteDatabase db = getReadableDatabase();

        // Instancia producto
        Product product;

        // Instanciado el arraylist
        ArrayList<Product> dataList = new ArrayList<>();

        // Desencadenar la consulta y traer los datos
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        // Obteniendo los datos obtenidos en la consulta anterior
        while (cursor.moveToNext()){
            // Instanciando la entidad
            product = new Product();

            // Asignar los datos del cursos a la ENTIDAD
            product.setIdproduct(cursor.getInt(0));
            product.setNameproduct(cursor.getString(1));

            // Añadiendo el objeto al array
            dataList.add(product);
        }

        return dataList;
    }

}
