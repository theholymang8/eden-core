package com.core.rest.eden.services;

import com.core.rest.eden.transfer.DTO.RecommendedFriendsDTO;

import java.util.List;

public interface CerebrumRestService {

    RecommendedFriendsDTO getRecommendedFriends(Long userId);

}
