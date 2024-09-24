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

CREATE TABLE `t_all_types` (
                               `id` int(11) NOT NULL,
                               `c_tinyint_signed` tinyint(4) DEFAULT NULL,
                               `c_tinyint_unsigned` tinyint(3) unsigned DEFAULT NULL,
                               `c_tinyint_zerofill` tinyint(3) unsigned zerofill DEFAULT NULL,
                               `c_smallint_signed` smallint(6) DEFAULT NULL,
                               `c_smallint_unsigned` smallint(5) unsigned DEFAULT NULL,
                               `c_smallint_zerofill` smallint(5) unsigned zerofill DEFAULT NULL,
                               `c_mediumint_signed` mediumint(9) DEFAULT NULL,
                               `c_mediumint_unsigned` mediumint(8) unsigned DEFAULT NULL,
                               `c_mediumint_zerofill` mediumint(8) unsigned zerofill DEFAULT NULL,
                               `c_int_signed` int(11) DEFAULT NULL,
                               `c_int_unsigned` int(10) unsigned DEFAULT NULL,
                               `c_int_zerofill` int(10) unsigned zerofill DEFAULT NULL,
                               `c_bigint_signed` bigint(20) DEFAULT NULL,
                               `c_bigint_unsigned` bigint(20) unsigned DEFAULT NULL,
                               `c_bigint_zerofill` bigint(20) unsigned zerofill DEFAULT NULL,
                               `c_decimal_signed` decimal(10,0) DEFAULT NULL,
                               `c_decimal_unsigned` decimal(10,0) unsigned DEFAULT NULL,
                               `c_decimal_zerofill` decimal(10,0) unsigned zerofill DEFAULT NULL,
                               `c_float_signed` float DEFAULT NULL,
                               `c_float_unsigned` float unsigned DEFAULT NULL,
                               `c_float_zerofill` float unsigned zerofill DEFAULT NULL,
                               `c_double_signed` double DEFAULT NULL,
                               `c_double_unsigned` double unsigned DEFAULT NULL,
                               `c_double_zerofill` double unsigned zerofill DEFAULT NULL,
                               `c_bit` bit(1) DEFAULT NULL,
                               `c_char` char(1) DEFAULT NULL,
                               `c_varchar` varchar(16) DEFAULT NULL,
                               `c_tinytext` tinytext DEFAULT NULL,
                               `c_text` text DEFAULT NULL,
                               `c_mediumtext` mediumtext DEFAULT NULL,
                               `c_longtext` longtext DEFAULT NULL,
                               `c_enum` enum('a','b','c') DEFAULT NULL,
                               `c_set` set('A','B','C') DEFAULT NULL,
                               `c_json` json,
                               `c_date` date DEFAULT NULL,
                               `c_time` time DEFAULT NULL,
                               `c_datetime` datetime DEFAULT NULL,
                               `c_timestamp` timestamp NULL DEFAULT NULL,
                               `c_year` year(4) DEFAULT NULL,
                               `c_binary` binary(1) DEFAULT NULL,
                               `c_varbinary` varbinary(16) DEFAULT NULL,
                               `c_tinyblob` tinyblob DEFAULT NULL,
                               `c_blob` blob DEFAULT NULL,
                               `c_mediumblob` mediumblob DEFAULT NULL,
                               `c_longblob` longblob DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `t_all_types` VALUES
    (1,127,255,010,32767,65535,00100,8388607,16777215,00001000,2147483647,4294967295,0000010000,9223372036854775807,18446744073709551615,00000000000000100000,1,2,0000000000,2.1,2.2,0000000000.2,3.1,3.2,00000000000000000000.3,'','c','varchar','tinytext','text','mediumtext','longtext','a','A','{\"json\":1}','2020-01-01','12:12:12','2020-01-01 12:12:12','2020-01-01 01:01:01',2020,'b','varbinary','tinyblob','blob','mediumblob','longblob');

CREATE TABLE `t_blob_primary` (
                                  `id` tinyblob NOT NULL,
                                  `name` varchar(16) DEFAULT NULL,
                                  PRIMARY KEY (`id`(16))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `t_blob_primary` VALUES
    ('man a','MAN a');

CREATE TABLE `t_data_lower_upper` (
                                      `id` int(11) NOT NULL,
                                      `name` varchar(16) DEFAULT NULL,
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `t_data_lower_upper` VALUES
    (1,'MaN b');

CREATE TABLE `t_diff` (
                          `id` int(11) NOT NULL,
                          `name` varchar(16) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `t_diff` VALUES
                         (1,'a'),
                         (2,'b');