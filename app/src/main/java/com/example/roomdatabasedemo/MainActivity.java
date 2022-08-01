package com.example.roomdatabasedemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.roomdatabasedemo.Database.DBAccess;
import com.example.roomdatabasedemo.Entities.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaración de variables
    private EditText editText;
    private Button btnAdd, btnReset, btnEdit, btnDelete;
    private RecyclerView recyclerView;

    private ArrayList<ListRow> dataList;
    private ArrayList<String> dataShow;

    LinearLayoutManager linearLayoutManager;
    Context context = this;
    DBAccess access;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inicializar UI
        initUI();

        // Iniciar acceso a la base de datos
        access = new DBAccess(context);

        // Cargar datos en el array
        queryData();



        // Set layout manager
        loadData();
        //testData();

        // Listener onclick
        btnAdd.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }

    // Datos de prueba
    public String searchProductById(int idproduct){

        String nameproduct = "";

        // Objeto que nos brinda acceso a la bd
        SQLiteDatabase db = access.getReadableDatabase();

        // Campos usados para la busqueda
        String[] paramConsult = {String.valueOf(idproduct)};

        // Campos a obtener (segun el orden indicado)
        String[] getFields = {"nameproduct"};

        // Control de excepciones
        try{
            // Ejecutar y obtener los datos
            Cursor cursor = db.query("products", getFields, "idproduct=?", paramConsult, null, null, null);

            // Se encontró registro coincidente
            if(cursor.moveToFirst()){
                // Enviando los datos a las cajas de textos
                nameproduct = cursor.getString(0);
            }
            else{
                // No se encontrarón registros
                Toast.makeText(context, "No existe el registro buscado", Toast.LENGTH_SHORT).show();
            }

            // Cerrar conexión
            cursor.close();
        } catch(Exception error){
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
        }

        return nameproduct;
    }


    // Cargar datos en el cardview
    private void loadData(){
        // Set layout manager
        recyclerView.setHasFixedSize(true);

        // Initialize liner layout
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Initialize adapter
        adapter = new MainAdapter(this, dataList, access, new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListRow listRow) {
                // Editar datos del producto
                editProduct(listRow.getIdproduct(), listRow.getNameproduct());
            }
        },
        new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListRow listRow) {
                // Eliminar producto
                validateDeletion(listRow.getIdproduct());
            }
        });


        // Set adapter
        recyclerView.setAdapter(adapter);
    }

    // Initialize ui
    private void initUI(){
        editText = findViewById(R.id.add_text);
        btnAdd = findViewById(R.id.bt_add);
        btnReset = findViewById(R.id.bt_reset);
        recyclerView = findViewById(R.id.recycler_view);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_add:
                validateRegister();
                break;
            case R.id.bt_reset:
                resetData();
                break;
            default:
                Toast.makeText(context, "Toast default", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // Consultas a la base de datos
    private void queryData(){
        // Solicitar acceso de tipo lectura
        SQLiteDatabase db = access.getReadableDatabase();

        // Instancia producto
        ListRow product;

        // Instanciado el arraylist
        dataList = new ArrayList<>();

        // Desencadenar la consulta y traer los datos
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        // Obteniendo los datos obtenidos en la consulta anterior
        while (cursor.moveToNext()){
            // Instanciando la entidad
            product = new ListRow();

            // Asignar los datos del cursos a la ENTIDAD
            product.setIdproduct(cursor.getInt(0));
            product.setNameproduct(cursor.getString(1));

            // Añadiendo el objeto al array
            dataList.add(product);
        }

    }

    /**
     * Esté método pasa loas datos del dataList al dataShow
     * Array de tipo string, necesario para el adapter
     */
    private void editDataListRow(){
        // Obtain ID and NAME
        int idproduct = 2;
        String nameproduct = "";

        // Create dialog
        Dialog dialog = new Dialog(context);

        // Agregar contexto
        dialog.setContentView(R.layout.dialog_update);

        // Width and Height
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Asign layout
        dialog.getWindow().setLayout(width, height);

        // Shoe dialog
        dialog.show();

        // Initialize and assign variable
        EditText editText = dialog.findViewById(R.id.edit_text);
        Button btnUpdate = dialog.findViewById(R.id.bt_update);

        editText.setText(nameproduct);

        // Listener click
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar dialog
                dialog.dismiss();

                //
            }
        });
    }

    private void validateRegister(){

        if(editText.getText().toString().isEmpty()){
            // No hay datos
            Toast.makeText(context, "Complete los datos", Toast.LENGTH_SHORT).show();
        } else {
            // Creando objeto dialog
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            dialog.setTitle("REGISTER")
                    .setMessage("¿Está seguro de registrar el producto?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addData();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Null
                        }
                    });

            // Mostrar dialogo
            dialog.show();
        }
    }

    /**
     * Insertar nuevos registros a la base de datos SQLite
     */
    private void addData(){
        // Instancia producto
        Product product = new Product();

        product.setNameproduct(editText.getText().toString());

        // procediendo a registrar
        long status = access.registerProduct(product);

        if(status > 0){
            Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_LONG).show();
            resetData();
            refreshDataList();
        } else {
            Toast.makeText(context, "ocurrio un problemaal registrar", Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Refrescar datos en la lista de elementos
     */
    private void refreshDataList(){
        queryData();
        loadData();
    }

    /**
     * Limpiar controles
     */
    private void resetData(){
        editText.setText(null);
    }

    /**
     * Editar datos del registro
     */
    private void editProduct(int idproduct, String nameproduct){
        //Toast.makeText(context, "Message" + listRow.getIdproduct(), Toast.LENGTH_LONG).show();
        // Create dialog
        Dialog dialog = new Dialog(context);

        // Agregar contexto
        dialog.setContentView(R.layout.dialog_update);

        // Width and Height
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Asign layout
        dialog.getWindow().setLayout(width, height);
        dialog.setTitle("Actualizar producto");

        // Shoe dialog
        dialog.show();

        // Initialize and assign variable
        EditText editText = dialog.findViewById(R.id.edit_text);
        Button btnUpdate = dialog.findViewById(R.id.bt_update);
        Button btnClose = dialog.findViewById(R.id.bt_close);

        editText.setText(nameproduct);


        // Listener click
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validar nombre del producto
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(context, "No existen datos por actualizar o están incompleto", Toast.LENGTH_LONG).show();
                } else {
                    // Cerrar dialog
                    dialog.dismiss();
                    validateUpdate(idproduct, editText.getText().toString().trim());
                }

            }
        });

        // Cerrar modal
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    /**
     * Este método valida si existe datos en el activity Busqueda.
     * Actualiza en caso exista datos, de lo contrario Muestra un mensaje de aviso
     */
    private void validateUpdate(int idproduct, String nameproduct){
        // Creando el objeto de dialogo
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        // Configurar los datos del dialog
        dialog.setTitle("PRODUCTOS")
                .setMessage("¿Estas seguro de actualizar el registro?")
                .setCancelable(false)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateRow(idproduct, nameproduct); // Actualizar
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        // Mostrar dialogo
        dialog.show();
    }


    /**
     * Este método permite actualizar los datos del registro buscado
     */
    private void updateRow(int idproduct, String nameproduct){
        // Objeto que permite la esritura de datos
        SQLiteDatabase db = access.getWritableDatabase();

        // Parametros
        String[] parameters = {String.valueOf(idproduct)};

        // Contenido de los valores a actualizar
        ContentValues values = new ContentValues();
        values.put("nameproduct", nameproduct);

        int value = db.update("products", values, "idproduct=?", parameters);

        // Validar si se pudo actualizar el registro
        if(value > 0){
            Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
            refreshDataList();
        }

        // Cerrar la conexión
        db.close();
    }

    /**
     * Este método valida si existe datos en el activity Busqueda.
     * Elimina en caso exista datos de lo contrario Muestra un mensaje de aviso
     */
    private void validateDeletion(int idproduct){
        // Crear instancia del dialogo
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        // Configurar contenido del dialogo
        dialog.setTitle("PRODUCTOS")
                .setMessage("¿Estas seguro de eliminar el registro?")
                .setCancelable(false)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteRow(idproduct); // Eliminar
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // No se hace nada
                    }
                });

        // Mostrar dialogo
        dialog.show();
    }

    /**
     * Este método contiene la logica para eliminar un registro
     */
    private void deleteRow(int idproduct){
        // Obteniendo acceso de escritura
        SQLiteDatabase db = access.getWritableDatabase();

        // Definir array con los parametros de entrada
        String[] parameters = {String.valueOf(idproduct)};

        // Ejecutar proceso
        int value = db.delete("products", "idproduct=?", parameters);

        // Confirmar eliminación
        if(value > 0){
            Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            refreshDataList();
        }else{
            Toast.makeText(context, "No se pudo eliminar el registro", Toast.LENGTH_SHORT).show();
        }

        // Cerrar la conexión
        db.close();
    }

}