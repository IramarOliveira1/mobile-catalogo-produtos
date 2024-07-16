package br.com.cairu.projeto.integrador.brecho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.dtos.home.HomeResponseDTO;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductAndCategory;
import br.com.cairu.projeto.integrador.brecho.dtos.product.ProductResponseDTO;
import br.com.cairu.projeto.integrador.brecho.models.Product;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private final List<Product> list;

    public HomeAdapter(List<Product> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameProduct;

        public TextView countClick;

        public ImageView images;

        public ViewHolder(View view) {
            super(view);
            nameProduct = itemView.findViewById(R.id.nameProductHome);
            countClick = itemView.findViewById(R.id.countClickView);
            images = itemView.findViewById(R.id.imageProductHome);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = list.get(position);

        holder.nameProduct.setText(item.getName().toUpperCase());
        holder.countClick.setText(String.valueOf(item.getCountClick() + " click"));
        Picasso.get().load("http://10.6.18.136:8080/" + item.getFiles().get(0).getUrl()).into(holder.images);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
