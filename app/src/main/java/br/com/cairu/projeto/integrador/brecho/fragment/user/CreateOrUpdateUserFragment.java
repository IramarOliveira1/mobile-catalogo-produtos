package br.com.cairu.projeto.integrador.brecho.fragment.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.santalu.maskara.widget.MaskEditText;

import java.io.IOException;
import java.util.ArrayList;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.dtos.user.UserRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.user.UserResponseDTO;
import br.com.cairu.projeto.integrador.brecho.services.UserService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import br.com.cairu.projeto.integrador.brecho.utils.InitToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrUpdateUserFragment extends Fragment {

    private Generic generic;
    private ProgressBar progressBar;
    private Button saveUser;
    private UserService userService;
    private UserResponseDTO userResponseDTO;
    private EditText inputUserName;
    private EditText inputUserEmail;
    private MaskEditText inputUserCpf;
    private EditText inputUserPassword;
    private MaskEditText inputUserPhone;
    private boolean isAdmin;
    private boolean isAdminEmpty = false;
    private Spinner inputUserIsAdmin;
    private boolean isUpdate;

    private ArrayAdapter<CharSequence> adapter;

    public CreateOrUpdateUserFragment() {
    }

    public CreateOrUpdateUserFragment(UserResponseDTO userResponseDTO, boolean isUpdate) {
        this.userResponseDTO = userResponseDTO;
        this.isUpdate = isUpdate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_or_update_user, container, false);

        inputUserIsAdmin = view.findViewById(R.id.inputUserIsAdmin);

        adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.isAdmin,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputUserIsAdmin.setAdapter(adapter);

        inputUserIsAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();

                isAdminEmpty = position != 0;

                if (selectedItem.equals("Sim") && position > 0) {
                    isAdmin = true;
                } else {
                    isAdmin = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isAdminEmpty = false;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        new InitToolbar().toolbar((AppCompatActivity) requireActivity(), toolbar, getActivity());

        userService = new ApiClient().getClient(getActivity()).create(UserService.class);

        saveUser = view.findViewById(R.id.btnSaveUser);
        inputUserName = view.findViewById(R.id.inputUserName);
        inputUserEmail = view.findViewById(R.id.inputUserEmail);
        inputUserCpf = view.findViewById(R.id.inputUserCpf);
        inputUserPassword = view.findViewById(R.id.inputUserPassword);
        inputUserPhone = view.findViewById(R.id.inputUserPhone);
        generic = new Generic(getActivity());
        progressBar = view.findViewById(R.id.progressBar);

        if (this.isUpdate) {
            inputUserName.setText(this.userResponseDTO.getName());
            inputUserEmail.setText(this.userResponseDTO.getEmail());
            inputUserCpf.setText(this.userResponseDTO.getCpf());
            inputUserPhone.setText(this.userResponseDTO.getPhone());

            System.out.println(this.userResponseDTO.isAdmin());
            if (this.userResponseDTO.isAdmin()) {
                inputUserIsAdmin.setSelection(adapter.getPosition("Sim"));
            } else {
                inputUserIsAdmin.setSelection(adapter.getPosition("Não"));
            }

            saveUser.setText("ATUALIZAR");
            TextView titleUser = view.findViewById(R.id.titleUserToolbar);
            titleUser.setText("Atualizar Usuário");
        }

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<EditText> editTexts = new ArrayList<>();
                ArrayList<MaskEditText> maskEditTexts = new ArrayList<>();

                editTexts.add(inputUserName);
                editTexts.add(inputUserEmail);
                editTexts.add(inputUserPassword);
                maskEditTexts.add(inputUserPhone);
                maskEditTexts.add(inputUserCpf);

                boolean verify = generic.empty(editTexts, maskEditTexts);

                if (!isAdminEmpty) {
                    Toast.makeText(getActivity(), "Por favor, selecione uma opção no campo Admin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!verify) {
                    progressBar.setVisibility(View.VISIBLE);
                    saveUser.setEnabled(false);
                    createdOrUpdate();
                }
            }
        });
    }

    public void createdOrUpdate() {
        if (this.isUpdate) {
            this.update();
        } else {
            this.register();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void register() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();

        userRequestDTO.setName(inputUserName.getText().toString());
        userRequestDTO.setEmail(inputUserEmail.getText().toString());
        userRequestDTO.setCpf(inputUserCpf.getText().toString());
        userRequestDTO.setAdmin(isAdmin);
        userRequestDTO.setPassword(inputUserPassword.getText().toString());
        userRequestDTO.setPhone(inputUserPhone.getText().toString());

        userService.register(userRequestDTO).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();

                    Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new UserFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    try {
                        MessageResponse messageResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                        Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                progressBar.setVisibility(View.GONE);
                saveUser.setEnabled(false);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                saveUser.setEnabled(true);
            }
        });
    }

    public void update() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();

        userRequestDTO.setName(inputUserName.getText().toString());
        userRequestDTO.setEmail(inputUserEmail.getText().toString());
        userRequestDTO.setCpf(inputUserCpf.getText().toString());
        userRequestDTO.setAdmin(isAdmin);
        userRequestDTO.setPassword(inputUserPassword.getText().toString());
        userRequestDTO.setPhone(inputUserPhone.getText().toString());
        userService.update(this.userResponseDTO.getId(), userRequestDTO).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();

                    Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new UserFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    try {
                        MessageResponse messageResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                        System.out.println("ERROR " + messageResponse.getMessage());
                        Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                progressBar.setVisibility(View.GONE);
                saveUser.setEnabled(true);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                saveUser.setEnabled(true);
            }
        });
    }
}