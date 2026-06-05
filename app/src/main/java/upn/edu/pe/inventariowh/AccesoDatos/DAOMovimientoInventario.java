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

    SQLiteOpenHelper helper;

    public DAOMovimientoInventario(Activity contexto) {
        helper = new OpenHelperDB(contexto, "WESTHAMDB", null, 2);
    }

    public boolean Insertar(MovimientoInventario m) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Codigo", m.getCodigo());
        cv.put("Tipo", m.getTipo());
        cv.put("IdProducto", m.getIdProducto());
        cv.put("Cantidad", m.getCantidad());
        cv.put("Fecha", m.getFecha());
        cv.put("Monto", m.getMonto());
        cv.put("Observacion", m.getObservacion());

        long r = db.insert("MovimientoInventario", null, cv);
        db.close();

        return r > 0;
    }

    public List<MovimientoInventario> ListarTodos() {

        List<MovimientoInventario> lista = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM MovimientoInventario ORDER BY IdMovimiento DESC",
                null
        );

        if (c.moveToFirst()) {
            do {
                lista.add(mapear(c));
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return lista;
    }

    public MovimientoInventario Buscar(int id) {

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM MovimientoInventario WHERE IdMovimiento=?",
                new String[]{String.valueOf(id)}
        );

        MovimientoInventario m = null;

        if (c.moveToFirst()) {
            m = mapear(c);
        }

        c.close();
        db.close();

        return m;
    }

    public boolean Actualizar(MovimientoInventario m) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Codigo", m.getCodigo());
        cv.put("Tipo", m.getTipo());
        cv.put("IdProducto", m.getIdProducto());
        cv.put("Cantidad", m.getCantidad());
        cv.put("Fecha", m.getFecha());
        cv.put("Monto", m.getMonto());
        cv.put("Observacion", m.getObservacion());

        int r = db.update(
                "MovimientoInventario",
                cv,
                "IdMovimiento=?",
                new String[]{String.valueOf(m.getIdMovimiento())}
        );

        db.close();

        return r > 0;
    }

    public boolean Eliminar(int id) {

        SQLiteDatabase db = helper.getWritableDatabase();

        int r = db.delete(
                "MovimientoInventario",
                "IdMovimiento=?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return r > 0;
    }

    private MovimientoInventario mapear(Cursor c) {

        MovimientoInventario m = new MovimientoInventario();

        m.setIdMovimiento(c.getInt(c.getColumnIndexOrThrow("IdMovimiento")));
        m.setCodigo(c.getString(c.getColumnIndexOrThrow("Codigo")));
        m.setTipo(c.getString(c.getColumnIndexOrThrow("Tipo")));
        m.setIdProducto(c.getInt(c.getColumnIndexOrThrow("IdProducto")));
        m.setCantidad(c.getInt(c.getColumnIndexOrThrow("Cantidad")));
        m.setFecha(c.getLong(c.getColumnIndexOrThrow("Fecha")));
        m.setMonto(c.getDouble(c.getColumnIndexOrThrow("Monto")));
        m.setObservacion(c.getString(c.getColumnIndexOrThrow("Observacion")));

        return m;
    }
}