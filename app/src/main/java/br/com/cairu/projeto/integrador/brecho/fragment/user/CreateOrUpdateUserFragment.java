package br.com.cairu.projeto.integrador.brecho.fragment.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.cairu.projeto.integrador.brecho.R;

public class CreateOrUpdateUserFragment extends Fragment {

    public CreateOrUpdateUserFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_or_update_user, container, false);
    }
}