package br.com.cairu.projeto.integrador.brecho.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnItemDeleteListener {
        void onUpdateItem(int position);

        void onItemDelete(int position);

        void onItemClick(String category, int position);
    }

    private List<String> imageUrls;
    private final ProductAdapter.OnItemDeleteListener listener;

    public ProductAdapter(List<String> list, ProductAdapter.OnItemDeleteListener listener) {
        this.imageUrls = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview_filter_product, parent, false);

        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        System.out.println(imageUrl);

        Picasso.get().load(imageUrl).into(holder.gridImage);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView gridImage;

        public ViewHolder(View view) {
            super(view);
            gridImage = itemView.findViewById(R.id.agoravaiImagem);
        }
    }
}
