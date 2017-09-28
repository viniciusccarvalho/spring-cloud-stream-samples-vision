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


import java.net.URI;

public class YouTubeExtractionResult {

	private URI sd240VideoUri;
	private URI sd360VideoUri;
	private URI hd720VideoUri;
	private URI hd1080VideoUri;
	private URI mediumThumbUri;
	private URI highThumbUri;
	private URI defaultThumbUri;
	private URI standardThumbUri;

	public URI getSd240VideoUri() {
		return sd240VideoUri;
	}

	public YouTubeExtractionResult setSd240VideoUri(URI sd240VideoUri) {
		this.sd240VideoUri = sd240VideoUri;
		return this;
	}

	public URI getSd360VideoUri() {
		return sd360VideoUri;
	}

	public YouTubeExtractionResult setSd360VideoUri(URI sd360VideoUri) {
		this.sd360VideoUri = sd360VideoUri;
		return this;
	}

	public URI getHd720VideoUri() {
		return hd720VideoUri;
	}

	public YouTubeExtractionResult setHd720VideoUri(URI hd720VideoUri) {
		this.hd720VideoUri = hd720VideoUri;
		return this;
	}

	public URI getHd1080VideoUri() {
		return hd1080VideoUri;
	}

	public YouTubeExtractionResult setHd1080VideoUri(URI hd1080VideoUri) {
		this.hd1080VideoUri = hd1080VideoUri;
		return this;
	}

	public URI getMediumThumbUri() {
		return mediumThumbUri;
	}

	public YouTubeExtractionResult setMediumThumbUri(URI mediumThumbUri) {
		this.mediumThumbUri = mediumThumbUri;
		return this;
	}

	public URI getHighThumbUri() {
		return highThumbUri;
	}

	public YouTubeExtractionResult setHighThumbUri(URI highThumbUri) {
		this.highThumbUri = highThumbUri;
		return this;
	}

	public URI getDefaultThumbUri() {
		return defaultThumbUri;
	}

	public YouTubeExtractionResult setDefaultThumbUri(URI defaultThumbUri) {
		this.defaultThumbUri = defaultThumbUri;
		return this;
	}

	public URI getStandardThumbUri() {
		return standardThumbUri;
	}

	public YouTubeExtractionResult setStandardThumbUri(URI standardThumbUri) {
		this.standardThumbUri = standardThumbUri;
		return this;
	}

	public URI getBestAvailableQualityVideoUri() {
		URI uri = getHd1080VideoUri();
		if (uri != null) {
			return uri;
		}
		uri = getHd720VideoUri();
		if (uri != null) {
			return uri;
		}
		uri = getSd360VideoUri();
		if (uri != null) {
			return uri;
		}
		uri = getSd240VideoUri();
		if (uri != null) {
			return uri;
		}
		return null;
	}

	public URI getBestAvailableQualityThumbUri() {
		URI uri = getHighThumbUri();
		if (uri != null) {
			return uri;
		}
		uri = getMediumThumbUri();
		if (uri != null) {
			return uri;
		}
		uri = getDefaultThumbUri();
		if (uri != null) {
			return uri;
		}
		uri = getStandardThumbUri();
		if (uri != null) {
			return uri;
		}
		return null;
	}
}
