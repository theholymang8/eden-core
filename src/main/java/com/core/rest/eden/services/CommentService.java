package com.core.rest.eden.services;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.transfer.DTO.CommentView;

import java.util.List;

public interface CommentService extends BaseService<Comment, Long>{

    List<CommentView> postComments(Post post);

}
