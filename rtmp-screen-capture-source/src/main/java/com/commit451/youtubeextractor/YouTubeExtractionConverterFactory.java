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


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class YouTubeExtractionConverterFactory extends Converter.Factory{
	static YouTubeExtractionConverterFactory create() {
		return new YouTubeExtractionConverterFactory();
	}
	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
		// This is good, we only register if the call includes this type, so that we could potentially
		// still be okay with having additional converter factories if we needed to
		if (type == YouTubeExtractionResult.class) {
			return new YouTubeBodyConverter(retrofit.baseUrl());
		}
		// Allow others to give it a go
		return null;
	}

	@Override
	public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
		throw new IllegalStateException("Not supported");
	}
}
