package com.dhsp.luvu.service.impl;

import com.dhsp.luvu.dto.request.BannerRequest;
import com.dhsp.luvu.entity.Banner;
import com.dhsp.luvu.repository.BannerRepository;
import com.dhsp.luvu.service.BannerService;
import com.dhsp.luvu.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    BannerRepository bannerRepo;

    @Override
    public Banner save(BannerRequest request) {
        if(bannerRepo.count() > 4)
            throw new RuntimeException("Xoá bớt đi rồi thêm");
        try {
            String image = UploadUtils.save(request.getImage());
            Banner banner = new Banner(image);
            return bannerRepo.save(banner);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Banner update(BannerRequest request, Long id) {
        try {
            Banner banner = bannerRepo.findById(id).orElseThrow(() -> new RuntimeException("Chưa có banner này trong hệ thống"));

            if (request.getImage() != null) {
                String image = UploadUtils.save(request.getImage());
                banner.setImage(image);
            }

            banner = bannerRepo.save(banner);
            return banner;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Boolean delete(Long id) {
        try {
            bannerRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Banner> findAll() {
        return bannerRepo.findAll();
    }

    @Override
    public Banner findById(Long id) {
        return bannerRepo.findById(id).orElseThrow(() -> new RuntimeException("Chưa có banner này trong hệ thống"));
    }
}
