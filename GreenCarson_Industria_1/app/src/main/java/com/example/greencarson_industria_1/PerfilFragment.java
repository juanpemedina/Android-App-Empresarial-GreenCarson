package com.example.greencarson_industria_1;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;
    Button btnCerrarSesion;
    TextView direccionView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        leerDatosDeFirestore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        CerrarSesion(view); //funcion b
        showEmail(view);
        direccionView = view.findViewById(R.id.textPerfil4);

        // Inflate the layout for this fragment
        return view;
    }
    // Muestra Email con el que se inicio sesion
    private void showEmail(View view){
        auth = FirebaseAuth.getInstance();
        textView = view.findViewById(R.id.textPerfil2);
        user = auth.getCurrentUser();
        //Si no hay usuario te regresa a a la actividad Main y si hay lo cambia el TextView
        if (user == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
        else{
            textView.setText(user.getEmail()); //cambia el texto y pone el email
        }
    }
    // Accion del boton de cerrar sesion
    private void CerrarSesion(View view){
        btnCerrarSesion = view.findViewById(R.id.btn_CerrarS);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog();
            } // Muestra el dialogo de cerrar cesion
        });
    }
    // Funcion para mostrar dialogo de cerrar sesion
    private void showDialog(){
        // Obtener el contexto desde la vista inflada en el fragmento
        Context context = getContext();

        if (context != null) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_custom_dialog2);
            //button cancelar que quita el dialog layout
            Button btnNo = dialog.findViewById(R.id.btn_no);
            btnNo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    dialog.dismiss();
                }
            });
            //button de aceptar para cerrar sesion
            Button btnYes = dialog.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //Cierra la sesion creada con el Auth de Firebase
                    FirebaseAuth.getInstance().signOut();
                    //Te envia al Activity Login
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            dialog.show();
        }
    }

    private void leerDatosDeFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Obtener el ID del usuario autenticado
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userID = currentUser != null ? currentUser.getUid() : "";

        DocumentReference docRef = db.collection("usuarios").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String direccion = document.getString("direccion");
                        // Puedes hacer lo que necesites con la dirección obtenida
                        Log.d(TAG, "Dirección del usuario: " + direccion);
                        direccionView.setText(direccion);
                    } else {
                        Log.d(TAG, "No existe el documento para el userID: " + userID);
                    }
                } else {
                    Log.d(TAG, "Error al obtener el documento: ", task.getException());
                }
            }
        });

    }
}