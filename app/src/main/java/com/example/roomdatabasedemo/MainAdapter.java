package com.example.roomdatabasedemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabasedemo.Database.DBAccess;
import com.example.roomdatabasedemo.Entities.Product;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    // Declarate variable
    private List<ListRow> dataList;
    private LayoutInflater mInflater;
    private Context context;
    private DBAccess access;

    final MainAdapter.OnItemClickListener listenerEdit, listenerDelete;

    // Interfaz onclick elementos
    public interface OnItemClickListener {
        void onItemClick(ListRow listRow);
    }

    // Constructor
    public MainAdapter(Context context, List<ListRow> dataList, DBAccess access, MainAdapter.OnItemClickListener listenerEdit, MainAdapter.OnItemClickListener listenerDelete){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.dataList = dataList;

        //this.access = access;
        this.listenerEdit = listenerEdit;
        this.listenerDelete = listenerDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize view
        View view = mInflater.inflate(R.layout.list_row_main, null);
        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(dataList.get(position));
        //holder.setOnClickListener(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        // Obtener el tamaño de registros
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Declare/Initialize variable
        TextView textView;
        ImageView btEdit, btDelete;
        int idproduct = 0;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Assign variable
            textView = itemView.findViewById(R.id.text_view);
            btEdit = itemView.findViewById(R.id.bt_edit);
            btDelete = itemView.findViewById(R.id.bt_delete);
        }

        // Los botones estaran en escucha del evento click
        public void setOnClickListener(ListRow listRow){
            textView.setText(listRow.getNameproduct());
            idproduct = listRow.getIdproduct();

            btEdit.setOnClickListener(this);
            btDelete.setOnClickListener(this);
        }

        // Listener en escucha del evento click de los botones
        public void bindData(final ListRow listRow) {
            // Set text on text view
            textView.setText(listRow.getNameproduct());
            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerEdit.onItemClick(listRow);
                }
            });

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerDelete.onItemClick(listRow);
                }
            });
        }

        /**
         * Evento click, de los btotones de cada elemento del recycle view
         * @param view
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bt_edit:
                    //Toast.makeText(context, "Id: " + idproduct, Toast.LENGTH_SHORT).show();
                    String name = textView.getText().toString();
                    editProduct(idproduct, name);
                    break;
                case R.id.bt_delete:
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                    break;

            }
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

                    // Cerrar dialog
                    updateRow(idproduct, editText.getText().toString());
                    dialog.dismiss();

                    //dataList.clear();
                    notifyDataSetChanged();

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
            }

            // Cerrar la conexión
            db.close();
        }

    }


}