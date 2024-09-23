CREATE DATABASE /*!32312 IF NOT EXISTS*/ `compare` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `compare`;

CREATE TABLE `t_only_compare` (
                                  `id` int(11) NOT NULL,
                                  `name` varchar(16) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_foreign_had` (
                                 `id` int(11) NOT NULL,
                                 `name` varchar(16) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_foreign_has` (
                                 `id` int(11) NOT NULL,
                                 `name` varchar(16) DEFAULT NULL,
                                 `r_id` int(11) NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `r_id` (`r_id`),
                                 CONSTRAINT `t_foreign_has_ibfk_1` FOREIGN KEY (`r_id`) REFERENCES `t_foreign_had` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_myisam` (
                            `id` int(11) NOT NULL,
                            `name` varchar(16) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE VIEW `t_view_ok` AS SELECT
                               1 AS `id`,
                               1 AS `name`;

CREATE TABLE `t_has_trigger_mismatch` (
                                          `id` int(11) NOT NULL,
                                          `name` varchar(16) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_no_primary` (
                                `id` int(11) NOT NULL,
                                `name` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_invalid_charset` (
                                     `id` int(11) NOT NULL,
                                     `name` varchar(16) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `t_wrong_auto_increment` (
                                          `id` int(11) NOT NULL AUTO_INCREMENT,
                                          `name` varchar(16) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_partition_mismatch` (
                                        `id` int(11) NOT NULL,
                                        `name` varchar(16) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_comment_mismatch` (
                                      `id` int(11) NOT NULL,
                                      `name` varchar(16) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
 PARTITION BY LIST  COLUMNS(`id`)
(PARTITION `p1` VALUES IN (1,2,3) ENGINE = InnoDB,
 PARTITION `p2` VALUES IN (4,5,6) ENGINE = InnoDB,
 PARTITION `p3` VALUES IN (7,8,9) ENGINE = InnoDB);