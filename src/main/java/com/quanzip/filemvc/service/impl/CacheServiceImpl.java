package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CacheServiceImpl implements CacheService {
    private final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void deleteByCacheName(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if(!Objects.isNull(cache)){
            // clear all data of cache name, not clear cache name in cahe memory
            cache.clear();
        }
    }

    @Override
    public void deleteCachebyElementId(String cacheName, Long id) {
        Cache cache = cacheManager.getCache(cacheName);
        if(!Objects.isNull(cache)) cache.evict(id);
    }

    @Override
    public Object getDataByCacheName(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        return cache != null ? cache.getNativeCache(): null;
    }

    @Override
    public List<String> getCacheNames() {
        return new ArrayList<>(cacheManager.getCacheNames());
    }

    @Override
    public void printCacheInformation() {
        List<String> cacheNames = getCacheNames();
        if(cacheNames.size() == 0) return;
        for(String cacheName: cacheNames){
            Cache cache = cacheManager.getCache(cacheName);
            if(!Objects.isNull(cache)) {
                Object data = cache.getNativeCache();
                logger.info(data.toString());
            }
        }
    }
}
