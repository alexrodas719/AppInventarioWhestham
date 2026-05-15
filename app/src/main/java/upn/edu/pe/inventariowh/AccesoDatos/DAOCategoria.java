package upn.edu.pe.inventariowh.AccesoDatos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import upn.edu.pe.inventariowh.Modelos.Categoria;

public class DAOCategoria {

    private String nombreBD;
    private int version;
    private Activity contexto;

    SQLiteOpenHelper oHelper;

    public DAOCategoria(Activity contexto) {

        this.nombreBD = "WESTHAMDB";
        this.version = 2; // Incrementar versión para aplicar cambios de categorías
        this.contexto = contexto;

        oHelper = new OpenHelperDB(contexto, nombreBD, null, version);
    }

    // =========================
    // INSERTAR
    // =========================
    public boolean Insertar(Categoria oC) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("Nombre", oC.getNombre());
        oColumna.put("Descripcion", oC.getDescripcion());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        long fila = db.insert("Categoria", null, oColumna);

        db.close();

        return fila > 0;
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Categoria> ListarTodos() {

        List<Categoria> lista = new ArrayList<>();

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM Categoria", null);

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
    public Categoria Buscar(int idCategoria) {

        Categoria oC = null;

        SQLiteDatabase db = oHelper.getReadableDatabase();

        Cursor oRegistros =
                db.rawQuery("SELECT * FROM Categoria WHERE IdCategoria=?",
                        new String[]{String.valueOf(idCategoria)});

        if (oRegistros.moveToFirst()) {

            oC = mapearRegistro(oRegistros);
        }

        oRegistros.close();
        db.close();

        return oC;
    }

    // =========================
    // BUSCAR POR NOMBRE
    // =========================
    public Categoria BuscarPorNombre(String nombre) {
        Categoria oC = null;
        SQLiteDatabase db = oHelper.getReadableDatabase();
        Cursor oRegistros = db.rawQuery("SELECT * FROM Categoria WHERE Nombre=?", new String[]{nombre});
        if (oRegistros.moveToFirst()) {
            oC = mapearRegistro(oRegistros);
        }
        oRegistros.close();
        db.close();
        return oC;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean Actualizar(Categoria oC) {

        ContentValues oColumna = new ContentValues();

        oColumna.put("Nombre", oC.getNombre());
        oColumna.put("Descripcion", oC.getDescripcion());

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =db.update("Categoria",oColumna,"IdCategoria=?",
                        new String[]{String.valueOf(oC.getIdCategoria())});

        db.close();

        return filas > 0;
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean Eliminar(int idCategoria) {

        SQLiteDatabase db = oHelper.getWritableDatabase();

        int filas =db.delete("Categoria",
                        "IdCategoria=?",
                        new String[]{String.valueOf(idCategoria)});

        db.close();

        return filas > 0;
    }

    // =========================
    // MAPEAR REGISTRO
    // =========================
    private Categoria mapearRegistro(Cursor oRegistros) {

        int idCategoria =oRegistros.getInt(oRegistros.getColumnIndexOrThrow("IdCategoria"));

        String nombre =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Nombre"));

        String descripcion =oRegistros.getString(oRegistros.getColumnIndexOrThrow("Descripcion"));

        return new Categoria(idCategoria,nombre,descripcion);
    }
}