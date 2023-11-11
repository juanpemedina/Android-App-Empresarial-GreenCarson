package com.example.greencarson_industria_1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        btnCerrarSesion = view.findViewById(R.id.btn_CerrarS);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog();
            }
        });

        auth = FirebaseAuth.getInstance();
        textView = view.findViewById(R.id.textPerfil2);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
        else{
            textView.setText(user.getEmail());
        }
        // Inflate the layout for this fragment
        return view;
    }
    private void showDialog(){
        // Obtener el contexto desde la vista inflada en el fragmento
        Context context = getContext();

        if (context != null) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_custom_dialog2);
            //button no
            Button btnNo = dialog.findViewById(R.id.btn_yes);
            btnNo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    dialog.dismiss();
                }
            });
            //button yes
            Button btnYes = dialog.findViewById(R.id.btn_no);
            btnYes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            dialog.show();
        }
    }
}