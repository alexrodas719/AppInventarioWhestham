package upn.edu.pe.inventariowh.AccesoDatos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.Modelos.Producto;

public class DAOProducto {

    private String nombreBD;
    private int version;
    private Activity contexto;

    SQLiteOpenHelper oHelper;

    public DAOProducto(Activity contexto) {

        this.nombreBD ="WESTHAMDB";
        this.version = 1;
        this.contexto = contexto;

        oHelper = new OpenHelperDB(contexto, nombreBD, null, version);
    }

    // =========================
    // INSERTAR
    // =========================
    public boolean Insertar(Producto oP) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("Nombre", oP.getNombre());
        oColumna.put("Foto", oP.getFoto());
        oColumna.put("SKU", oP.getSku());
        oColumna.put("IdCategoria", oP.getIdCategoria());
        oColumna.put("Talla", oP.getTalla());
        oColumna.put("Color", oP.getColor());
        oColumna.put("Stock", oP.getStock());
        oColumna.put("Precio", oP.getPrecio());
        oColumna.put("Descripcion", oP.getDescripcion());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        long fila = db.insert("Producto", null, oColumna);
        db.close();
        return fila > 0;
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Producto> ListarTodos() {

        List<Producto> lista = new ArrayList<>();

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM Producto", null);

        if (oRegistros.moveToFirst()) {

            do {

                lista.add(mapearRegistro(oRegistros));

            } while (oRegistros.moveToNext());
        }

        oRegistros.close();
        db.close();

        return lista;
    }

    // =========================
    // BUSCAR
    // =========================
    public Producto Buscar(int idProducto) {

        Producto oP = null;

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM Producto WHERE IdProducto=?",
                        new String[]{String.valueOf(idProducto)});

        if (oRegistros.moveToFirst()) {

            oP = mapearRegistro(oRegistros);
        }

        oRegistros.close();
        db.close();

        return oP;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean Actualizar(Producto oP) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("Nombre", oP.getNombre());
        oColumna.put("Foto", oP.getFoto());
        oColumna.put("SKU", oP.getSku());
        oColumna.put("IdCategoria", oP.getIdCategoria());
        oColumna.put("Talla", oP.getTalla());
        oColumna.put("Color", oP.getColor());
        oColumna.put("Stock", oP.getStock());
        oColumna.put("Precio", oP.getPrecio());
        oColumna.put("Descripcion", oP.getDescripcion());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =
                db.update("Producto",
                        oColumna,
                        "IdProducto=?",
                        new String[]{String.valueOf(oP.getIdProducto())});

        db.close();

        return filas > 0;
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean Eliminar(int idProducto) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =
                db.delete("Producto",
                        "IdProducto=?",
                        new String[]{String.valueOf(idProducto)});

        db.close();

        return filas > 0;
    }

    // =========================
    // MAPEAR REGISTRO
    // =========================
    private Producto mapearRegistro(Cursor oRegistros) {

        int idProducto =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdProducto"));

        String nombre =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Nombre"));

        String foto =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Foto"));

        String sku =oRegistros.getString(oRegistros.getColumnIndexOrThrow("SKU"));

        int idCategoria =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdCategoria"));

        String talla =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Talla"));

        String color =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Color"));

        int stock =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("Stock"));

        double precio =oRegistros.getDouble(oRegistros.getColumnIndexOrThrow("Precio"));

        String descripcion =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Descripcion"));

        return new Producto(idProducto,nombre,foto,sku,idCategoria,talla,color,stock,precio,descripcion);
    }
}