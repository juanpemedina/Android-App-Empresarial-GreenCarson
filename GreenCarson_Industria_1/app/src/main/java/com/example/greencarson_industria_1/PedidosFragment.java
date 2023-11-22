package com.example.greencarson_industria_1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PedidosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidosFragment extends Fragment {

    private Spinner materialSpin;
    private Spinner unidadSpin;
    private EditText cantidad_RP;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PedidosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PedidosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidosFragment newInstance(String param1, String param2) {
        PedidosFragment fragment = new PedidosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        //Spiner 1
        materialSpin =  view.findViewById(R.id.material_RP);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterMaterial = ArrayAdapter.createFromResource(getActivity(), R.array.materiales, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        materialSpin.setAdapter(adapterMaterial);
        //Spiner 2
        unidadSpin = view.findViewById(R.id.unidad_RP);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterUnidad = ArrayAdapter.createFromResource(getActivity(), R.array.unidad, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterUnidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        unidadSpin.setAdapter(adapterUnidad);

        // Obtén una referencia al botón desde el diseño
        Button button = view.findViewById(R.id.button_agendar);
        cantidad_RP = view.findViewById(R.id.cantidad_RP);

        // Encuentra el TableLayout en la vista
        TableLayout tableLayout = view.findViewById(R.id.tableLayout);
        String estatusPedido = "pendiente";

        // Obtén una referencia al botón de "Btn Agendar"
        Button btnagendar = view.findViewById(R.id.button_agendar);
        // Configura un OnClickListener para el botón
        btnagendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estatusPedido == "activo"){
                    Toast.makeText(getContext(), "Ya tienes un pedido activo", Toast.LENGTH_SHORT).show();
                } else {
                    List<Map<String, String>> contenido = crearContenido(tableLayout);
                    // Crea una instancia del fragmento al que deseas navegar (PedidosAgendarFragment)
                    PedidosAgendarFragment changeFragment = PedidosAgendarFragment.newInstance(contenido);
                    // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PedidosAgendarFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                    transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                    transaction.commit();
                }
            }
        });
        // Obtén una referencia al botón de "Añadir Material"
        Button addButton = view.findViewById(R.id.button_añadirMaterial);
        // Configura un OnClickListener para el botón "Añadir Material"
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estatusPedido == "activo"){
                    Toast.makeText(getContext(), "Ya tienes un pedido activo", Toast.LENGTH_SHORT).show();
                } else {
                    agregarFilaATabla(tableLayout);
                }
            }
        });
        return view;
    }

    // Método para agregar una fila a tu TableLayout
    private void agregarFilaATabla(TableLayout tableLayout) {
        // Obtén los valores seleccionados e ingresados
        String materialSeleccionado = materialSpin.getSelectedItem().toString();
        String cantidadIngresada = cantidad_RP.getText().toString();
        String unidadSeleccionada = unidadSpin.getSelectedItem().toString();

        // Validación para evitar agregar "Seleccionar Material" o "Seleccionar Unidad" a la tabla
        /////////FALTANTE/////////

        // Crea una nueva fila para tu TableLayout
        TableRow row = new TableRow(getActivity());

        // Crea TextViews para los valores obtenidos
        TextView materialTextView = new TextView(getActivity());
        materialTextView.setText(materialSeleccionado);
        materialTextView.setPadding(10, 10, 10, 10);
        materialTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView cantidadTextView = new TextView(getActivity());
        cantidadTextView.setText(cantidadIngresada);
        cantidadTextView.setPadding(10, 10, 10, 10);
        cantidadTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView unidadTextView = new TextView(getActivity());
        unidadTextView.setText(unidadSeleccionada);
        unidadTextView.setPadding(10, 10, 10, 10);
        unidadTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        // Crea un botón para la fila
        Button eliminarButton = new Button(getActivity());
        eliminarButton.setTransformationMethod(null);
        eliminarButton.setText("Eliminar");
        eliminarButton.setTextColor(getResources().getColor(R.color.redAdver));
        eliminarButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableLayout.removeView(row);
            }
        });
        eliminarButton.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        // Agrega los elementos a la fila
        row.addView(materialTextView);
        row.addView(cantidadTextView);
        row.addView(unidadTextView);
        row.addView(eliminarButton);

        // Agrega la fila al TableLayout
        tableLayout.addView(row);

        materialSpin.setSelection(0);
        unidadSpin.setSelection(0);
        cantidad_RP.setText("");

    }
    //Crea mapa con valores y lo envia al proximo fragmento
    private List<Map<String, String>> crearContenido(TableLayout tableLayout) {
        List<Map<String, String>> listaMapas = new ArrayList<>();

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            TextView materialTextView = (TextView) row.getChildAt(0);
            TextView cantidadTextView = (TextView) row.getChildAt(1);
            TextView unidadTextView = (TextView) row.getChildAt(2);

            String material = materialTextView.getText().toString();
            String cantidad = cantidadTextView.getText().toString();
            String unidad = unidadTextView.getText().toString();

            Map<String, String> fila = new HashMap<>();
            fila.put("material", material);
            fila.put("cantidad", cantidad);
            fila.put("unidad", unidad);

            listaMapas.add(fila);
        }

        return listaMapas;
    }
}

