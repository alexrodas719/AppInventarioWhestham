package upn.edu.pe.inventariowh.AccesoDatos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.Modelos.MovimientoInventario;

public class DAOMovimientoInventario {

    private String nombreBD;
    private int version;
    private Activity contexto;

    SQLiteOpenHelper oHelper;

    public DAOMovimientoInventario(Activity contexto) {

        this.nombreBD = "WESTHAMDB";
        this.version = 1;
        this.contexto = contexto;

        oHelper = new OpenHelperDB(contexto, nombreBD, null, version);
    }

    // =========================
    // INSERTAR
    // =========================
    public boolean Insertar(MovimientoInventario oM) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("Codigo", oM.getCodigo());
        oColumna.put("Tipo", oM.getTipo());
        oColumna.put("IdProducto", oM.getIdProducto());
        oColumna.put("Cantidad", oM.getCantidad());
        oColumna.put("Fecha", oM.getFecha());
        oColumna.put("Monto", oM.getMonto());
        oColumna.put("Observacion", oM.getObservacion());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        long fila =
                db.insert("MovimientoInventario",
                        null,
                        oColumna);

        db.close();

        return fila > 0;
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<MovimientoInventario> ListarTodos() {

        List<MovimientoInventario> lista = new ArrayList<>();

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM MovimientoInventario", null);

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
    public MovimientoInventario Buscar(int idMovimiento) {

        MovimientoInventario oM = null;

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM MovimientoInventario WHERE IdMovimiento=?",
                        new String[]{String.valueOf(idMovimiento)});

        if (oRegistros.moveToFirst()) {

            oM = mapearRegistro(oRegistros);
        }

        oRegistros.close();
        db.close();

        return oM;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean Actualizar(MovimientoInventario oM) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("Codigo", oM.getCodigo());
        oColumna.put("Tipo", oM.getTipo());
        oColumna.put("IdProducto", oM.getIdProducto());
        oColumna.put("Cantidad", oM.getCantidad());
        oColumna.put("Fecha", oM.getFecha());
        oColumna.put("Monto", oM.getMonto());
        oColumna.put("Observacion", oM.getObservacion());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =
                db.update("MovimientoInventario",
                        oColumna,
                        "IdMovimiento=?",
                        new String[]{String.valueOf(oM.getIdMovimiento())});

        db.close();

        return filas > 0;
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean Eliminar(int idMovimiento) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =
                db.delete("MovimientoInventario",
                        "IdMovimiento=?",
                        new String[]{String.valueOf(idMovimiento)});

        db.close();

        return filas > 0;
    }

    // =========================
    // MAPEAR REGISTRO
    // =========================
    private MovimientoInventario mapearRegistro(Cursor oRegistros) {

        int idMovimiento =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdMovimiento"));

        String codigo =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Codigo"));

        String tipo =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Tipo"));

        int idProducto =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdProducto"));

        int cantidad =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("Cantidad"));

        long fecha =oRegistros.getLong(oRegistros.getColumnIndexOrThrow("Fecha"));

        double monto =oRegistros.getDouble(oRegistros.getColumnIndexOrThrow("Monto"));

        String observacion =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Observacion"));

        return new MovimientoInventario(idMovimiento,codigo,tipo,idProducto,cantidad,fecha,monto,observacion);
    }
}