CREATE TABLE `news`
(
    `id`        bigint                                  NOT NULL AUTO_INCREMENT,
    `title`     varchar(100) COLLATE utf8mb3_unicode_ci NOT NULL,
    `author`    varchar(100) COLLATE utf8mb3_unicode_ci NOT NULL,
    `date_time` datetime                                NOT NULL,
    `text`      varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_unicode_ci;

INSERT INTO `news` (`id`, `title`, `author`, `date_time`, `text`)
VALUES (1, 'Заголовок 100', 'Автор 100', '2024-03-26 19:50:20', 'Текст 100'),
       (2, 'Заголовок 200', 'Автор 200', '2024-03-26 22:55:42', 'Текст 200'),
       (3, 'Заголовок 300', 'Автор 300', '2024-03-26 22:55:42', 'Текст 300'),
       (4, 'Заголовок 400', 'Автор 400', '2024-03-26 22:55:42', 'Текст 400');

CREATE TABLE `comment`
(
    `id`        bigint                                  NOT NULL AUTO_INCREMENT,
    `news_id`   bigint                                  NOT NULL,
    `author`    varchar(100) COLLATE utf8mb3_unicode_ci NOT NULL,
    `date_time` datetime                                NOT NULL,
    `text`      varchar(255) COLLATE utf8mb3_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `news_id` (`news_id`),
    CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_unicode_ci;

INSERT INTO `comment` (`id`, `news_id`, `author`, `date_time`, `text`)
VALUES (1, 1, 'автор 100', '2024-03-26 23:05:25', 'текст 100'),
       (2, 1, 'автор 200', '2024-03-26 23:05:53', 'текст 200'),
       (3, 2, 'автор 300', '2024-03-26 23:05:57', 'текст 300'),
       (4, 2, 'автор 400', '2024-03-26 23:54:31', 'текст 400');

CREATE TABLE `tag`
(
    `id`   bigint                                  NOT NULL AUTO_INCREMENT,
    `name` varchar(100) COLLATE utf8mb3_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_unicode_ci;

INSERT INTO `tag` (`id`, `name`)
VALUES (1, 'Наука'),
       (2, 'Техника'),
       (3, 'Технологии'),
       (4, 'Политика');

CREATE TABLE `news_tag`
(
    `news_id` bigint NOT NULL,
    `tag_id`  bigint NOT NULL,
    KEY `news_id` (`news_id`),
    KEY `tag_id` (`tag_id`),
    CONSTRAINT `news_tag_ibfk_1` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE,
    CONSTRAINT `news_tag_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE,
    UNIQUE (news_id, tag_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_unicode_ci;

INSERT INTO `news_tag` (`news_id`, `tag_id`)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 1),
       (4, 4);

