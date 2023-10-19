package ru.fisenko.shareAll.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisenko.shareAll.models.Url;
import ru.fisenko.shareAll.repositories.PostsRepository;
import ru.fisenko.shareAll.repositories.UrlRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UrlService {
    @Value("${url.generator.count}")
    private int urlCount;
    private long currentUrls;

    private final UrlRepository urlRepository;
    private final PostsRepository postsRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository, PostsRepository postsRepository) {
        this.urlRepository = urlRepository;
        this.postsRepository = postsRepository;
    }

    @Transactional
    public String getUrl() {
        checkNext();
        String url = urlRepository.findFirstByUrlIsNotNull().getUrl();
        urlRepository.deleteById(url);
        currentUrls--;
        return url;
    }

    private void checkNext() {
        if(currentUrls>0)return ;
        else if (urlRepository.count()>0)
            currentUrls=urlRepository.count();
        else {
            generateUrls(urlCount);
            currentUrls=urlRepository.count();
        }
    }

    @Transactional
    public void generateUrls(int urlCount) {
        String salt = "dasdgksda2345efg32";
        long time = Calendar.getInstance().getTimeInMillis();
        List<String> urls = new ArrayList<>();
        for(int i = 0; i < urlCount; i++)
            urls.add(DigestUtils.md5Hex(salt + i + time));
        urls.removeAll(postsRepository.findByInventoryIds(urls));

        urlRepository.saveAll(urls.stream().map(url -> new Url(url)).toList());
        currentUrls=urlRepository.count();
    }




}
