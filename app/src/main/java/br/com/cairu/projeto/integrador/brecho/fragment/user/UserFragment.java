package br.com.cairu.projeto.integrador.brecho.fragment.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.adapter.UserAdapter;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.dtos.user.UserResponseDTO;
import br.com.cairu.projeto.integrador.brecho.fragment.login.LoginFragment;
import br.com.cairu.projeto.integrador.brecho.services.UserService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment implements UserAdapter.OnItemDeleteListener {

    private ProgressBar progressBar;
    private UserAdapter userAdapter;
    private UserService userService;
    private Generic generic;
    private List<UserResponseDTO> itemList;

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycleViewUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        generic = new Generic(requireContext());

        itemList = new ArrayList<>();

        userService = new ApiClient().getClient(getActivity()).create(UserService.class);

        userAdapter = new UserAdapter(itemList, this);
        recyclerView.setAdapter(userAdapter);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        this.all();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.createOrUpdate(view);
    }

    public void all() {
        userService.all().enqueue(new Callback<List<UserResponseDTO>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<UserResponseDTO>> call, @NonNull Response<List<UserResponseDTO>> response) {
                if (response.isSuccessful()) {
                    List<UserResponseDTO> userResponseDTO = response.body();
                    itemList.clear();
                    itemList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

                if (response.code() == 403) {
                    generic.clear();

                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Token expirado faça login novamente.", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout, new LoginFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserResponseDTO>> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    public void createOrUpdate(View view) {
        Button buttonTextView = view.findViewById(R.id.createUser);

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new CreateOrUpdateUserFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void delete(Long id, int position) {
        userService.delete(id).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();

                    itemList.remove(position);
                    userAdapter.notifyItemRemoved(position);

                    Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        MessageResponse errorResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                        Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void index(Long id) {
        userService.index(id).enqueue(new Callback<UserResponseDTO>() {
            @Override
            public void onResponse(Call<UserResponseDTO> call, Response<UserResponseDTO> response) {
                if (response.isSuccessful()) {
                    UserResponseDTO userResponseDTO = response.body();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new CreateOrUpdateUserFragment(userResponseDTO, true))
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<UserResponseDTO> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpdateItem(int position) {
        UserResponseDTO item = itemList.get(position);

        this.index(item.getId());
    }

    @Override
    public void onItemDelete(int position) {
        UserResponseDTO item = itemList.get(position);
        this.confirmDelete(item.getId(), position);
    }

    public void confirmDelete(Long id, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Você deseja realmente apagar esse usuário ?");
        builder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(id, position);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}