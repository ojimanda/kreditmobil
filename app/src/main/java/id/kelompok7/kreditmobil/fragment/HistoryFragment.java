package id.kelompok7.kreditmobil.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.kelompok7.kreditmobil.R;
import id.kelompok7.kreditmobil.adapter.HistoryAdapter;
import id.kelompok7.kreditmobil.config.DBHelper;
import id.kelompok7.kreditmobil.model.ModelHistory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DBHelper db;
    String brand, merk, dp,tenor, cicilan;
    List<ModelHistory> element;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        View view = inflater.inflate(R.layout.activity_history, container, false); db = new DBHelper(getContext());
        Cursor historyCursor =db.getHistories();

        if(historyCursor.getCount() == 0) {
            Toast.makeText(getContext(), "Empty History", Toast.LENGTH_SHORT).show();
        } else {

            element = new ArrayList<>();
            while (historyCursor.moveToNext()){
                String uuid = historyCursor.getString(0);
                String uid = historyCursor.getString(1);
                brand = historyCursor.getString(2);
                merk = historyCursor.getString(3);
                dp = historyCursor.getString(4);
                tenor = historyCursor.getString(5);
                cicilan = historyCursor.getString(6);
                System.out.println(brand + " "+ merk + " " +cicilan);
                element.add(new ModelHistory(brand, merk, dp +"%", "Rp " + cicilan + " per bulan"));
            }

            HistoryAdapter adapter = new HistoryAdapter(element, getContext());
            RecyclerView recyclerView = view.findViewById(R.id.rv_history);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

        }
        return view ;
    }
}