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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class YouTubeBodyConverter implements Converter<ResponseBody, YouTubeExtractionResult> {

	private HttpUrl mBaseUrl;

	YouTubeBodyConverter(HttpUrl baseUrl) {
		mBaseUrl = baseUrl;
	}

	@Override
	public YouTubeExtractionResult convert(ResponseBody value) throws IOException {
		String html = value.string();

		HashMap<String, String> video = getQueryMap(html, "UTF-8");

		if (video.containsKey("url_encoded_fmt_stream_map")) {
			List<String> streamQueries = new ArrayList<>(Arrays.asList(video.get("url_encoded_fmt_stream_map").split(",")));

			String adaptiveFmts = video.get("adaptive_fmts");
			String[] split = adaptiveFmts.split(",");

			streamQueries.addAll(Arrays.asList(split));

			Map<Integer,String> streamLinks = new HashMap<>();
			for (String streamQuery : streamQueries) {
				HashMap<String, String> stream = getQueryMap(streamQuery, "UTF-8");
				String type = stream.get("type").split(";")[0];
				String urlString = stream.get("url");

				if (urlString != null /*&& MimeTypeMap.getSingleton().hasMimeType(type)*/) {
					String signature = stream.get("sig");

					if (signature != null) {
						urlString = urlString + "&signature=" + signature;
					}

					if (getQueryMap(urlString, "UTF-8").containsKey("signature")) {
						streamLinks.put(Integer.parseInt(stream.get("itag")), urlString);
					}
				}
			}

			final URI sd240VideoUri = extractVideoUri(YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_SMALL_240, streamLinks);
			final URI sd360VideoUri = extractVideoUri(YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_MEDIUM_360, streamLinks);
			final URI hd720VideoUri = extractVideoUri(YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_HD_720, streamLinks);
			final URI hd1080VideoUri = extractVideoUri(YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_HD_1080, streamLinks);

			final URI mediumThumbUri = video.containsKey("iurlmq") ? URI.create(video.get("iurlmq")) : null;
			final URI highThumbUri = video.containsKey("iurlhq") ? URI.create(video.get("iurlhq")) : null;
			final URI defaultThumbUri = video.containsKey("iurl") ? URI.create(video.get("iurl")) : null;
			final URI standardThumbUri = video.containsKey("iurlsd") ? URI.create(video.get("iurlsd")) : null;

			return new YouTubeExtractionResult()
					.setSd240VideoUri(sd240VideoUri)
					.setSd360VideoUri(sd360VideoUri)
					.setHd720VideoUri(hd720VideoUri)
					.setHd1080VideoUri(hd1080VideoUri)
					.setMediumThumbUri(mediumThumbUri)
					.setHighThumbUri(highThumbUri)
					.setDefaultThumbUri(defaultThumbUri)
					.setStandardThumbUri(standardThumbUri);
		} else {
			throw new YouTubeExtractionException("Status: " + video.get("status") + "\nReason: " + video.get("reason") + "\nError code: " + video.get("errorcode"));
		}

	}

	private static HashMap<String, String> getQueryMap(String queryString, String charsetName) throws UnsupportedEncodingException {
		HashMap<String, String> map = new HashMap<>();

		String[] fields = queryString.split("&");

		for (String field : fields) {
			String[] pair = field.split("=");
			if (pair.length == 2) {
				String key = pair[0];
				String value = URLDecoder.decode(pair[1], charsetName).replace('+', ' ');
				map.put(key, value);
			}
		}

		return map;
	}

	private URI extractVideoUri(int quality, Map<Integer,String> streamLinks) {
		URI videoUri = null;
		if (streamLinks.get(quality) != null) {
			String streamLink = streamLinks.get(quality);
			videoUri = URI.create(streamLink);
		}
		return videoUri;
	}
}
