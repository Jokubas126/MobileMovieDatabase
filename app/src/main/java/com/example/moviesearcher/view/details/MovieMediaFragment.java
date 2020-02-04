package com.example.moviesearcher.view.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviesearcher.R;
import com.example.moviesearcher.model.data.Video;
import com.example.moviesearcher.util.BundleUtil;
import com.example.moviesearcher.util.YoutubeUtil;
import com.example.moviesearcher.viewmodel.MovieMediaViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieMediaFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.information_layout) LinearLayout informationLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.video_name) TextView trailerName;
    @BindView(R.id.youtube_fragment) FrameLayout youtubeFrame;
    @BindView(R.id.poster_recycler_view) RecyclerView posterView;
    @BindView(R.id.backdrop_recycler_view) RecyclerView backdropView;

    private YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
    private MovieMediaViewModel viewModel;

    private ImageAdapter posterAdapter = new ImageAdapter();
    private ImageAdapter backdropAdapter = new ImageAdapter();

    public MovieMediaFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_media, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewModel = ViewModelProviders.of(this).get(MovieMediaViewModel.class);
        viewModel.fetch(getActivity(), getArguments());

        posterView.setLayoutManager(new GridLayoutManager(getContext(),2));
        posterView.setItemAnimator(new DefaultItemAnimator());
        posterView.setAdapter(posterAdapter);

        backdropView.setLayoutManager(new LinearLayoutManager(getContext()));
        backdropView.setItemAnimator(new DefaultItemAnimator());
        backdropView.setAdapter(backdropAdapter);

        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getTrailer().observe(getViewLifecycleOwner(), trailer -> {
            if (trailer != null){
                trailerName.setText(trailer.getName());
                if (getActivity() != null){
                    getActivity().getFragmentManager().beginTransaction().replace(youtubeFrame.getId(), youTubePlayerFragment).commit();
                    initializeYoutubePlayer(trailer.getKey());
                }
            }
        });
        viewModel.getPosterList().observe(getViewLifecycleOwner(), posterList -> {
            if (posterList != null){
                posterAdapter.updateImagePathList(posterList);
            }
        });
        viewModel.getBackdropList().observe(getViewLifecycleOwner(), backdropList -> {
            if (backdropList != null){
                backdropAdapter.updateImagePathList(backdropList);
            }
        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null){
                progressBar.setVisibility(isLoading? View.VISIBLE : View.GONE);
                informationLayout.setVisibility(isLoading? View.GONE : View.VISIBLE);
            }
        });
    }

    private void initializeYoutubePlayer(String key){
        youTubePlayerFragment.initialize(YoutubeUtil.API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setFullscreen(false);
                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.cueVideo(key);
                Log.d("YoutubePlayer", "onInitializationFailure: successfully to initialized");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("YoutubePlayer", "onInitializationFailure: failed to initialize");
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.overview_menu_item:
                if (getArguments() != null) {
                    NavDirections action = MovieMediaFragmentDirections.actionMovieOverview(getArguments().getInt(BundleUtil.KEY_MOVIE_ID));
                    Navigation.findNavController(bottomNavigationView).navigate(action);
                }
                break;

            case R.id.cast_menu_item:
                if (getArguments() != null) {
                    NavDirections action = MovieMediaFragmentDirections.actionMovieCast(getArguments().getInt(BundleUtil.KEY_MOVIE_ID));
                    Navigation.findNavController(bottomNavigationView).navigate(action);
                }
                break;
        }
        return false;
    }
}
