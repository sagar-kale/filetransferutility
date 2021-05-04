package com.sgr.config;

import com.sgr.models.BrowserDetails;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

public class UserDetails {
    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static BrowserDetails getUserAgent(HttpServletRequest request) {
        BrowserDetails details = new BrowserDetails();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        details.setId(String.valueOf(userAgent.getId()));
        details.setName(userAgent.getBrowser().getName());
        details.setOs(userAgent.getOperatingSystem().getName());
        details.setDeviceType(userAgent.getOperatingSystem().getDeviceType().toString());
        details.setVersion(userAgent.getBrowserVersion().getVersion());
        return details;
    }
}
