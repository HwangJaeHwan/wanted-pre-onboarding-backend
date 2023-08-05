package com.example.wanted.service;


import com.example.wanted.domain.Post;
import com.example.wanted.domain.User;
import com.example.wanted.repository.PostRepository;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.PageInfo;
import com.example.wanted.request.PostModify;
import com.example.wanted.request.PostWrite;
import com.example.wanted.response.PostList;
import com.example.wanted.response.PostListInfo;
import com.example.wanted.response.PostRead;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public void write(Long userId, PostWrite postWrite) {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);//유저 못찾음 에러

        postRepository.save(Post.builder()
                .title(postWrite.getTitle())
                .content(postWrite.getContent())
                .user(user)
                .build());

    }

    public PostRead read(Long postId) {

        return new PostRead(postRepository.findById(postId).orElseThrow(RuntimeException::new));//게시물 미발견 에러

    }

    public void modify(Long userId, Long postId,PostModify postModify) {

        Post modifyPost = writerCheck(userId, postId);

        modifyPost.modify(postModify);


    }

    public void delete(Long userId, Long postId) {

        Post deletePost = writerCheck(userId, postId);

        postRepository.delete(deletePost);

    }

    public PostList posts(PageInfo page) {

        Page<Post> posts = postRepository.findAll(PageRequest.of(page.getPage()-1, 10));

        return PostList.builder()
                .totalPage(posts.getTotalPages())
                .infos(posts.map(PostListInfo::new).getContent())
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .build();

    }

    private Post writerCheck(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);

        Post post = postRepository.findById(postId).orElseThrow(RuntimeException::new);

        if (post.getUser() != user) {
            throw new RuntimeException();
        }

        return post;
    }


}