//package com.bupt.pojo;
//import org.redisson.api.RBloomFilter;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class BloomFilterInitializer {
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    private static final String BLOOM_FILTER_NAME = "myBloomFilter";
//    private static final long EXPECTED_INSERTIONS = 10000;
//    private static final double FALSE_PROBABILITY = 0.01;
//
//    private boolean isInitialized = false;
//
//    @PostConstruct
//    public void initBloomFilter() {
//        if (!isInitialized) {
//            RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_NAME);
//            bloomFilter.tryInit(EXPECTED_INSERTIONS, FALSE_PROBABILITY);
//            isInitialized = true;
//        }
//    }
//}
