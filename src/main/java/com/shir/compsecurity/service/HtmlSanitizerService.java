package com.shir.compsecurity.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class HtmlSanitizerService {
    public String sanitize(String raw) {
        if (raw == null) return null;
        return Jsoup.clean(raw, Safelist.none());
    }
}
