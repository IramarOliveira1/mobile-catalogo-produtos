package br.com.cairu.projeto.integrador.brecho.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.dtos.HomeResponseDTO;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeResponseDTO> list;

    public HomeAdapter(List<HomeResponseDTO> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recicle_view_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeResponseDTO item = list.get(position);

        holder.nameProduct.setText(item.getName().toUpperCase());
        holder.countClick.setText(String.valueOf(item.getCountClick() + " clicks"));
//        Picasso.get().load("http://10.0.2.2:8080/" + item.getFile().getUrl()).into(holder.images);
        Picasso.get().load("http://10.0.2.2:8080/public/images/1714138911150.png").into(holder.images);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
