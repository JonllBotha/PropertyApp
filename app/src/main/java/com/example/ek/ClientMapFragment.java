package com.example.ek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class ClientMapFragment extends Fragment implements OnMapReadyCallback {

    public ClientMapFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        if (supportMapFragment != null) {
            Log.d("MapFragment", "Map Fragment Found");
            supportMapFragment.getMapAsync(this);
        } else {
            Log.d("MapFragment", "Map Fragment Not Found");
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker and move the camera
        LatLng bellville = new LatLng(-33.9000, 18.6253);
        LatLng benoni = new LatLng(-26.1881, 28.3200);
        LatLng bethlehem = new LatLng(-28.2304, 28.3086);
        LatLng bloemfontein = new LatLng(-29.0852, 26.1596);
        LatLng boksburg = new LatLng(-26.2120, 28.2597);
        LatLng brakpan = new LatLng(-26.2361, 28.3694);
        LatLng butterworth = new LatLng(-32.3261, 28.1495);
        LatLng capeTown = new LatLng(-33.9249, 18.4241);
        LatLng carletonville = new LatLng(-26.3602, 27.4000);
        LatLng dikeni = new LatLng(-32.6958, 26.5303);
        LatLng durban = new LatLng(-29.8587, 31.0218);
        LatLng eastLondon = new LatLng(-33.0292, 27.8546);
        LatLng emalahleni = new LatLng(-25.8776, 29.2126);
        LatLng empangeni = new LatLng(-28.7613, 31.8937);
        LatLng george = new LatLng(-33.9648, 22.4594);
        LatLng germiston = new LatLng(-26.2425, 28.1767);
        LatLng giyani = new LatLng(-23.3086, 30.7180);
        LatLng gqeberha = new LatLng(-33.9608, 25.6022);
        LatLng graaffReinet = new LatLng(-32.2536, 24.5328);
        LatLng hopefield = new LatLng(-33.0667, 18.3500);
        LatLng jagersfontein = new LatLng(-29.7698, 25.4318);
        LatLng johannesburg = new LatLng(-26.2041, 28.0473);
        LatLng kariega = new LatLng(-33.7685, 25.5277);
        LatLng kimberley = new LatLng(-28.7280, 24.7499);
        LatLng klerksdorp = new LatLng(-26.8660, 26.6667);
        LatLng komani = new LatLng(-31.8975, 26.8758);
        LatLng kroonstad = new LatLng(-27.6504, 27.2349);
        LatLng krugersdorp = new LatLng(-26.0858, 27.7753);
        LatLng kuruman = new LatLng(-27.4496, 23.4326);
        LatLng lebowakgomo = new LatLng(-24.2000, 29.5000);
        LatLng mahikeng = new LatLng(-25.8655, 25.6442);
        LatLng makhanda = new LatLng(-33.3061, 26.5324);
        LatLng mbombela = new LatLng(-25.4753, 30.9703);
        LatLng mmabatho = new LatLng(-25.8375, 25.6203);
        LatLng mthatha = new LatLng(-31.5889, 28.7844);
        LatLng musina = new LatLng(-22.3453, 30.0394);
        LatLng newcastle = new LatLng(-27.7573, 29.9315);
        LatLng odendaalsrus = new LatLng(-27.8719, 26.6839);
        LatLng oudshoorn = new LatLng(-33.5859, 22.1995);
        LatLng paarl = new LatLng(-33.7342, 18.9626);
        LatLng parys = new LatLng(-26.8994, 27.4531);
        LatLng phalaborwa = new LatLng(-23.9420, 31.1411);
        LatLng phuthaditjhaba = new LatLng(-28.5267, 28.8150);
        LatLng pietermaritzburg = new LatLng(-29.6006, 30.3794);
        LatLng pinetown = new LatLng(-29.8134, 30.8500);
        LatLng polokwane = new LatLng(-23.9000, 29.4500);
        LatLng portNolloth = new LatLng(-29.2495, 16.8698);
        LatLng potchefstroom = new LatLng(-26.7167, 27.1000);
        LatLng pretoria = new LatLng(-25.7479, 28.2293);
        LatLng qonce = new LatLng(-32.8800, 27.3853);
        LatLng randburg = new LatLng(-26.0936, 28.0054);
        LatLng randfontein = new LatLng(-26.1844, 27.7020);
        LatLng roodepoort = new LatLng(-26.1628, 27.8725);
        LatLng rustenburg = new LatLng(-25.6655, 27.2410);
        LatLng sasolburg = new LatLng(-26.8131, 27.8196);
        LatLng secunda = new LatLng(-26.5161, 29.2017);
        LatLng seshego = new LatLng(-23.8708, 29.4149);
        LatLng sibasa = new LatLng(-22.9186, 30.4799);
        LatLng simonsTown = new LatLng(-34.1979, 18.4321);
        LatLng soweto = new LatLng(-26.2485, 27.8540);
        LatLng springs = new LatLng(-26.2485, 28.4294);
        LatLng stellenbosch = new LatLng(-33.9346, 18.8668);
        LatLng swellendam = new LatLng(-34.0180, 20.4412);
        LatLng thabazimbi = new LatLng(-24.5917, 27.4112);
        LatLng ulundi = new LatLng(-28.3355, 31.4256);
        LatLng umlazi = new LatLng(-29.9258, 30.9023);
        LatLng vanderbijlpark = new LatLng(-26.7066, 27.8374);
        LatLng vereeniging = new LatLng(-26.6748, 27.9261);
        LatLng virginia = new LatLng(-28.1030, 26.8655);
        LatLng welkom = new LatLng(-27.9767, 26.7350);
        LatLng worcester = new LatLng(-33.6469, 19.4486);
        LatLng zwelitsha = new LatLng(-32.9000, 27.4167);
        LatLng umnambithi = new LatLng(-28.5506, 29.7808);
        googleMap.addMarker(new MarkerOptions().position(bellville).title("Bellville"));
        googleMap.addMarker(new MarkerOptions().position(benoni).title("Benoni"));
        googleMap.addMarker(new MarkerOptions().position(bethlehem).title("Bethlehem"));
        googleMap.addMarker(new MarkerOptions().position(bloemfontein).title("Bloemfontein"));
        googleMap.addMarker(new MarkerOptions().position(boksburg).title("Boksburg"));
        googleMap.addMarker(new MarkerOptions().position(brakpan).title("Brakpan"));
        googleMap.addMarker(new MarkerOptions().position(butterworth).title("Butterworth"));
        googleMap.addMarker(new MarkerOptions().position(capeTown).title("Cape Town"));
        googleMap.addMarker(new MarkerOptions().position(carletonville).title("Carletonville"));
        googleMap.addMarker(new MarkerOptions().position(dikeni).title("Dikeni"));
        googleMap.addMarker(new MarkerOptions().position(durban).title("Durban"));
        googleMap.addMarker(new MarkerOptions().position(eastLondon).title("East London"));
        googleMap.addMarker(new MarkerOptions().position(emalahleni).title("Emalahleni"));
        googleMap.addMarker(new MarkerOptions().position(empangeni).title("Empangeni"));
        googleMap.addMarker(new MarkerOptions().position(george).title("George"));
        googleMap.addMarker(new MarkerOptions().position(germiston).title("Germiston"));
        googleMap.addMarker(new MarkerOptions().position(giyani).title("Giyani"));
        googleMap.addMarker(new MarkerOptions().position(gqeberha).title("Gqeberha"));
        googleMap.addMarker(new MarkerOptions().position(graaffReinet).title("Graaff-Reinet"));
        googleMap.addMarker(new MarkerOptions().position(hopefield).title("Hopefield"));
        googleMap.addMarker(new MarkerOptions().position(jagersfontein).title("Jagersfontein"));
        googleMap.addMarker(new MarkerOptions().position(johannesburg).title("Johannesburg"));
        googleMap.addMarker(new MarkerOptions().position(kariega).title("Kariega"));
        googleMap.addMarker(new MarkerOptions().position(kimberley).title("Kimberley"));
        googleMap.addMarker(new MarkerOptions().position(klerksdorp).title("Klerksdorp"));
        googleMap.addMarker(new MarkerOptions().position(komani).title("Komani"));
        googleMap.addMarker(new MarkerOptions().position(kroonstad).title("Kroonstad"));
        googleMap.addMarker(new MarkerOptions().position(krugersdorp).title("Krugersdorp"));
        googleMap.addMarker(new MarkerOptions().position(kuruman).title("Kuruman"));
        googleMap.addMarker(new MarkerOptions().position(lebowakgomo).title("Lebowakgomo"));
        googleMap.addMarker(new MarkerOptions().position(mahikeng).title("Mahikeng"));
        googleMap.addMarker(new MarkerOptions().position(makhanda).title("Makhanda"));
        googleMap.addMarker(new MarkerOptions().position(mbombela).title("Mbombela"));
        googleMap.addMarker(new MarkerOptions().position(mmabatho).title("Mmabatho"));
        googleMap.addMarker(new MarkerOptions().position(mthatha).title("Mthatha"));
        googleMap.addMarker(new MarkerOptions().position(musina).title("Musina"));
        googleMap.addMarker(new MarkerOptions().position(newcastle).title("Newcastle"));
        googleMap.addMarker(new MarkerOptions().position(odendaalsrus).title("Odendaalsrus"));
        googleMap.addMarker(new MarkerOptions().position(oudshoorn).title("Oudshoorn"));
        googleMap.addMarker(new MarkerOptions().position(paarl).title("Paarl"));
        googleMap.addMarker(new MarkerOptions().position(parys).title("Parys"));
        googleMap.addMarker(new MarkerOptions().position(phalaborwa).title("Phalaborwa"));
        googleMap.addMarker(new MarkerOptions().position(phuthaditjhaba).title("Phuthaditjhaba"));
        googleMap.addMarker(new MarkerOptions().position(pietermaritzburg).title("Pietermaritzburg"));
        googleMap.addMarker(new MarkerOptions().position(pinetown).title("Pinetown"));
        googleMap.addMarker(new MarkerOptions().position(polokwane).title("Polokwane"));
        googleMap.addMarker(new MarkerOptions().position(portNolloth).title("Port Nolloth"));
        googleMap.addMarker(new MarkerOptions().position(potchefstroom).title("Potchefstroom"));
        googleMap.addMarker(new MarkerOptions().position(pretoria).title("Pretoria"));
        googleMap.addMarker(new MarkerOptions().position(qonce).title("Qonce"));
        googleMap.addMarker(new MarkerOptions().position(randburg).title("Randburg"));
        googleMap.addMarker(new MarkerOptions().position(randfontein).title("Randfontein"));
        googleMap.addMarker(new MarkerOptions().position(roodepoort).title("Roodepoort"));
        googleMap.addMarker(new MarkerOptions().position(rustenburg).title("Rustenburg"));
        googleMap.addMarker(new MarkerOptions().position(sasolburg).title("Sasolburg"));
        googleMap.addMarker(new MarkerOptions().position(secunda).title("Secunda"));
        googleMap.addMarker(new MarkerOptions().position(seshego).title("Seshego"));
        googleMap.addMarker(new MarkerOptions().position(sibasa).title("Sibasa"));
        googleMap.addMarker(new MarkerOptions().position(simonsTown).title("Simon's Town"));
        googleMap.addMarker(new MarkerOptions().position(soweto).title("Soweto"));
        googleMap.addMarker(new MarkerOptions().position(springs).title("Springs"));
        googleMap.addMarker(new MarkerOptions().position(stellenbosch).title("Stellenbosch"));
        googleMap.addMarker(new MarkerOptions().position(swellendam).title("Swellendam"));
        googleMap.addMarker(new MarkerOptions().position(thabazimbi).title("Thabazimbi"));
        googleMap.addMarker(new MarkerOptions().position(ulundi).title("Ulundi"));
        googleMap.addMarker(new MarkerOptions().position(umlazi).title("Umlazi"));
        googleMap.addMarker(new MarkerOptions().position(vanderbijlpark).title("Vanderbijlpark"));
        googleMap.addMarker(new MarkerOptions().position(vereeniging).title("Vereeniging"));
        googleMap.addMarker(new MarkerOptions().position(virginia).title("Virginia"));
        googleMap.addMarker(new MarkerOptions().position(welkom).title("Welkom"));
        googleMap.addMarker(new MarkerOptions().position(worcester).title("Worcester"));
        googleMap.addMarker(new MarkerOptions().position(zwelitsha).title("Zwelitsha"));
        googleMap.addMarker(new MarkerOptions().position(umnambithi).title("Umnambithi"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(johannesburg, 5));

        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String cityName = marker.getTitle();
                Bundle bundle = new Bundle();
                bundle.putString("cityName", cityName);

                ClientHomeFragment clientHomeFragment = new ClientHomeFragment();
                clientHomeFragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.google_map, clientHomeFragment)
                        .addToBackStack(null)
                        .commit();

                return true;
            }
        });
    }
}