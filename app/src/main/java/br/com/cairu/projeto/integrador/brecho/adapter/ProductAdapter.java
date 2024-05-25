package br.com.cairu.projeto.integrador.brecho.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductAndCategory;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnItemDeleteListener {
        void onUpdateItem(int position);

        void onItemDelete(int position);

        void onItemClick(String category, int position);
    }

    private List<ProductResponseDTO> list;

    private List<ProductResponseDTO> filteredItemList;
    private final ProductAdapter.OnItemDeleteListener listener;

    public ProductAdapter(List<ProductResponseDTO> list, OnItemDeleteListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductResponseDTO item = list.get(position);

        holder.name.setText(item.getName());
        holder.price.setText("R$ " + item.getPrice());
        holder.btnDelete.setId(item.getId().intValue());
        holder.btnUpdate.setId(item.getId().intValue());

        Picasso.get().load("http://10.0.2.2:8080/" + item.getFiles().get(0).getUrl()).into(holder.images);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView price;
        public Button btnDelete;
        public Button btnUpdate;
        public ImageView images;

        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.nameProduct);
            price = itemView.findViewById(R.id.priceProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
            btnUpdate = itemView.findViewById(R.id.btnUpdateProduct);
            images = itemView.findViewById(R.id.imageProduct);
        }
    }
}
