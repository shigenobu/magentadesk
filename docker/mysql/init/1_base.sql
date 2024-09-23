CREATE DATABASE /*!32312 IF NOT EXISTS*/ `base` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `base`;

CREATE TABLE `t_only_base` (
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

CREATE trigger t_has_trigger_mismatch_insert_trigger after insert on t_has_trigger_mismatch for each row insert into t_trigger_result values(now(), 'insert');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `t_partition_mismatch` (
                                        `id` int(11) NOT NULL,
                                        `name` varchar(16) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
 PARTITION BY LIST  COLUMNS(`id`)
(PARTITION `p1` VALUES IN (1,2,3) ENGINE = InnoDB,
 PARTITION `p2` VALUES IN (4,5,6) ENGINE = InnoDB,
 PARTITION `p` VALUES IN (7,8,9) ENGINE = InnoDB);

CREATE TABLE `t_comment_mismatch` (
                                      `id` int(11) NOT NULL COMMENT 'USER ID',
                                      `name` varchar(16) DEFAULT NULL COMMENT 'USER NAME',
                                      PRIMARY KEY (`id`) COMMENT 'PRIMARY IDX ID',
                                      KEY `name` (`name`) COMMENT 'SECONDARY IDX NAME'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='TABLE COMMENT'
 PARTITION BY LIST  COLUMNS(`id`)
(PARTITION `p1` VALUES IN (1,2,3) COMMENT = 'PARTITION P1' ENGINE = InnoDB,
 PARTITION `p2` VALUES IN (4,5,6) COMMENT = 'PARTITION P2' ENGINE = InnoDB,
 PARTITION `p3` VALUES IN (7,8,9) COMMENT = 'PARTITION P3' ENGINE = InnoDB);

CREATE TABLE `T_ALL_UPPER` (
                               `ID` int(11) NOT NULL,
                               `NAME` varchar(16) DEFAULT NULL,
                               PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `T_ALL_UPPER` VALUES
    (1,'a');

CREATE TABLE `t_lower_UPPER` (
                                 `Id` int(11) NOT NULL,
                                 `NaMe` varchar(16) DEFAULT NULL,
                                 PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `t_lower_UPPER` VALUES
    (1,'a');