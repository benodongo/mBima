package ben.com.mbima.categories;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ben.com.mbima.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashFragment extends Fragment implements View.OnClickListener {
    CardView vehicle,general,medical,personal;


    public DashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_dash_board, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        vehicle = view.findViewById(R.id.vehicle);
        vehicle.setOnClickListener(this);
        general = view.findViewById(R.id.general);
        general.setOnClickListener(this);
        medical = view.findViewById(R.id.medical);
        medical.setOnClickListener(this);
        personal = view.findViewById(R.id.personal);
        personal.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.vehicle:
                gotoVehicle();
                break;
            case R.id.general:
                gotoGeneral();
                break;
            case R.id.medical:
                gotoMedical();
                break;
            case R.id.personal:
                gotoPersonal();
                break;
        }
    }

    private void gotoMedical() {
        Fragment fragment = new MedicalFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void gotoPersonal() {
        Fragment fragment = new LifeFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void gotoGeneral() {
        Fragment fragment = new GeneralFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void gotoVehicle() {
        Fragment fragment = new VehicleFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
