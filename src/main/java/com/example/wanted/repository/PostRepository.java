package com.example.wanted.repository;

import com.example.wanted.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Query(value = "select p from Post p join fetch p.user u",
            countQuery = "select count(p) from Post p")
    Page<Post> findPosts(Pageable pageable);

}
