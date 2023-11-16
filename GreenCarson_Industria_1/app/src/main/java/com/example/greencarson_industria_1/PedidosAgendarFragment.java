package com.example.greencarson_industria_1;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PedidosAgendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PedidosAgendarFragment extends Fragment {

    private Button btnBack;
    private Button btnAgendar;
    private Button btnTime;
    private Button btnCalendar;


    //
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PedidosAgendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidosAgendarFragment newInstance(String param1, String param2) {
        PedidosAgendarFragment fragment = new PedidosAgendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PedidosAgendarFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_pedidos_agendar, container, false);

        FirebaseApp.initializeApp(requireContext()); // Inicializa Firebase en el contexto del Fragmento

        btnBack = view.findViewById(R.id.buttonBackPedidos);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                PedidosFragment changeFragment = new PedidosFragment();
                // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PerfilFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                transaction.commit();
            }
        });
        btnAgendar = view.findViewById(R.id.buttonBackAgendar);
        btnAgendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                guardarEnFirestore();

                // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                PedidosFragment changeFragment = new PedidosFragment();
                // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PerfilFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                transaction.commit();
            }
        });
        //Calendar
        btnCalendar = view.findViewById(R.id.btnDate);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCalendario(view);
            }
        });
        btnTime = view.findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirHora(view);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void abrirCalendario(View view){
        Calendar cal = Calendar.getInstance();
        int año = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String fecha = dayOfMonth + "/" + month + "/" + year;
                btnCalendar.setText(fecha);
            }
        }, dia, mes, año);
        dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpd.show();
    }
     public void abrirHora(View view){
        Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int min  = c.get(Calendar.MINUTE);

         TimePickerDialog tmd = new TimePickerDialog(requireActivity(), new TimePickerDialog.OnTimeSetListener() {
             @Override
             public void onTimeSet(TimePicker timePicker, int hoursOfDay, int minute) {
                     btnTime.setText(hoursOfDay + ":" + minute);
             }
         },hora,min, false);
         tmd.show();
     }
     // guardar
     private void guardarEnFirestore() {
         FirebaseFirestore db = FirebaseFirestore.getInstance();
         // Crear un mapa con los datos que deseas guardar
         Map<String, Object> recolecion = new HashMap<>();
         recolecion.put("direccion", "Calle Baltazar, No. 3015, 72760");
         recolecion.put("estado", "activo");
         recolecion.put("fecha", "2023-11-16");
         recolecion.put("hora", "09:14");
         recolecion.put("usuarioID", "JP");

         // Agregar estos datos a la colección "usuarios"
         db.collection("recolecciones")
                 .add(recolecion)
                 .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                     @Override
                     public void onSuccess(DocumentReference documentReference) {
                         // Éxito al guardar los datos
                         Log.d(TAG, "Documento agregado con ID: " + documentReference.getId());
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         // Error al guardar los datos
                         Log.w(TAG, "Error al agregar documento", e);
                     }
                 });


     }


}