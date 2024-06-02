package br.com.cairu.projeto.integrador.brecho.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnItemDeleteListener {
        void onUpdateItem(int position);

        void onItemDelete(int position);

        void onItemClick(String category, int position);
    }

    private final List<ProductResponseDTO> list;
    private final List<ProductResponseDTO> listName;
    private final OnItemDeleteListener listener;

    private final boolean catalog;

    public ProductAdapter(List<ProductResponseDTO> list, OnItemDeleteListener listener, boolean catalog) {
        this.list = list;
        this.listener = listener;
        this.listName = new ArrayList<>(list);
        this.catalog = catalog;
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

        Locale locale = new Locale("pt", "BR");

        String convertMoney = NumberFormat.getCurrencyInstance(locale).format(Double.parseDouble(item.getPrice()));

        holder.name.setText(item.getName());
        holder.price.setText(convertMoney);
        holder.btnDelete.setId(item.getId().intValue());
        holder.btnUpdate.setId(item.getId().intValue());

        Picasso.get().load("http://10.0.2.2:8080/" + item.getFiles().get(0).getUrl()).into(holder.images);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());

        if (listName.isEmpty()) {
            listName.addAll(list);
        }

        list.clear();
        if (text.isEmpty()) {
            list.addAll(listName);
        } else {
            String finalText = text;
            list.addAll(listName.stream()
                    .filter(product -> product.getName().toLowerCase(Locale.getDefault()).contains(finalText))
                    .collect(Collectors.toList()));
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView price;
        public TextView description;
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
            LinearLayout recyclerViewClick = itemView.findViewById(R.id.recyclerViewClick);

            if (catalog) {
                btnDelete.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.GONE);

                recyclerViewClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onUpdateItem(getAdapterPosition());
                    }
                });
            } else {
                btnDelete.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.VISIBLE);
            }

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUpdateItem(getAdapterPosition());
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemDelete(getAdapterPosition());
                }
            });
        }
    }
}
