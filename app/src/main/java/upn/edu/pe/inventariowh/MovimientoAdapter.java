package upn.edu.pe.inventariowh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import upn.edu.pe.inventariowh.Modelos.MovimientoInventario;

public class MovimientoAdapter extends BaseAdapter {

    private Context context;
    private List<MovimientoInventario> lista;

    public MovimientoAdapter(Context context, List<MovimientoInventario> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_movimiento, parent, false);
        }

        MovimientoInventario m = lista.get(position);

        TextView txtCodigo = convertView.findViewById(R.id.txtCodigoVenta);
        TextView txtDetalle = convertView.findViewById(R.id.txtDetalle);
        TextView txtMonto = convertView.findViewById(R.id.txtMonto);
        TextView txtFechaHora = convertView.findViewById(R.id.txtFechaHora);

        // Código venta
        txtCodigo.setText("Venta #" + m.getCodigo());

        // Cantidad productos
        txtDetalle.setText("Productos: " + m.getCantidad());

        // Monto
        txtMonto.setText("S/ " + String.format(Locale.getDefault(), "%.2f", m.getMonto()));

        // Fecha y hora
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String fechaTexto = sdf.format(new Date(m.getFecha()));

        txtFechaHora.setText(fechaTexto);

        return convertView;
    }
}