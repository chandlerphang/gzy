package com.cactus.guozy.common.cms;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;

public class AssetView implements View {

	private static final Logger LOG = LoggerFactory.getLogger(AssetView.class);

	protected boolean browserAssetCachingEnabled = true;
	protected long cacheSeconds = 60 * 60 * 24;

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String cacheFilePath = (String) model.get("cacheFilePath");
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(cacheFilePath));
		try {
			String mimeType = (String) model.get("mimeType");
			response.setContentType(mimeType);
			if (!browserAssetCachingEnabled) {
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
			} else {
				response.setHeader("Cache-Control", "public");
				response.setHeader("Pragma", "cache");
				if (!StringUtils.isEmpty(request.getHeader("If-Modified-Since"))) {
					long lastModified = request.getDateHeader("If-Modified-Since");
					Calendar last = Calendar.getInstance();
					last.setTime(new Date(lastModified));
					Calendar check = Calendar.getInstance();
					check.add(Calendar.SECOND, -2 * new Long(cacheSeconds).intValue());
					if (check.compareTo(last) < 0) {
						response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
						return;
					}
				} else {
					Calendar check = Calendar.getInstance();
					check.add(Calendar.SECOND, -1 * new Long(cacheSeconds).intValue());
					response.setDateHeader("Last-Modified", check.getTimeInMillis());
				}
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, new Long(cacheSeconds).intValue());
				response.setDateHeader("Expires", cal.getTimeInMillis());
			}
			OutputStream os = response.getOutputStream();
			boolean eof = false;
			while (!eof) {
				int temp = bis.read();
				if (temp < 0) {
					eof = true;
				} else {
					os.write(temp);
				}
			}
			os.flush();
		} catch (Exception e) {
			if (e.getCause() instanceof SocketException) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Unable to stream asset", e);
				}
			} else {
				LOG.error("Unable to stream asset", e);
				throw e;
			}
		} finally {
			try {
				bis.close();
			} catch (Throwable e) {
				// do nothing
			}
		}
	}

	public boolean isBrowserAssetCachingEnabled() {
		return browserAssetCachingEnabled;
	}

	public void setBrowserAssetCachingEnabled(boolean browserAssetCachingEnabled) {
		this.browserAssetCachingEnabled = browserAssetCachingEnabled;
	}

	public long getCacheSeconds() {
		return cacheSeconds;
	}

	public void setCacheSeconds(long cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}

}
