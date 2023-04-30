package com.dhsp.luvu.service;

import com.dhsp.luvu.dto.request.BannerRequest;
import com.dhsp.luvu.entity.Banner;

import java.util.List;

public interface BannerService {
    Banner save(BannerRequest request);

    Banner update(BannerRequest request, Long id);

    Boolean delete(Long id);

    List<Banner> findAll();

    Banner findById(Long id);
}
