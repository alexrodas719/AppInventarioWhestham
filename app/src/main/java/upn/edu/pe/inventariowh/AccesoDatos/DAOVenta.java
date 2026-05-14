package upn.edu.pe.inventariowh.AccesoDatos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.Modelos.Venta;

public class DAOVenta {

    private String nombreBD;
    private int version;
    private Activity contexto;

    SQLiteOpenHelper oHelper;

    public DAOVenta(Activity contexto) {

        this.nombreBD = "WESTHAMDB";
        this.version = 1;
        this.contexto = contexto;

        oHelper = new OpenHelperDB(contexto, nombreBD, null, version);
    }

    // =========================
    // INSERTAR
    // =========================
    public boolean Insertar(Venta oV) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("Codigo", oV.getCodigo());
        oColumna.put("Fecha", oV.getFecha());
        oColumna.put("Subtotal", oV.getSubtotal());
        oColumna.put("Descuento", oV.getDescuento());
        oColumna.put("Total", oV.getTotal());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        long fila =db.insert("Venta",null,oColumna);
        db.close();
        return fila > 0;
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Venta> ListarTodos() {

        List<Venta> lista = new ArrayList<>();

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM Venta", null);

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
    public Venta Buscar(int idVenta) {

        Venta oV = null;

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM Venta WHERE IdVenta=?",
                        new String[]{String.valueOf(idVenta)});

        if (oRegistros.moveToFirst()) {

            oV = mapearRegistro(oRegistros);
        }

        oRegistros.close();
        db.close();

        return oV;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean Actualizar(Venta oV) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("Codigo", oV.getCodigo());
        oColumna.put("Fecha", oV.getFecha());
        oColumna.put("Subtotal", oV.getSubtotal());
        oColumna.put("Descuento", oV.getDescuento());
        oColumna.put("Total", oV.getTotal());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =
                db.update("Venta",
                        oColumna,
                        "IdVenta=?",
                        new String[]{String.valueOf(oV.getIdVenta())});

        db.close();

        return filas > 0;
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean Eliminar(int idVenta) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =
                db.delete("Venta",
                        "IdVenta=?",
                        new String[]{String.valueOf(idVenta)});

        db.close();

        return filas > 0;
    }

    // =========================
    // MAPEAR REGISTRO
    // =========================
    private Venta mapearRegistro(Cursor oRegistros) {

        int idVenta =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdVenta"));

        String codigo =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Codigo"));

        long fecha =oRegistros.getLong(oRegistros.getColumnIndexOrThrow("Fecha"));

        double subtotal =oRegistros.getDouble(oRegistros.getColumnIndexOrThrow("Subtotal"));

        double descuento =oRegistros.getDouble(oRegistros.getColumnIndexOrThrow("Descuento"));

        double total =oRegistros.getDouble(oRegistros.getColumnIndexOrThrow("Total"));

        return new Venta(idVenta,codigo,fecha,subtotal,descuento,total);
    }
}