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
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Vinicius Carvalho
 */
public class LanguageInterceptor implements Interceptor {

	static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
	static final String LANGUAGE_QUERY_PARAM = "language";

	String language;

	LanguageInterceptor() {
		this.language = Locale.getDefault().getLanguage();
	}

	@Override
	public Response intercept(Chain chain) throws IOException {

		Request request = chain.request();

		HttpUrl url = request.url()
				.newBuilder()
				.addQueryParameter(LANGUAGE_QUERY_PARAM, language)
				.build();

		Request requestWithHeaders = request.newBuilder()
				.addHeader(ACCEPT_LANGUAGE_HEADER, language)
				.url(url)
				.build();
		return chain.proceed(requestWithHeaders);
	}

	void setLanguage(String language) {
		this.language = language;
	}
}

