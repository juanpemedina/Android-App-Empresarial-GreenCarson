package com.example.greencarson_industria_1;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramadosCancelarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramadosCancelarFragment extends Fragment {

    private Button btnEliminar;
    private Button btnBack;
    private TextView fechaLimitTV;
    String nuevaFechaStr;

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

        leerFechadFS();

        fechaLimitTV= view.findViewById(R.id.editTextText2);
        btnEliminar = view.findViewById(R.id.btn_eliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                compararFechas(nuevaFechaStr);
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
    //Muestra el dialog de cancelar
    private void showDialog(){
        // Obtener el contexto desde la vista inflada en el fragmento
        Context context = getContext();

        if (context != null) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_custom_dialog);
            //button no
            Button btnNo = dialog.findViewById(R.id.btn_no);
            btnNo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    dialog.dismiss();
                }
            });
            //button yes
            Button btnYes = dialog.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    deleteFSPedido();
                    // Una vez que el pedido se ha eliminado con éxito
                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView); // Reemplaza con tu ID de BottomNavigationView

                    // Seleccionar el elemento deseado
                    MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.pedidos); // Reemplaza con el ID del elemento a seleccionar
                    menuItem.setChecked(true);


                    //limpia el dialog
                    dialog.dismiss();
                    // Crea una instancia del fragmento al que deseas navegar (PerfilFragment)
                    PedidosFragment changeFragment = new PedidosFragment();
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
    //borra el pedido
    private void deleteFSPedido(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener el ID del usuario autenticado
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userID = currentUser != null ? currentUser.getUid() : "";

        db.collection("recolecciones_empresariales")
                .whereEqualTo("estado","activo")
                .whereEqualTo("usuarioID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Iterar sobre los documentos que cumplen con el filtro
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Obtener la referencia al documento y borrarlo
                                db.collection("recolecciones_empresariales")
                                        .document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Document successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void leerFechadFS() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Obtener el ID del usuario autenticado
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userID = currentUser != null ? currentUser.getUid() : "";

        db.collection("recolecciones_empresariales")
                .whereEqualTo("estado", "activo")
                .whereEqualTo("usuarioID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String fechaDocumento = document.getString("fecha");
                                Log.d(TAG, "fechaDocumento" + fechaDocumento);

                                restarUnDiaFecha(fechaDocumento);

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    // Restar un dia a la fecha
    private void restarUnDiaFecha(String fecha) {
        try {
            // Formato de fecha actual: AÑO-MES-DIA
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            // Parsear el String de fecha a un objeto Date
            Date date = sdf.parse(fecha);
            // Crear un objeto Calendar e inicializarlo con la fecha parseada
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // Restar un día
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            // Obtener la nueva fecha
            Date nuevaFecha = calendar.getTime();

            // Convertir la nueva fecha de nuevo a String con el formato deseado
            nuevaFechaStr = sdf.format(nuevaFecha);
            Log.d(TAG, "fechaDocumento: " + nuevaFechaStr);

            // Mostrar la nueva fecha en el TextView
            fechaLimitTV.setText(nuevaFechaStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void compararFechas(String fecha) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(fecha); // Fecha obtenida de la base de datos

            // Obtener la fecha actual
            Calendar calendarFechaActual = Calendar.getInstance();
            Calendar calendarFecha = Calendar.getInstance();
            calendarFecha.setTime(date);

            // Obtener hora
            Calendar horaActual = Calendar.getInstance();
            int hora = horaActual.get(Calendar.HOUR_OF_DAY); // Obtener la hora actual en formato de 24 horas

            // Comparar solo año, mes y día
            if (calendarFecha.get(Calendar.YEAR) == calendarFechaActual.get(Calendar.YEAR) && calendarFecha.get(Calendar.MONTH) == calendarFechaActual.get(Calendar.MONTH) &&
                    calendarFecha.get(Calendar.DAY_OF_MONTH) == calendarFechaActual.get(Calendar.DAY_OF_MONTH)) {
                Log.d(TAG, "La fecha es la misma que la fecha actual");
                if (hora < 17) { //Cambiar 17 por la hora que quieres
                    showDialog();
                    Log.d(TAG, "La hora es antes de las 18:00 hrs");
                } else {
                    Log.d(TAG, "La hora es después o igual a las 18:00 hrs");
                    Toast.makeText(getContext(), "Venció la hora y fecha límite cancelación", Toast.LENGTH_SHORT).show();
                }
            } else if (calendarFecha.before(calendarFechaActual)) {
                Log.d(TAG, "La fecha es anterior a la fecha actual");
                Toast.makeText(getContext(), "Venció la fecha de cancelación", Toast.LENGTH_SHORT).show();

            } else {
                showDialog();
                Log.d(TAG, "La fecha es posterior a la fecha actual");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}