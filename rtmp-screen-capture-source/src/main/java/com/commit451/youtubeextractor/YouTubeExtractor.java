/*
 *  Copyright 2017 original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.commit451.youtubeextractor;


import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class YouTubeExtractor {
	private static final String BASE_URL = "https://www.youtube.com/";

	static final int YOUTUBE_VIDEO_QUALITY_SMALL_240 = 36;
	static final int YOUTUBE_VIDEO_QUALITY_MEDIUM_360 = 18;
	static final int YOUTUBE_VIDEO_QUALITY_HD_720 = 22;
	static final int YOUTUBE_VIDEO_QUALITY_HD_1080 = 37;

	/**
	 * Create a YouTubeExtractor
	 * @return a new {@link YouTubeExtractor}
	 */
	public static YouTubeExtractor create() {
		return create(null);
	}

	/**
	 * Create a new YouTubeExtractor with a custom OkHttp client builder
	 * @return a new {@link YouTubeExtractor}
	 */
	public static YouTubeExtractor create(OkHttpClient.Builder okHttpBuilder) {
		return new YouTubeExtractor(okHttpBuilder);
	}

	private final YouTube youTube;
	private final LanguageInterceptor interceptor;

	private YouTubeExtractor(OkHttpClient.Builder okBuilder) {

		if (okBuilder == null) {
			okBuilder = new OkHttpClient.Builder();
		}

		interceptor = new LanguageInterceptor();
		okBuilder.addInterceptor(interceptor);

		Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

		retrofitBuilder
				.baseUrl(BASE_URL)
				.client(okBuilder.build())
				.addConverterFactory(YouTubeExtractionConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

		youTube = retrofitBuilder.build().create(YouTube.class);
	}
	/**
	 * Extract the video information
	 * @param videoId the video ID
	 * @return the extracted result
	 */
	public Single<YouTubeExtractionResult> extract( String videoId) {
		return youTube.extract(videoId);
	}

	/**
	 * Set the language. Defaults to {@link java.util.Locale#getDefault()}
	 * @param language the language
	 */
	public void setLanguage( String language) {
		interceptor.setLanguage(language);
	}
}
