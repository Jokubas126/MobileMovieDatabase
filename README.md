# Mobile Movie Database (MMDb)
This application was made as a hobby project. It is used for searching movies by genre, release date, original language or a title keyword. Also, retrieving movies by 4 different categories: popularity, vote average, upcoming and now playing. The information is retrieved from [The Movie Database (TMDb)](https://www.themoviedb.org).

# Main methods and libraries used
### General
* MVVM Patern
* [Android Jetpack](https://developer.android.com/jetpack) for a single activity approach
* Parsing JSON from REST API using [Retrofit](https://square.github.io/retrofit/) with coroutines
* View and Data binding
* [Glide library](https://bumptech.github.io/glide/)
* [YoutubePlayer](https://developers.google.com/youtube/android/player)

### UI components
* RecyclerView with StaggeredGridLayout, horizontal and vertical LinearLayouts
* [ExpandableRecyclerView](https://github.com/thoughtbot/expandable-recycler-view)
* DrawerLayout with NavigationView
* BottomNavigationView
* [MultiSlider](https://github.com/apptik/MultiSlider/tree/v1.3)
* SearchView
