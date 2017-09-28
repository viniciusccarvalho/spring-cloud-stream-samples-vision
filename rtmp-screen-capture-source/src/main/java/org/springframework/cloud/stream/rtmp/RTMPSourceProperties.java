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

package org.springframework.cloud.stream.rtmp;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Vinicius Carvalho
 */
@ConfigurationProperties("rtmp")
public class RtmpSourceProperties {

	private Integer intervalBetweenFrames = 1;

	private String imageFormat = "png";

	private Integer poolSize = 2;

	private boolean saveSnapshots = false;

	private String snapshotFolder = System.getProperty("java.io.tmpdir");

	public boolean isSaveSnapshots() {
		return saveSnapshots;
	}

	public void setSaveSnapshots(boolean saveSnapshots) {
		this.saveSnapshots = saveSnapshots;
	}

	public String getSnapshotFolder() {
		return snapshotFolder;
	}

	public void setSnapshotFolder(String snapshotFolder) {
		this.snapshotFolder = snapshotFolder;
	}

	public Integer getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(Integer poolSize) {
		this.poolSize = poolSize;
	}


	public Integer getIntervalBetweenFrames() {
		return intervalBetweenFrames;
	}

	public void setIntervalBetweenFrames(Integer intervalBetweenFrames) {
		this.intervalBetweenFrames = intervalBetweenFrames;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
}
