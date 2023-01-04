package com.quanzip.filemvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// cache.getNativeCache() is a map, it can be cast to map<String, Object>
// This class is for testing cache and controller cache memory mannually

@RestController
@RequestMapping(value = "/cache")
public class CacheController {

    // this is use for read cache saved, can not use to write cache.
    @Autowired
    private CacheManager cacheManager;

    @GetMapping(value = "/list")
    public ResponseEntity<List<String>> getListDataCached(){
        List<String> result = new ArrayList<>(cacheManager.getCacheNames());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/key")
    public String getKeyofCachename(@RequestParam(value = "name") String cacheName){
        Cache cache = cacheManager.getCache(cacheName);
        if(!Objects.isNull(cache)){
            Object object = cache.getNativeCache();
            return object.toString();
        }
        return null;
    }

    @GetMapping(value = "/delete")
    public void deleteCacheByName(@RequestParam(value = "name") String cacheName){
        Cache cache = cacheManager.getCache(cacheName);
        if(!Objects.isNull(cache)){
            // when using cache.clear() cach like this, we only clear data of cache, but name still exist in cache memory
            cache.clear();
//            cache.evict();
        }
    }

}
