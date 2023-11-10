package com.example.greencarson_industria_1;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramadosCancelarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramadosCancelarFragment extends Fragment {

    private Button btnEliminar;
    private Button btnBack;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProgramadosCancelarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgramadosCancelarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgramadosCancelarFragment newInstance(String param1, String param2) {
        ProgramadosCancelarFragment fragment = new ProgramadosCancelarFragment();
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
        View view = inflater.inflate(R.layout.fragment_programados_cancelar, container, false);

        btnEliminar = view.findViewById(R.id.btn_eliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog();
            }
        });
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                ProgramadosFragment changeFragment = new ProgramadosFragment();
                // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PerfilFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                transaction.commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    private void showDialog(){
        // Obtener el contexto desde la vista inflada en el fragmento
        Context context = getContext();

        if (context != null) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_custom_dialog);
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
                    //limpia el dialog
                    dialog.dismiss();
                    // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                    ProgramadosFragment changeFragment = new ProgramadosFragment();
                    // Realiza una transacción de fragmentos para reemplazar PedidosFragment por PerfilFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, changeFragment); // Reemplaza el contenedor de fragmentos
                    transaction.addToBackStack(null); // Agrega la transacción a la pila de retroceso
                    transaction.commit();
                }
            });
            dialog.show();
        }

    }
}