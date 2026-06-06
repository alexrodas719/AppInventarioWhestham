package upn.edu.pe.inventariowh.AccesoDatos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.Modelos.Producto;

public class DAOProducto {

    private String nombreBD;
    private int version;
    private Activity contexto;

    SQLiteOpenHelper oHelper;

    public DAOProducto(Activity contexto) {

        this.nombreBD = "WESTHAMDB";
        this.version = 2;
        this.contexto = contexto;

        oHelper = new OpenHelperDB(contexto, nombreBD, null, version);
    }

    public boolean Insertar(Producto oP) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        ContentValues oColumna = new ContentValues();
        oColumna.put("Nombre", oP.getNombre());
        oColumna.put("Foto", oP.getFoto());
        oColumna.put("SKU", oP.getSku());
        oColumna.put("IdCategoria", oP.getIdCategoria());
        oColumna.put("Talla", oP.getTalla());
        oColumna.put("Color", oP.getColor());
        oColumna.put("Stock", oP.getStock());
        oColumna.put("PrecioCompra", oP.getPrecioCompra());
        oColumna.put("PrecioVenta", oP.getPrecioVenta());
        oColumna.put("Descripcion", oP.getDescripcion());

        long fila = db.insert("Producto", null, oColumna);

        if (fila == -1) {
            Log.e("DB_INSERT", " Error al insertar producto (posible SKU duplicado)");
            db.close();
            return false;
        }

        db.close();
        return true;
    }

    public List<Producto> ListarTodos() {

        List<Producto> lista = new ArrayList<>();

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Producto", null);

        if (c.moveToFirst()) {
            do {
                lista.add(mapearRegistro(c));
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return lista;
    }

    public Producto Buscar(int idProducto) {

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM Producto WHERE IdProducto=?",
                new String[]{String.valueOf(idProducto)}
        );

        Producto p = null;

        if (c.moveToFirst()) {
            p = mapearRegistro(c);
        }

        c.close();
        db.close();

        return p;
    }

    public boolean Actualizar(Producto p) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Nombre", p.getNombre());
        cv.put("Foto", p.getFoto());
        cv.put("SKU", p.getSku());
        cv.put("IdCategoria", p.getIdCategoria());
        cv.put("Talla", p.getTalla());
        cv.put("Color", p.getColor());
        cv.put("Stock", p.getStock());
        cv.put("PrecioCompra", p.getPrecioCompra());
        cv.put("PrecioVenta", p.getPrecioVenta());
        cv.put("Descripcion", p.getDescripcion());

        int filas = db.update(
                "Producto",
                cv,
                "IdProducto=?",
                new String[]{String.valueOf(p.getIdProducto())}
        );

        db.close();
        return filas > 0;
    }

    public boolean Eliminar(int idProducto) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas = db.delete(
                "Producto",
                "IdProducto=?",
                new String[]{String.valueOf(idProducto)}
        );

        db.close();
        return filas > 0;
    }

    public List<Producto> Filtrar(String texto) {
        List<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM Producto WHERE Nombre LIKE ? OR SKU LIKE ?",
                new String[]{"%" + texto + "%", "%" + texto + "%"}
        );

        if (c.moveToFirst()) {
            do {
                lista.add(mapearRegistro(c));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return lista;
    }
    public boolean ActualizarStock(int idProducto, int nuevoStock) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Stock", nuevoStock);

        int filas = db.update(
                "Producto",
                cv,
                "IdProducto=?",
                new String[]{String.valueOf(idProducto)}
        );

        db.close();
        return filas > 0;
    }
    private Producto mapearRegistro(Cursor c) {

        return new Producto(
                c.getInt(c.getColumnIndexOrThrow("IdProducto")),
                c.getString(c.getColumnIndexOrThrow("Nombre")),
                c.getBlob(c.getColumnIndexOrThrow("Foto")),
                c.getString(c.getColumnIndexOrThrow("SKU")),
                c.getInt(c.getColumnIndexOrThrow("IdCategoria")),
                c.getString(c.getColumnIndexOrThrow("Talla")),
                c.getString(c.getColumnIndexOrThrow("Color")),
                c.getInt(c.getColumnIndexOrThrow("Stock")),
                c.getDouble(c.getColumnIndexOrThrow("PrecioCompra")),
                c.getDouble(c.getColumnIndexOrThrow("PrecioVenta")),
                c.getString(c.getColumnIndexOrThrow("Descripcion"))
        );
    }
}