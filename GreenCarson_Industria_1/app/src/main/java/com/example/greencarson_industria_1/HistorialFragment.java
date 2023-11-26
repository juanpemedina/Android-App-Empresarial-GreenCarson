package com.example.greencarson_industria_1;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Month;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialFragment extends Fragment {
    private Button pickDateBtn;
    private Button btnShow;
    private TextView selectedDateTV;
    private View viewP;

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
        viewP = view;
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
        readToTable(viewP, 0, 0);
        btnShow = view.findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readToTable(viewP, 0, 0);
                btnShow.setVisibility(View.INVISIBLE);

            }
        });

        return view;
    }
    //MOSTAR Date picker
    private void showDatePickerDialog() {
        // Obtener la instancia del calendario.
        final Calendar c = Calendar.getInstance();

        // Obtener el año, mes y día actual.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        MonthYearPickerDialog pd = new MonthYearPickerDialog();
        pd.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Mostrar la fecha seleccionada en el TextView.
                pickDateBtn.setText(getMonthYearString(year, monthOfYear));
                readToTable(viewP, year, monthOfYear);

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

    //leer y mostrar tabla
    private void readToTable(View view, int selectedYear, int selectedMonth){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Obtener el ID del usuario autenticado
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userID = currentUser != null ? currentUser.getUid() : "";


        db.collection("recolecciones_empresariales")
                .whereEqualTo("estado","terminado")
                .whereEqualTo("usuarioID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            TableLayout tableLayout = view.findViewById(R.id.tableLayout);
                            clearTable(tableLayout);

                            if (selectedYear != 0 && selectedMonth != 0){
                                btnShow.setVisibility(View.VISIBLE);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String fechaCampo = document.getString("fecha");
                                    // Extraer el día, mes y año de la fecha almacenada en Firestore
                                    int year = Integer.parseInt(fechaCampo.split("-")[0]);
                                    int month = Integer.parseInt(fechaCampo.split("-")[1]);
                                    int day = Integer.parseInt(fechaCampo.split("-")[2]);
                                    Log.d(TAG, "ID del fecha: " + fechaCampo);
                                    Log.d(TAG, "ID del Dia: " + day);
                                    Log.d(TAG, "ID del Mes: " + month);
                                    Log.d(TAG, "ID del Año: " + year);

                                    // Filtrar los documentos por día, mes y año seleccionados
                                    if (year == selectedYear && month == selectedMonth ) {
                                        insertIntoTable(document,tableLayout);
                                    }
                                }

                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    insertIntoTable(document,tableLayout);
                                }
                            }


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void clearTable(TableLayout tableLayout){
        // Obtiene el número de filas en el TableLayout
        int childCount = tableLayout.getChildCount();

        // Comienza desde 1 para no eliminar la primera fila
        for (int i = childCount - 1; i > 0; i--) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                // Elimina cada fila del TableLayout excepto la primera
                tableLayout.removeViewAt(i);
            }
        }
    }

//Insert to table
    private void insertIntoTable(QueryDocumentSnapshot document, TableLayout tableLayout){
        Log.d(TAG, document.getId() + " => " + document.getData());
        String documentId = document.getId(); // Obtiene el ID del documento
        String fechaCampo = document.getString("fecha");
        Log.d(TAG, "ID del documento: " + documentId);
        Log.d(TAG, "Valor del campo específico: " + fechaCampo.toString());

        // Crear una nueva fila (TableRow)
        TableRow newRow = new TableRow(getActivity());

        // Crear TextViews para cada valor y configurar su texto
        TextView documentIdTextView = new TextView(getActivity());
        documentIdTextView.setText(documentId);
        documentIdTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        documentIdTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        documentIdTextView.setTextColor(getResources().getColor(android.R.color.black)); // Establecer color negro
        documentIdTextView.setPadding(10, 10, 10, 10);

        TextView fechaCampoTextView = new TextView(getActivity());
        fechaCampoTextView.setText(fechaCampo);
        fechaCampoTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        fechaCampoTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        fechaCampoTextView.setTextColor(getResources().getColor(android.R.color.black)); // Establecer color negro
        fechaCampoTextView.setPadding(10, 10, 10, 10);

        Button infoButton = new Button(getActivity());
        infoButton.setTransformationMethod(null);
        infoButton.setText("Info.");
        infoButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        infoButton.setTextColor(getResources().getColor(R.color.verdeRecicla));
        infoButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentId = document.getId(); // Obtiene el ID del documento
                // Crear una instancia del fragmento HistorialInfoFragment con el documentId
                HistorialInfoFragment changeFragment = HistorialInfoFragment.newInstance(documentId);
                // Realizar una transacción de fragmentos para reemplazar el Fragmento actual por HistorialInfoFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        infoButton.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        // Agregar los TextViews a la fila
        newRow.addView(documentIdTextView);
        newRow.addView(fechaCampoTextView);
        newRow.addView(infoButton);
        // Agregar la fila a la tabla
        tableLayout.addView(newRow);

    }
}