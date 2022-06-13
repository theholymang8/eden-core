package com.core.rest.eden.base;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.CommentService;
import com.core.rest.eden.services.PostService;
import com.core.rest.eden.services.TopicService;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("generate-posts")
public class PostFactory extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;

    private final PostService postService;

    private final CommentService commentService;

    private final TopicService topicService;

    @Override
    public void run(String... args) {
        List<Post> posts = List.of(
                Post.builder().body("One of the biggest mysteries of the Great Barrier Reef are blue holes that can give researchers a rare look at ocean life and how we can protect it. Dive in to see how researchers are using @GoogleMaps to locate and learn from these sinkholes → http://goo.gle/2TGnxvl")
                        .dateCreated(LocalDateTime.of(2022, 3, 16, 17, 48))
                        .dateUpdated(null)
                        .build(),
                Post.builder().body("We’ve got very exciting news.. Tony’s Chocolonely Chocolate Bar is open! This seriously crazy Chocolate Bar is a dream comin' true for every choco fan. In the Bar you'll be enjoying the most crazy delicious choco-creations, all made from Tony's chocolate (made with fully traceable cocoa beans from West Africa), whilst at the same time learning more about Tony's serious mission: to make 100% slave free the norm in chocolate. See you in the Bar?")
                        .dateCreated(LocalDateTime.of(2022, 3, 16, 17, 20))
                        .dateUpdated(null)
                        .build(),
                Post.builder().body("We've found the most private way for job advertisements. Everyone who opens the browser console will see it!  Curious? Get in touch and fight for privacy with us")
                        .dateCreated(LocalDateTime.of(2022, 2, 27, 13, 43))
                        .dateUpdated(null)
                        .build(),
                Post.builder().body("New here? Remember that Twitter is optimized to make you into passive consumers, by en(r/g)aging you with content that you don't choose yourself.\n" +
                        "\n" +
                        "Here, that doesn't happen. But it also means you're responsible to find people to follow. \n" +
                        "\n" +
                        "It will take some work. \n" +
                        "\n" +
                        "If you're willing to put that in, you'll find a wide variety of friendly people that will be happy to have meaningful conversions and laugh and cry together with you.\n" +
                        "\n" +
                        "Start by searching and looking at who other people follow.")
                        .dateCreated(LocalDateTime.of(2022, 1, 13, 9, 34))
                        .dateUpdated(null)
                        .build(),
                Post.builder().body("Bitcoin (BTC) Signals for 48% Upside With Target at $61,000 \n" +
                        " \n" +
                        " https://beincrypto.com/bitcoin-btc-signals-for-48-upside-with-target-at-61000/ ")
                        .dateCreated(LocalDateTime.of(2022, 1, 13, 16, 37))
                        .dateUpdated(null)
                        .build(),
                Post.builder().body("I have to hand it to Twitter's management. Their decision to paywall anyone that isn't logged in is THE ABSOLUTE BEST MARKETING EVER for Nitter.net")
                        .dateCreated(LocalDateTime.of(2022, 2, 11, 12, 43))
                        .dateUpdated(null)
                        .build()
        );

        List<User> users = userService.findAll();

        /* Associate users with posts */
        if (!users.isEmpty()) {
            posts.get(0).setUser(users.get(0));
            posts.get(0).setLikes(10);

            posts.get(1).setUser(users.get(0));
            posts.get(1).setLikes(8);

            posts.get(2).setUser(users.get(1));
            posts.get(2).setLikes(6);

            posts.get(3).setUser(users.get(1));
            posts.get(3).setLikes(4);

            posts.get(4).setUser(users.get(2));
            posts.get(4).setLikes(27);

            posts.get(5).setUser(users.get(2));
            posts.get(5).setLikes(32);
        }

        List<Comment> comments = List.of(
                Comment.builder()
                        .body("This is definitely a blast!")
                        .dateCreated(LocalDateTime.of(2022, 1, 13, 16, 37))
                        .dateUpdated(null)
                        .post(posts.get(3))
                        .user(users.get(0))
                        .build(),
                Comment.builder()
                        .body("Not news to my ears to be honest!")
                        .dateCreated(LocalDateTime.of(2022, 1, 13, 16, 37))
                        .dateUpdated(null)
                        .post(posts.get(3))
                        .user(users.get(1))
                        .build(),
                Comment.builder()
                        .body("It wasn't like this for me a month ago")
                        .dateCreated(LocalDateTime.of(2022, 1, 13, 16, 37))
                        .dateUpdated(null)
                        .post(posts.get(3))
                        .user(users.get(2))
                        .build(),
                Comment.builder()
                        .body("Every man has a plan that will not work – My dad after cutting a length of kitchen counter top 1/4 inch too short.")
                        .dateCreated(LocalDateTime.of(2022, 1, 12, 14, 17))
                        .dateUpdated(null)
                        .post(posts.get(3))
                        .user(users.get(3))
                        .build(),
                Comment.builder()
                        .body("New user of Nitter. I rather like it!" +
                                "I'm just concerned that Nitter's use of the unofficial Twitter API, will be disallowed at some point in future.\n" +
                                "Even if that happens, it may be a blessing in disguise.")
                        .dateCreated(LocalDateTime.of(2022, 1, 13, 15, 27))
                        .dateUpdated(null)
                        .post(posts.get(3))
                        .user(users.get(4))
                        .build(),
                Comment.builder()
                        .body("Was always excited about the ocean life!")
                        .dateCreated(LocalDateTime.of(2022, 3, 16, 20, 23))
                        .dateUpdated(null)
                        .post(posts.get(0))
                        .user(users.get(5))
                        .build(),
                Comment.builder()
                        .body("Overlooking how beautiful this actually is")
                        .dateCreated(LocalDateTime.of(2022, 3, 17, 16, 37))
                        .dateUpdated(null)
                        .post(posts.get(0))
                        .user(users.get(4))
                        .build()
        );

        logger.info("Created {} posts", posts.size());
        postService.createAll(posts);

        logger.info("Created {} comments", comments.size());
        commentService.createAll(comments);

    }
}
