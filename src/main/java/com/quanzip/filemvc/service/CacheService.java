package com.quanzip.filemvc.service;

import java.util.List;

public interface CacheService {
    void deleteByCacheName(String cacheName);
    void deleteCachebyElementId(String cacheName, Long Id);
    Object getDataByCacheName(String cacheName);
    List<String> getCacheNames();
    void printCacheInformation();
}
