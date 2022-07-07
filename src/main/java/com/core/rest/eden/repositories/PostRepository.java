package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQuery;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    /*@Override
    @Query(value = "select p from Post p join fetch p.topics left join p.comments where p.id = :id")
    Optional<Post> findById(Long id);*/

    @Query(value = "select distinct p from Post p join fetch p.topics where p.clusteredTopic= :clusteredTopic")
    List<Post> findPostsByClusteredTopic(Integer clusteredTopic, Pageable pageable);

    Post findByIdCustom(Long id);

    @Query(value = "select distinct p from Post p join fetch p.topics where p.user = :user order by p.dateCreated desc")
    List<Post> findAllByUser(User user, Pageable pageable);

    @Query(value = "select distinct p from Post p left outer join Friendship f on p.user = f.addressee where f.requester = :user")
    List<Post> findFriendsPosts(User user, Pageable pageable);

    Post findByUser(User user);

    //@Query(value = "select distinct p from Post p left join fetch p.topics where p.topics IN :topics")
    List<Post> findDistinctAllByTopicsIn(Set<Topic> topics, Pageable pageable);
}
