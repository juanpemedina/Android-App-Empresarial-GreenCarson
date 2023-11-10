package com.example.greencarson_industria_1;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.time.Month;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialFragment extends Fragment {
    private Button pickDateBtn;
    private Button btnInfo;
    private TextView selectedDateTV;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialFragment newInstance(String param1, String param2) {
        HistorialFragment fragment = new HistorialFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historial, container, false);
        // Conectar los elementos de la interfaz de usuario.
        pickDateBtn = view.findViewById(R.id.btnD);
        selectedDateTV = view.findViewById(R.id.textView8);
        // Agregar un clic oyente al botón de selección de fecha.
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnInfo = view.findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                HistorialInfoFragment changeFragment = new HistorialInfoFragment();
                // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PerfilFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                transaction.commit();
            }
        });


        return view;
    }

    private void showDatePickerDialog() {
        // Obtener la instancia del calendario.
        final Calendar c = Calendar.getInstance();

        // Obtener el año, mes y día actual.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        // Crear un diálogo de selección de fecha.
        /*DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Mostrar la fecha seleccionada en el TextView.
                        pickDateBtn.setText(getMonthYearString(year, monthOfYear));
                    }
                },
                year, month, 1
        );
        // Mostrar el diálogo de selección de fecha.
        datePickerDialog.show();*/
        MonthYearPickerDialog pd = new MonthYearPickerDialog();
        pd.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Mostrar la fecha seleccionada en el TextView.
                pickDateBtn.setText(getMonthYearString(year, monthOfYear));
            }
        });
        pd.show(getActivity().getFragmentManager(), "MonthYearPickerDialog");
    }

    private String getMonthYearString(int year, int month) {
        // Convierte el número de mes en una representación legible (p. ej., de 0 a Enero, 1 a Febrero, etc.).
        month = month -1;
        String[] monthNames = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String monthName = monthNames[month];

        // Retorna el mes y el año en un formato legible.
        return monthName + " " + year;
    }
}