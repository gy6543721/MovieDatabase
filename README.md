# MovieDB Compose
<img src="https://github.com/gy6543721/MovieDatabase/blob/main/app/src/main/res/mipmap-xxxhdpi/ic_moviedatabase_launcher.png" height="200"/>

TMDB Android App written in Kotlin and Compose.  
Now support English, Japanese, Traditional Chinese, Simplified Chinese and Cantonese, will support more languages in the future.  

[Google Play Store](https://play.google.com/store/apps/details?id=levilin.moviedatabase)  
[Demo Video](https://youtu.be/fixcJBaKpcE)  
<table>
	<tr>
		<td>
			<img src="pictures/001.png"  height=250>
		</td>
		<td>
			<img src="pictures/004.png"  height=250>
		</td>
	</tr>
	<tr>
		<td>
			<img src="pictures/002.png"  height=250>
		</td>
		<td>
			<img src="pictures/005.png"  height=250>
		</td>
	</tr>
	<tr>
		<td>
			<img src="pictures/003.png"  height=250>
		</td>
		<td>
			<img src="pictures/006.png"  height=250>
		</td>
	</tr>
</table>

## API
- API Documentation: https://developers.themoviedb.org/3/getting-started/introduction  
- Film search API endpoint: **GET** https://api.themoviedb.org/3/search/movie?api_key={apikey}&query={search_query}  
- Film details API endpoint: **GET** https://api.themoviedb.org/3/movie/{movie_id}?api_key={apikey}  

# Main Libraries Used
* Compose
* Coroutines
* Navigation
* Room
* Dagger Hilt
* Retrofit
* OkHttp3
* Gson
* Coil
* Junit

