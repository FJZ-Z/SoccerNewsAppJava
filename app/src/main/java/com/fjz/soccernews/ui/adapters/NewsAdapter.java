package com.fjz.soccernews.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fjz.soccernews.R;
import com.fjz.soccernews.databinding.NewsItemBinding;
import com.fjz.soccernews.domain.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final List<News> news;
    private final FavoriteListener favoriteListener;
    public NewsAdapter(List<News> news, FavoriteListener favoriteListener){
        this.news = news; this.favoriteListener = favoriteListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NewsItemBinding binding = NewsItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        News news = this.news.get(position);
        holder.binding.tvTittle.setText(news.tittle);
        holder.binding.tvDescription.setText(news.description);
        Picasso.get().load(news.image).fit().into(holder.binding.ivThumbnail);
        holder.binding.btOpenlink.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(news.link));
            context.startActivity(i);
        });
        holder.binding.ivShare.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT,news.link);
            context.startActivity(Intent.createChooser(i,"Share"));
        });
        holder.binding.ivFavorites.setOnClickListener(view -> {
            news.favorite = !news.favorite;
            this.favoriteListener.onFavorite(news);
            notifyItemChanged(position);
        });
        int favoritecolor = news.favorite? R.color.favorite_active:R.color.favorite_inactive;
        //holder.binding.ivFavorites.setColorFilter(context.getResources().getColor(favoritecolor));
        holder.binding.ivFavorites.setColorFilter(ContextCompat.getColor(context,favoritecolor));
    }

    @Override
    public int getItemCount() {
        return this.news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final NewsItemBinding binding;
        public ViewHolder(NewsItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface FavoriteListener {
        void onFavorite(News news);
    }
}
