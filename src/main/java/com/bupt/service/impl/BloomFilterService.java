//import org.redisson.api.RBloomFilter;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//
//@Service
//public class BloomFilterService {
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private static final String BLOOM_FILTER_NAME = "userBloomFilter";
//    private static final long EXPECTED_INSERTIONS = 10000;
//    private static final double FALSE_PROBABILITY = 0.01;
//
//    @PostConstruct
//    public void init() {
//        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_NAME);
//        bloomFilter.tryInit(EXPECTED_INSERTIONS, FALSE_PROBABILITY);
//
//        List<Long> userIds = userRepository.findAll().stream().map(User::getId).toList();
//        for (Long userId : userIds) {
//            bloomFilter.add(userId.toString());
//        }
//    }
//
//    public boolean mightContain(String element) {
//        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_NAME);
//        return bloomFilter.contains(element);
//    }
//}