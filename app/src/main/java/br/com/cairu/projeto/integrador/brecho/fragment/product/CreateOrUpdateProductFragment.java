package br.com.cairu.projeto.integrador.brecho.fragment.product;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import br.com.cairu.projeto.integrador.brecho.services.CategoryService;
import br.com.cairu.projeto.integrador.brecho.services.ProductService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import br.com.cairu.projeto.integrador.brecho.utils.InitToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrUpdateProductFragment extends Fragment {

    private Generic generic;
    private ProgressBar progressBar;
    private Button saveProduct;
    private ProductService productService;
    private ProductResponseDTO productResponseDTO;
    private boolean isUpdate;
    private EditText name;
    private EditText description;
    private CurrencyEditText price;
    private Spinner inputCategoryProduct;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private List<Uri> imageUris = new ArrayList<>();
    private ImageView imageOne, imageTwo, imageThree;
    private ImageButton deleteImageOne, deleteImageTwo, deleteImageThree;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_or_update_product, container, false);

        inputCategoryProduct = view.findViewById(R.id.inputCategoryProduct);
        Spinner inputProductIsActive = view.findViewById(R.id.inputProductIsActive);

        ArrayAdapter<CharSequence> adapterIsActive = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.trueOrFalse,
                android.R.layout.simple_spinner_item
        );
        adapterIsActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputProductIsActive.setAdapter(adapterIsActive);

        view.findViewById(R.id.deleteImageOne).setOnClickListener(v -> removeImage(0));
        view.findViewById(R.id.deleteImageTwo).setOnClickListener(v -> removeImage(1));
        view.findViewById(R.id.deleteImageThree).setOnClickListener(v -> removeImage(2));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generic = new Generic(requireContext());

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        new InitToolbar().toolbar((AppCompatActivity) requireActivity(), toolbar, getActivity());

        productService = new ApiClient().getClient(getActivity()).create(ProductService.class);

        progressBar = view.findViewById(R.id.progressBar);

        name = view.findViewById(R.id.inputProductName);
        description = view.findViewById(R.id.inputProductDescription);
        price = view.findViewById(R.id.inputProductPrice);

        price.setLocale(new Locale("pt", "BR"));

        imageOne = view.findViewById(R.id.imageOne);
        imageTwo = view.findViewById(R.id.imageTwo);
        imageThree = view.findViewById(R.id.imageThree);

        saveProduct = view.findViewById(R.id.btnSaveProduct);

        deleteImageOne = view.findViewById(R.id.deleteImageOne);
        deleteImageTwo = view.findViewById(R.id.deleteImageTwo);
        deleteImageThree = view.findViewById(R.id.deleteImageThree);
        Button buttonSelectImages = view.findViewById(R.id.buttonSelectImages);

        buttonSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<EditText> editTexts = new ArrayList<>();
                ArrayList<CurrencyEditText> currencyEditTexts = new ArrayList<>();

                editTexts.add(name);
                editTexts.add(description);
                currencyEditTexts.add(price);

                boolean verify = generic.empty(editTexts, null , currencyEditTexts);

//                if (!isAdminEmpty) {
//                    Toast.makeText(getActivity(), "Por favor, selecione uma opção no campo Admin", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (!verify) {
                    progressBar.setVisibility(View.VISIBLE);
                    saveProduct.setEnabled(false);
                    createdOrUpdate();
                }
            }
        });

        getCategory();
    }

    public void getCategory() {

        CategoryService categoryService = new ApiClient().getClient(getActivity()).create(CategoryService.class);
        categoryService.all().enqueue(new Callback<List<CategoryResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryResponseDTO>> call, Response<List<CategoryResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryResponseDTO> categoriesApi = response.body();

                    List<CategoryResponseDTO> categories = new ArrayList<>();
                    CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

                    categoryResponseDTO.setId(0L);
                    categoryResponseDTO.setName("Selecione");
                    categories.add(categoryResponseDTO);
                    categories.addAll(categoriesApi);

                    ArrayAdapter<CategoryResponseDTO> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    inputCategoryProduct.setAdapter(adapter);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponseDTO>> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImagesLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> pickImagesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    handleImageSelection(result.getData());
                }
            }
    );

    public void handleImageSelection(Intent intent) {
//        imageUris.clear();
        if (intent.getClipData() != null) {
            int count = intent.getClipData().getItemCount();
            for (int i = 0; i < count && i < 3; i++) {
                Uri imageUri = intent.getClipData().getItemAt(i).getUri();
                if (isValidImageType(imageUri)) {
                    imageUris.add(imageUri);
                } else {
                    Toast.makeText(getContext(), "Only JPG, JPEG and PNG images are allowed", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (intent.getData() != null) {
            Uri imageUri = intent.getData();
            if (isValidImageType(imageUri)) {
                imageUris.add(intent.getData());
            } else {
                Toast.makeText(getContext(), "Only JPG, JPEG and PNG images are allowed", Toast.LENGTH_SHORT).show();
            }
        }
        displaySelectedImages();
    }

    private boolean isValidImageType(Uri uri) {
        String mimeType = getContext().getContentResolver().getType(uri);
        long fileSize = getFileSize(uri);

        if (!("image/jpeg".equals(mimeType) || "image/png".equals(mimeType) || "image/jpg".equals(mimeType))) {
            Toast.makeText(getContext(), "Somente imagens JPG, JPEG e PNG são permitidas.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fileSize > MAX_FILE_SIZE) {
            Toast.makeText(getContext(), "O tamanho do arquivo deve ser inferior a 5 MB", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void removeImage(int index) {
        if (index < imageUris.size()) {
            imageUris.remove(index);
            displaySelectedImages();
        }
    }

    private long getFileSize(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
        cursor.moveToFirst();
        long size = cursor.getLong(sizeIndex);
        cursor.close();
        return size;
    }

    private void displaySelectedImages() {
        deleteImageOne.setVisibility(View.GONE);
        deleteImageTwo.setVisibility(View.GONE);
        deleteImageThree.setVisibility(View.GONE);
        imageOne.setVisibility(View.GONE);
        imageTwo.setVisibility(View.GONE);
        imageThree.setVisibility(View.GONE);
        imageOne.setImageURI(null);
        imageTwo.setImageURI(null);
        imageThree.setImageURI(null);
        if (!imageUris.isEmpty()) {
            deleteImageOne.setVisibility(View.VISIBLE);
            imageOne.setImageURI(imageUris.get(0));
            imageOne.setVisibility(View.VISIBLE);
        }

        if (imageUris.size() > 1) {
            deleteImageTwo.setVisibility(View.VISIBLE);
            imageTwo.setImageURI(imageUris.get(1));
            imageTwo.setVisibility(View.VISIBLE);
        }

        if (imageUris.size() > 2) {
            deleteImageThree.setVisibility(View.VISIBLE);
            imageThree.setImageURI(imageUris.get(2));
            imageThree.setVisibility(View.VISIBLE);
        }
    }

    public void createdOrUpdate() {
        if (this.isUpdate) {
            System.out.println("cair aqui");
//            this.update();
        } else {
            this.register();
        }
    }

    public void register(){

        System.out.println("CAIR AQUI");

        progressBar.setVisibility(View.GONE);
        saveProduct.setEnabled(true);
    }
}

