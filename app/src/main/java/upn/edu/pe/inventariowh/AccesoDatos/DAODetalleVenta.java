package upn.edu.pe.inventariowh.AccesoDatos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.Modelos.DetalleVenta;

public class DAODetalleVenta {

    private String nombreBD;
    private int version;
    private Activity contexto;

    SQLiteOpenHelper oHelper;

    public DAODetalleVenta(Activity contexto) {

        this.nombreBD = "WESTHAMDB";
        this.version = 2;
        this.contexto = contexto;

        oHelper = new OpenHelperDB(contexto, nombreBD, null, version);
    }

    public boolean Insertar(DetalleVenta oDV) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("IdVenta", oDV.getIdVenta());
        oColumna.put("IdProducto", oDV.getIdProducto());
        oColumna.put("Cantidad", oDV.getCantidad());
        oColumna.put("PrecioUnitario", oDV.getPrecioUnitario());
        oColumna.put("Subtotal", oDV.getSubtotal());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        long fila =
                db.insert("DetalleVenta",
                        null,
                        oColumna);

        db.close();

        return fila > 0;
    }

    public List<DetalleVenta> ListarTodos() {

        List<DetalleVenta> lista = new ArrayList<>();

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM DetalleVenta", null);

        if (oRegistros.moveToFirst()) {

            do {

                lista.add(mapearRegistro(oRegistros));

            } while (oRegistros.moveToNext());
        }

        oRegistros.close();
        db.close();

        return lista;
    }

    public DetalleVenta Buscar(int idDetalleVenta) {

        DetalleVenta oDV = null;

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM DetalleVenta WHERE IdDetalleVenta=?",
                        new String[]{String.valueOf(idDetalleVenta)});

        if (oRegistros.moveToFirst()) {

            oDV = mapearRegistro(oRegistros);
        }

        oRegistros.close();
        db.close();

        return oDV;
    }

    public boolean Actualizar(DetalleVenta oDV) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("IdVenta", oDV.getIdVenta());
        oColumna.put("IdProducto", oDV.getIdProducto());
        oColumna.put("Cantidad", oDV.getCantidad());
        oColumna.put("PrecioUnitario", oDV.getPrecioUnitario());
        oColumna.put("Subtotal", oDV.getSubtotal());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =
                db.update("DetalleVenta",
                        oColumna,
                        "IdDetalleVenta=?",
                        new String[]{String.valueOf(oDV.getIdDetalleVenta())});

        db.close();

        return filas > 0;
    }

    public boolean Eliminar(int idDetalleVenta) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =
                db.delete("DetalleVenta",
                        "IdDetalleVenta=?",
                        new String[]{String.valueOf(idDetalleVenta)});

        db.close();

        return filas > 0;
    }

    private DetalleVenta mapearRegistro(Cursor oRegistros) {

        int idDetalleVenta =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdDetalleVenta"));

        int idVenta =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdVenta"));

        int idProducto =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdProducto"));

        int cantidad =oRegistros.getInt( oRegistros.getColumnIndexOrThrow("Cantidad"));

        double precioUnitario =oRegistros.getDouble(oRegistros.getColumnIndexOrThrow("PrecioUnitario"));

        double subtotal =oRegistros.getDouble(oRegistros.getColumnIndexOrThrow("Subtotal"));

        return new DetalleVenta(idDetalleVenta,idVenta,idProducto,cantidad,precioUnitario,subtotal);
    }
}