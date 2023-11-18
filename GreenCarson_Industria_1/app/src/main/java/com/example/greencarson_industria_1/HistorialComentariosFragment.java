package com.example.greencarson_industria_1;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialComentariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialComentariosFragment extends Fragment {

    private Button btnBack;
    private Button btnComentar;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistorialComentariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistorialComentariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialComentariosFragment newInstance(String documentId) {
        HistorialComentariosFragment fragment = new HistorialComentariosFragment();
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
        View view = inflater.inflate(R.layout.fragment_historial_comentarios, container, false);
        String documentId = getArguments().getString("documentId");
        readDate(view,documentId);
        EditText comentETxt = view.findViewById(R.id.comentText);



        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Crea una instancia del fragmento al que deseas navegar
                HistorialFragment changeFragment = new HistorialFragment();
                // Realiza una transacción de fragmentos para reemplazar
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                transaction.commit();
            }
        });
        btnComentar = view.findViewById(R.id.btn_enviar);
        btnComentar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String textoIngresado = comentETxt.getText().toString();
                Log.d(TAG, "DocumentSnapshot data: textoIngresado" );
                sendComent(view,documentId, textoIngresado);
                // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                HistorialFragment changeFragment = new HistorialFragment();
                // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PerfilFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                transaction.commit();

            }
        });
        return view;
    }
    private void sendComent(View view, String documentId, String textoIngresado){
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference DocRef = db.collection("recolecciones_empresariales").document(documentId);
        DocRef
                .update("comentario", textoIngresado)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    private void readDate(View view, String documentId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("recolecciones_empresariales").document(documentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        TextView fechaTextView = view.findViewById(R.id.textView14);
                        String fechaDocumento = document.getString("fecha");
                        fechaTextView.setText(fechaDocumento);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}