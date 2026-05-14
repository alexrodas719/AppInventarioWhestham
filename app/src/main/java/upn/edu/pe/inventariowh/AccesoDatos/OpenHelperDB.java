package upn.edu.pe.inventariowh.AccesoDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OpenHelperDB extends SQLiteOpenHelper {


    String tabla_categoria ="CREATE TABLE Categoria(IdCategoria INTEGER PRIMARY KEY AUTOINCREMENT,Nombre VARCHAR(60) NOT NULL,Descripcion VARCHAR(200));";

    String tabla_producto ="CREATE TABLE Producto(IdProducto INTEGER PRIMARY KEY AUTOINCREMENT,Nombre VARCHAR(60) NOT NULL,Foto VARCHAR(255)," +
                    "SKU VARCHAR(200) NOT NULL UNIQUE,IdCategoria INTEGER NOT NULL,Talla VARCHAR(20)," +
                    "Color VARCHAR(30) NOT NULL,Stock INTEGER NOT NULL,Precio REAL NOT NULL," +
                    "Descripcion VARCHAR(200),FOREIGN KEY(IdCategoria) REFERENCES Categoria(IdCategoria));";


    String tabla_movimientoInventario ="CREATE TABLE MovimientoInventario(IdMovimiento INTEGER PRIMARY KEY AUTOINCREMENT,Codigo VARCHAR(50) NOT NULL," +
                    "Tipo VARCHAR(20) NOT NULL,IdProducto INTEGER NOT NULL,Cantidad INTEGER NOT NULL,Fecha INTEGER NOT NULL," +
                    "Monto REAL NOT NULL,Observacion VARCHAR(200),FOREIGN KEY(IdProducto) REFERENCES Producto(IdProducto));";



    String tabla_venta ="CREATE TABLE Venta(IdVenta INTEGER PRIMARY KEY AUTOINCREMENT,Codigo VARCHAR(50) NOT NULL," +
                    "Fecha INTEGER NOT NULL,Subtotal REAL NOT NULL,Descuento REAL NOT NULL,Total REAL NOT NULL);";

    String tabla_detalleVenta ="CREATE TABLE DetalleVenta(IdDetalleVenta INTEGER PRIMARY KEY AUTOINCREMENT,IdVenta INTEGER NOT NULL," +
                    "IdProducto INTEGER NOT NULL,Cantidad INTEGER NOT NULL,PrecioUnitario REAL NOT NULL,Subtotal REAL NOT NULL," +
                    "FOREIGN KEY(IdVenta) REFERENCES Venta(IdVenta),FOREIGN KEY(IdProducto) REFERENCES Producto(IdProducto));";

    public OpenHelperDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tabla_categoria);
        db.execSQL(tabla_producto);
        db.execSQL(tabla_movimientoInventario);
        db.execSQL(tabla_venta);
        db.execSQL(tabla_detalleVenta);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS DetalleVenta");
        db.execSQL("DROP TABLE IF EXISTS MovimientoInventario");
        db.execSQL("DROP TABLE IF EXISTS Venta");
        db.execSQL("DROP TABLE IF EXISTS Producto");
        db.execSQL("DROP TABLE IF EXISTS Categoria");
        onCreate(db);
    }
}
