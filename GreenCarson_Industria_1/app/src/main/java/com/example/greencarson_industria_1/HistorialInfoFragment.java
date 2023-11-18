package com.example.greencarson_industria_1;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialInfoFragment extends Fragment {

    private Button btnHComentar;
    private Button btnBack;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HistorialInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistorialInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
   /* public static HistorialInfoFragment newInstance(String param1, String param2) {
        HistorialInfoFragment fragment = new HistorialInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    } */
    public static HistorialInfoFragment newInstance(String documentId) {
        HistorialInfoFragment fragment = new HistorialInfoFragment();
        Bundle args = new Bundle();
        args.putString("documentId", documentId);
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
        View view = inflater.inflate(R.layout.fragment_historial_info, container, false);
        String documentId = getArguments().getString("documentId");
        leerTablaFS(view,documentId);


        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                HistorialFragment changeFragment = new HistorialFragment();
                // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PerfilFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                transaction.commit();
            }
        });
        btnHComentar = view.findViewById(R.id.btn_comentar);
        btnHComentar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String documentId = getArguments().getString("documentId");
                // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                HistorialComentariosFragment changeFragment = HistorialComentariosFragment.newInstance(documentId);
                // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PerfilFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                transaction.commit();
            }
        });


        return view;
    }
    //leer tabla
    private void leerTablaFS(View view, String documentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener el ID del usuario autenticado
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userID = currentUser != null ? currentUser.getUid() : "";

        db.collection("recolecciones_empresariales")
                .document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                // Fecha entregado
                                TextView fechaTextView = view.findViewById(R.id.txtFecha2);
                                String fechaDocumento = document.getString("fecha");
                                fechaTextView.setText(fechaDocumento);
                                //Ubicacion
                                TextView direccionTextView = view.findViewById(R.id.textView19);
                                String direccionDocumento = document.getString("direccion");
                                direccionTextView.setText(direccionDocumento);
                                TableLayout tableLayout = view.findViewById(R.id.tableLayout);
                                // Obtener el campo "contenido" como un arreglo de mapas
                                List<Map<String, String>> listaDeMapas = (List<Map<String, String>>) document.get("contenido");
                                if (listaDeMapas != null && !listaDeMapas.isEmpty()) {
                                    // Iterar a través de los mapas en la lista
                                    for (Map<String, String> mapaInterno : listaDeMapas) {

                                        // Acceder a los valores internos del mapa interno
                                        String valorMaterial = mapaInterno.get("material");
                                        String valorCantidad = mapaInterno.get("cantidad");
                                        String valorUnidad = mapaInterno.get("unidad");

                                        // Crear una nueva fila (TableRow)
                                        TableRow newRow = new TableRow(getActivity());

                                        // Crear TextViews para cada valor y configurar su texto
                                        TextView materialTextView = new TextView(getActivity());
                                        materialTextView.setText(valorMaterial);
                                        materialTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                                        materialTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                        materialTextView.setTextColor(getResources().getColor(android.R.color.black)); // Establecer color negro
                                        materialTextView.setPadding(10, 10, 10, 10);

                                        TextView cantidadTextView = new TextView(getActivity());
                                        cantidadTextView.setText(valorCantidad);
                                        cantidadTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                                        cantidadTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                        cantidadTextView.setTextColor(getResources().getColor(android.R.color.black)); // Establecer color negro
                                        cantidadTextView.setPadding(10, 10, 10, 10);

                                        TextView unidadTextView = new TextView(getActivity());
                                        unidadTextView.setText(valorUnidad);
                                        unidadTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                                        unidadTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                        unidadTextView.setTextColor(getResources().getColor(android.R.color.black)); // Establecer color negro
                                        unidadTextView.setPadding(10, 10, 10, 10);

                                        // Agregar los TextViews a la fila
                                        newRow.addView(materialTextView);
                                        newRow.addView(cantidadTextView);
                                        newRow.addView(unidadTextView);

                                        // Agregar la fila a la tabla
                                        tableLayout.addView(newRow);
                                        // Hacer lo que necesites con estos valores
                                        Log.d(TAG, "Material: " + valorMaterial);
                                        Log.d(TAG, "Cantidad: " + valorCantidad);
                                        Log.d(TAG, "Unidad: " + valorUnidad);
                                    }
                                } else {
                                    Log.d(TAG, "El campo nombreDelCampo está vacío o es nulo.");
                                }

                            } else {
                            Log.d(TAG, "El documento no existe");
                            }
                        } else {
                            Log.d(TAG, "Error obteniendo documentos: ", task.getException());
                        }
                    }
                });
    }


}