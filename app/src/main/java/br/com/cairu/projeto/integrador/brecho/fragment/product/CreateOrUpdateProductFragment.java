package br.com.cairu.projeto.integrador.brecho.fragment.product;

import android.app.DownloadManager;
import android.content.Context;
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

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.config.ApiClient;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.generic.MessageResponse;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductRequestDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import br.com.cairu.projeto.integrador.brecho.models.Category;
import br.com.cairu.projeto.integrador.brecho.models.File;
import br.com.cairu.projeto.integrador.brecho.services.ProductService;
import br.com.cairu.projeto.integrador.brecho.utils.Generic;
import br.com.cairu.projeto.integrador.brecho.utils.InitToolbar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrUpdateProductFragment extends Fragment {

    private Generic generic;
    private ProgressBar progressBar;
    private Button saveProduct;
    private ProductService productService;
    private ProductResponseDTO productResponseDTO;
    private Spinner inputCategoryProduct;
    private boolean isUpdate;
    private boolean isActiveEmpty = false;
    private boolean isActive;
    private EditText name;
    private EditText description;
    private CurrencyEditText price;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private final List<Uri> imageUris = new ArrayList<>();
    private ImageView imageOne, imageTwo, imageThree;
    private ImageButton deleteImageOne, deleteImageTwo, deleteImageThree;
    private boolean categoryProductEmpty = false;
    private Long categoryId;
    private Spinner inputProductIsActive;
    private ArrayAdapter<CharSequence> adapterIsActive;
    private ArrayAdapter<CategoryResponseDTO> adapterCategory;
    private List<CategoryResponseDTO> categories;

    public CreateOrUpdateProductFragment() {
    }

    public CreateOrUpdateProductFragment(ProductResponseDTO productResponseDTO, boolean isUpdate, List<CategoryResponseDTO> categories) {
        this.productResponseDTO = productResponseDTO;
        this.isUpdate = isUpdate;
        this.categories = categories;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_or_update_product, container, false);

        inputCategoryProduct = view.findViewById(R.id.inputCategoryProduct);
        inputProductIsActive = view.findViewById(R.id.inputProductIsActive);

        adapterIsActive = ArrayAdapter.createFromResource(requireContext(), R.array.trueOrFalse, android.R.layout.simple_spinner_item);
        adapterIsActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputProductIsActive.setAdapter(adapterIsActive);

        adapterCategory = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputCategoryProduct.setAdapter(adapterCategory);

        inputProductIsActive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();

                isActiveEmpty = position != 0;

                isActive = selectedItem.equals("Sim") && position > 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isActiveEmpty = false;
            }
        });

        inputCategoryProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CategoryResponseDTO selectedItem = (CategoryResponseDTO) parent.getItemAtPosition(position);
                categoryId = selectedItem.getId();

                categoryProductEmpty = position != 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoryProductEmpty = false;
            }
        });

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

        new InitToolbar().toolbar((AppCompatActivity) requireActivity(), toolbar, getActivity(), false);

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

        if (this.isUpdate) {
            name.setText(this.productResponseDTO.getName());
            description.setText(this.productResponseDTO.getDescription());
            price.setText(this.productResponseDTO.getPrice());

            inputCategoryProduct.setSelection(adapterCategory.getPosition(new CategoryResponseDTO(this.productResponseDTO.getCategory().getId(), this.productResponseDTO.getCategory().getName())));

            if (this.productResponseDTO.isActive()) {
                inputProductIsActive.setSelection(adapterIsActive.getPosition("Sim"));
            } else {
                inputProductIsActive.setSelection(adapterIsActive.getPosition("Não"));
            }

            for (File file : this.productResponseDTO.getFiles()) {
                Uri imageUri = Uri.parse("http://10.0.2.2:8080/" + file.getUrl());
                imageUris.add(imageUri);

                Picasso.get().load(imageUris.get(0)).into(imageOne);

                if (imageUris.size() > 1) {
                    Picasso.get().load(imageUris.get(1)).into(imageTwo);
                }
                if (imageUris.size() > 2) {
                    Picasso.get().load(imageUris.get(2)).into(imageThree);
                }
            }

            displaySelectedImages();

            saveProduct.setText("ATUALIZAR");
            TextView titleProduct = view.findViewById(R.id.titleProductToolbar);
            titleProduct.setText("Atualizar Produto");
        }

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

                boolean verify = generic.empty(editTexts, null, null);

                if (!isActiveEmpty) {
                    Toast.makeText(getActivity(), "Por favor, selecione uma opção no campo Admin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!categoryProductEmpty) {
                    Toast.makeText(getActivity(), "Por favor, selecione uma opção no campo Categoria", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!verify) {
                    progressBar.setVisibility(View.VISIBLE);
                    saveProduct.setEnabled(false);
                    try {
                        createdOrUpdate();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
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

    private final ActivityResultLauncher<Intent> pickImagesLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
            handleImageSelection(result.getData());
        }
    });

    public void handleImageSelection(Intent intent) {
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
            if (isUpdate) {
                Picasso.get().load(imageUris.get(0)).into(imageOne);
            }
        }

        if (imageUris.size() > 1) {
            deleteImageTwo.setVisibility(View.VISIBLE);
            imageTwo.setImageURI(imageUris.get(1));
            imageTwo.setVisibility(View.VISIBLE);

            if (isUpdate) {
                Picasso.get().load(imageUris.get(1)).into(imageTwo);
            }
        }

        if (imageUris.size() > 2) {
            deleteImageThree.setVisibility(View.VISIBLE);
            imageThree.setImageURI(imageUris.get(2));
            imageThree.setVisibility(View.VISIBLE);

            if (isUpdate) {
                Picasso.get().load(imageUris.get(2)).into(imageThree);
            }
        }
    }

    public void createdOrUpdate() throws IOException {
        if (this.isUpdate) {
            this.update();
        } else {
            this.register();
        }
    }

    public static MultipartBody.Part prepareFilePart(Context context, String partName, Uri fileUri, String realPath) {
        java.io.File file = new java.io.File(realPath);

        RequestBody requestFile = RequestBody.create(file, MediaType.parse(Objects.requireNonNull(context.getContentResolver().getType(fileUri))));
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private String getRealPathFromUri(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    public void register() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        Category category = new Category();

        productRequestDTO.setName(name.getText().toString());
        productRequestDTO.setDescription(description.getText().toString());
        productRequestDTO.setPrice(String.valueOf(price.getRawValue()));
        productRequestDTO.setActive(isActive);
        category.setId(categoryId);
        productRequestDTO.setCategory(category);

        ArrayList<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : imageUris) {
            parts.add(prepareFilePart(requireContext(), "images", uri, getRealPathFromUri(requireContext(), uri)));
        }

        productService.register(productRequestDTO, parts).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();

                    Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProductFragment()).addToBackStack(null).commit();
                } else {
                    try {
                        MessageResponse messageResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                        Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                progressBar.setVisibility(View.GONE);
                saveProduct.setEnabled(true);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                saveProduct.setEnabled(true);
            }
        });
    }


//    public String downloadImageNew(String filename, String downloadUrlOfImage) {
//        try {
//            DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
//            Uri downloadUri = Uri.parse(downloadUrlOfImage);
//            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                    .setAllowedOverRoaming(false)
//                    .setTitle(filename)
//                    .setMimeType("image/png") // Your file type. You can use this code to download other file types also.
//                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, java.io.File.separator + filename + ".png");
//            dm.enqueue(request);
//
//            System.out.println(downloadUri);
//
//            return getRealPathFromUri(requireContext(), downloadUri);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    public void update() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        Category category = new Category();
        File file = new File();

        productRequestDTO.setName(name.getText().toString());
        productRequestDTO.setDescription(description.getText().toString());
        productRequestDTO.setPrice(String.valueOf(price.getRawValue()));
        productRequestDTO.setActive(isActive);
        category.setId(categoryId);
        productRequestDTO.setCategory(category);

        ArrayList<MultipartBody.Part> parts = new ArrayList<>();
        List<File> urls = new ArrayList<>();
        for (Uri uri : imageUris) {
            if (Objects.requireNonNull(uri.getPath()).contains("/public")) {
                file.setUrl(uri.getPath().substring(1));
                urls.add(file);
            } else {
                parts.add(prepareFilePart(requireContext(), "images", uri, getRealPathFromUri(requireContext(), uri)));
            }
        }

        System.out.println(urls.size());

//        productService.update(this.productResponseDTO.getId(), productRequestDTO, parts, urls).enqueue(new Callback<MessageResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
//                if (response.isSuccessful()) {
//                    MessageResponse messageResponse = response.body();
//
//                    Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProductFragment()).addToBackStack(null).commit();
//                } else {
//                    try {
//                        MessageResponse messageResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
//                        Toast.makeText(getActivity(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//                progressBar.setVisibility(View.GONE);
//                saveProduct.setEnabled(true);
//            }
//
//            @Override
//            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
//                Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//                saveProduct.setEnabled(true);
//            }
//        });
    }
}

