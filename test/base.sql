-- MariaDB dump 10.19  Distrib 10.5.11-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: base
-- ------------------------------------------------------
-- Server version	10.5.11-MariaDB-1:10.5.11+maria~focal

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `base`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `base` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `base`;

--
-- Sequence structure for `s_seq`
--

DROP SEQUENCE IF EXISTS `s_seq`;
CREATE SEQUENCE `s_seq` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB;
SELECT SETVAL(`s_seq`, 1, 0);

--
-- Table structure for table `T_ALL_UPPER`
--

DROP TABLE IF EXISTS `T_ALL_UPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `T_ALL_UPPER` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `T_ALL_UPPER`
--

LOCK TABLES `T_ALL_UPPER` WRITE;
/*!40000 ALTER TABLE `T_ALL_UPPER` DISABLE KEYS */;
INSERT INTO `T_ALL_UPPER` VALUES (1,'a');
/*!40000 ALTER TABLE `T_ALL_UPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_all_types`
--

DROP TABLE IF EXISTS `t_all_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `c_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_all_types`
--

LOCK TABLES `t_all_types` WRITE;
/*!40000 ALTER TABLE `t_all_types` DISABLE KEYS */;
INSERT INTO `t_all_types` VALUES (1,127,255,010,32767,65535,00100,8388607,16777215,00001000,2147483647,4294967295,0000010000,9223372036854775807,18446744073709551615,00000000000000100000,2147483647,4294967295,4294967295,-3.40282e38,3.40282e38,003.40282e38,-1.7976931348623157e308,1.7976931348623157e308,1.7976931348623157e308,'','c','varchar','tinytext','text','mediumtext','longtext','a','A','{\"json\":1}','2020-01-01','12:12:12','2020-01-01 12:12:12','2020-01-01 01:01:01',2020,'b','varbinary','tinyblob','blob','mediumblob','longblob'),(2,127,255,010,32767,65535,00100,8388607,16777215,00001000,2147483647,4294967295,0000010000,9223372036854775807,18446744073709551615,00000000000000100000,1,2,0000000000,2.1,2.2,0000000000.2,3.1,3.2,00000000000000000000.3,'','c','varchar','tinytext','text','mediumtext','longtext','a','A','{\"json\":1}','2020-01-01','12:12:12','2020-01-01 12:12:12','2020-01-01 01:01:01',2020,'b','varbinary','tinyblob','blob','mediumblob','longblob');
/*!40000 ALTER TABLE `t_all_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_article`
--

DROP TABLE IF EXISTS `t_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_article` (
  `article_id` bigint(20) unsigned NOT NULL COMMENT '記事ID',
  `title` mediumtext NOT NULL COMMENT 'タイトル',
  `detail` longtext NOT NULL COMMENT '詳細',
  `in_date` datetime NOT NULL COMMENT '登録日時',
  `up_date` datetime NOT NULL COMMENT '更新日時',
  PRIMARY KEY (`article_id`),
  KEY `k1` (`up_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='記事';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_article`
--

LOCK TABLES `t_article` WRITE;
/*!40000 ALTER TABLE `t_article` DISABLE KEYS */;
INSERT INTO `t_article` VALUES (1,'記事1','詳細詳細詳細詳細詳細詳細詳細','2020-10-01 00:00:00','2020-10-10 00:00:00'),(2,'記事2','詳細詳細詳細詳細詳細詳細詳細','2020-10-11 00:00:00','2020-10-20 00:00:00'),(3,'記事3','詳細詳細詳細詳細詳細詳細詳細','2020-10-21 00:00:00','2020-10-30 00:00:00'),(4,'記事4','詳細詳細詳細詳細詳細詳細詳細','2020-11-01 00:00:00','2020-11-10 00:00:00'),(5,'記事5','詳細詳細詳細詳細詳細詳細詳細','2020-11-11 00:00:00','2020-11-20 00:00:00');
/*!40000 ALTER TABLE `t_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_blob_primary`
--

DROP TABLE IF EXISTS `t_blob_primary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_blob_primary` (
  `id` tinyblob NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`(16))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_blob_primary`
--

LOCK TABLES `t_blob_primary` WRITE;
/*!40000 ALTER TABLE `t_blob_primary` DISABLE KEYS */;
INSERT INTO `t_blob_primary` VALUES ('man a','MAN A');
/*!40000 ALTER TABLE `t_blob_primary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_comment`
--

DROP TABLE IF EXISTS `t_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_comment` (
  `id` int(11) NOT NULL COMMENT 'USER ID',
  `name` varchar(16) DEFAULT NULL COMMENT 'USER NAME',
  PRIMARY KEY (`id`) COMMENT 'PRIMARY IDX ID',
  KEY `name` (`name`) COMMENT 'SECONDARY IDX NAME'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='TABLE COMMENT'
 PARTITION BY LIST  COLUMNS(`id`)
(PARTITION `p1` VALUES IN (1,2,3) COMMENT = 'PARTITION P1' ENGINE = InnoDB,
 PARTITION `p2` VALUES IN (4,5,6) COMMENT = 'PARTITION P2' ENGINE = InnoDB,
 PARTITION `p3` VALUES IN (7,8,9) COMMENT = 'PARTITION P3' ENGINE = InnoDB);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_comment`
--

LOCK TABLES `t_comment` WRITE;
/*!40000 ALTER TABLE `t_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_comment_mismatch`
--

DROP TABLE IF EXISTS `t_comment_mismatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_comment_mismatch` (
  `id` int(11) NOT NULL COMMENT 'USER ID',
  `name` varchar(16) DEFAULT NULL COMMENT 'USER NAME',
  PRIMARY KEY (`id`) COMMENT 'PRIMARY IDX ID',
  KEY `name` (`name`) COMMENT 'SECONDARY IDX NAME'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='TABLE COMMENT'
 PARTITION BY LIST  COLUMNS(`id`)
(PARTITION `p1` VALUES IN (1,2,3) COMMENT = 'PARTITION P1' ENGINE = InnoDB,
 PARTITION `p2` VALUES IN (4,5,6) COMMENT = 'PARTITION P2' ENGINE = InnoDB,
 PARTITION `p3` VALUES IN (7,8,9) COMMENT = 'PARTITION P3' ENGINE = InnoDB);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_comment_mismatch`
--

LOCK TABLES `t_comment_mismatch` WRITE;
/*!40000 ALTER TABLE `t_comment_mismatch` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_comment_mismatch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_data_lower_upper`
--

DROP TABLE IF EXISTS `t_data_lower_upper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_data_lower_upper` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_data_lower_upper`
--

LOCK TABLES `t_data_lower_upper` WRITE;
/*!40000 ALTER TABLE `t_data_lower_upper` DISABLE KEYS */;
INSERT INTO `t_data_lower_upper` VALUES (1,'MAN A');
/*!40000 ALTER TABLE `t_data_lower_upper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_diff`
--

DROP TABLE IF EXISTS `t_diff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_diff` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_diff`
--

LOCK TABLES `t_diff` WRITE;
/*!40000 ALTER TABLE `t_diff` DISABLE KEYS */;
INSERT INTO `t_diff` VALUES (1,'a'),(3,'c');
/*!40000 ALTER TABLE `t_diff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_diff_generated`
--

DROP TABLE IF EXISTS `t_diff_generated`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_diff_generated` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `age` int(11) GENERATED ALWAYS AS (`id` * 100) VIRTUAL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_diff_generated`
--

LOCK TABLES `t_diff_generated` WRITE;
/*!40000 ALTER TABLE `t_diff_generated` DISABLE KEYS */;
INSERT INTO `t_diff_generated` VALUES (1,'a',100),(3,'c',300);
/*!40000 ALTER TABLE `t_diff_generated` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_diff_generated_stored`
--

DROP TABLE IF EXISTS `t_diff_generated_stored`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_diff_generated_stored` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `age` int(11) GENERATED ALWAYS AS (`id` * 100) STORED,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_diff_generated_stored`
--

LOCK TABLES `t_diff_generated_stored` WRITE;
/*!40000 ALTER TABLE `t_diff_generated_stored` DISABLE KEYS */;
INSERT INTO `t_diff_generated_stored` VALUES (1,'a',100),(3,'c',300);
/*!40000 ALTER TABLE `t_diff_generated_stored` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_dup_unique`
--

DROP TABLE IF EXISTS `t_dup_unique`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_dup_unique` (
  `id` int(11) NOT NULL,
  `uid` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_dup_unique`
--

LOCK TABLES `t_dup_unique` WRITE;
/*!40000 ALTER TABLE `t_dup_unique` DISABLE KEYS */;
INSERT INTO `t_dup_unique` VALUES (1,'a'),(2,'c');
/*!40000 ALTER TABLE `t_dup_unique` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_foreign_had`
--

DROP TABLE IF EXISTS `t_foreign_had`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_foreign_had` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_foreign_had`
--

LOCK TABLES `t_foreign_had` WRITE;
/*!40000 ALTER TABLE `t_foreign_had` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_foreign_had` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_foreign_has`
--

DROP TABLE IF EXISTS `t_foreign_has`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_foreign_has` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `r_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `r_id` (`r_id`),
  CONSTRAINT `t_foreign_has_ibfk_1` FOREIGN KEY (`r_id`) REFERENCES `t_foreign_had` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_foreign_has`
--

LOCK TABLES `t_foreign_has` WRITE;
/*!40000 ALTER TABLE `t_foreign_has` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_foreign_has` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_has_default_seq`
--

DROP TABLE IF EXISTS `t_has_default_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_has_default_seq` (
  `id` int(11) NOT NULL DEFAULT nextval(`base`.`s_seq`),
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_has_default_seq`
--

LOCK TABLES `t_has_default_seq` WRITE;
/*!40000 ALTER TABLE `t_has_default_seq` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_has_default_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_has_trigger`
--

DROP TABLE IF EXISTS `t_has_trigger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_has_trigger` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_has_trigger`
--

LOCK TABLES `t_has_trigger` WRITE;
/*!40000 ALTER TABLE `t_has_trigger` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_has_trigger` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 trigger t_has_trigger_insert_trigger after insert on t_has_trigger for each row insert into t_trigger_result values(now(), 'insert') */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 trigger t_has_trigger_update_trigger after insert on t_has_trigger for each row insert into t_trigger_result values(now(), 'update') */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 trigger t_has_trigger_delete_trigger after insert on t_has_trigger for each row insert into t_trigger_result values(now(), 'delete') */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `t_has_trigger_mismatch`
--

DROP TABLE IF EXISTS `t_has_trigger_mismatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_has_trigger_mismatch` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_has_trigger_mismatch`
--

LOCK TABLES `t_has_trigger_mismatch` WRITE;
/*!40000 ALTER TABLE `t_has_trigger_mismatch` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_has_trigger_mismatch` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 trigger t_has_trigger_mismatch_insert_trigger after insert on t_has_trigger_mismatch for each row insert into t_trigger_result values(now(), 'insert') */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `t_invalid_charset`
--

DROP TABLE IF EXISTS `t_invalid_charset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_invalid_charset` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_invalid_charset`
--

LOCK TABLES `t_invalid_charset` WRITE;
/*!40000 ALTER TABLE `t_invalid_charset` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_invalid_charset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_lower_UPPER`
--

DROP TABLE IF EXISTS `t_lower_UPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_lower_UPPER` (
  `Id` int(11) NOT NULL,
  `NaMe` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_lower_UPPER`
--

LOCK TABLES `t_lower_UPPER` WRITE;
/*!40000 ALTER TABLE `t_lower_UPPER` DISABLE KEYS */;
INSERT INTO `t_lower_UPPER` VALUES (1,'a');
/*!40000 ALTER TABLE `t_lower_UPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_movie`
--

DROP TABLE IF EXISTS `t_movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_movie` (
  `movie_id` bigint(20) unsigned NOT NULL COMMENT '動画ID',
  `title` mediumtext NOT NULL COMMENT 'タイトル',
  `detail` longtext NOT NULL COMMENT '詳細',
  `in_date` datetime NOT NULL COMMENT '登録日時',
  `up_date` datetime NOT NULL COMMENT '更新日時',
  PRIMARY KEY (`movie_id`),
  KEY `k1` (`up_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='動画';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_movie`
--

LOCK TABLES `t_movie` WRITE;
/*!40000 ALTER TABLE `t_movie` DISABLE KEYS */;
INSERT INTO `t_movie` VALUES (1,'動画1','詳細詳細詳細詳細詳細詳細詳細','2020-10-01 00:00:00','2020-10-10 00:00:00'),(2,'動画2','詳細詳細詳細詳細詳細詳細詳細','2020-10-11 00:00:00','2020-10-20 00:00:00'),(4,'動画4','詳細詳細詳細詳細詳細詳細詳細','2020-11-01 00:00:00','2020-11-10 00:00:00'),(5,'動画5','詳細詳細詳細詳細詳細詳細詳細詳細','2020-11-11 00:00:00','2020-11-30 00:00:00');
/*!40000 ALTER TABLE `t_movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_myisam`
--

DROP TABLE IF EXISTS `t_myisam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_myisam` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_myisam`
--

LOCK TABLES `t_myisam` WRITE;
/*!40000 ALTER TABLE `t_myisam` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_myisam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_no_primary`
--

DROP TABLE IF EXISTS `t_no_primary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_no_primary` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_no_primary`
--

LOCK TABLES `t_no_primary` WRITE;
/*!40000 ALTER TABLE `t_no_primary` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_no_primary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_no_primary_system_versioned`
--

DROP TABLE IF EXISTS `t_no_primary_system_versioned`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_no_primary_system_versioned` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 WITH SYSTEM VERSIONING;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_no_primary_system_versioned`
--

LOCK TABLES `t_no_primary_system_versioned` WRITE;
/*!40000 ALTER TABLE `t_no_primary_system_versioned` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_no_primary_system_versioned` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_ok`
--

DROP TABLE IF EXISTS `t_ok`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_ok` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_ok`
--

LOCK TABLES `t_ok` WRITE;
/*!40000 ALTER TABLE `t_ok` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_ok` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_only_base`
--

DROP TABLE IF EXISTS `t_only_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_only_base` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_only_base`
--

LOCK TABLES `t_only_base` WRITE;
/*!40000 ALTER TABLE `t_only_base` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_only_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_partition`
--

DROP TABLE IF EXISTS `t_partition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_partition` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
 PARTITION BY LIST  COLUMNS(`id`)
(PARTITION `p1` VALUES IN (1,2,3) ENGINE = InnoDB,
 PARTITION `p2` VALUES IN (4,5,6) ENGINE = InnoDB,
 PARTITION `p` VALUES IN (7,8,9) ENGINE = InnoDB);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_partition`
--

LOCK TABLES `t_partition` WRITE;
/*!40000 ALTER TABLE `t_partition` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_partition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_partition_mismatch`
--

DROP TABLE IF EXISTS `t_partition_mismatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_partition_mismatch` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
 PARTITION BY LIST  COLUMNS(`id`)
(PARTITION `p1` VALUES IN (1,2,3) ENGINE = InnoDB,
 PARTITION `p2` VALUES IN (4,5,6) ENGINE = InnoDB,
 PARTITION `p` VALUES IN (7,8,9) ENGINE = InnoDB);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_partition_mismatch`
--

LOCK TABLES `t_partition_mismatch` WRITE;
/*!40000 ALTER TABLE `t_partition_mismatch` DISABLE KEYS */;
INSERT INTO `t_partition_mismatch` VALUES (1,'a'),(4,'d');
/*!40000 ALTER TABLE `t_partition_mismatch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_partition_sub`
--

DROP TABLE IF EXISTS `t_partition_sub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_partition_sub` (
  `id` int(11) NOT NULL,
  `birthday` date NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`,`birthday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
 PARTITION BY RANGE (year(`birthday`))
SUBPARTITION BY HASH (to_days(`birthday`))
SUBPARTITIONS 2
(PARTITION `p1` VALUES LESS THAN (2019) ENGINE = InnoDB,
 PARTITION `p2` VALUES LESS THAN (2020) ENGINE = InnoDB);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_partition_sub`
--

LOCK TABLES `t_partition_sub` WRITE;
/*!40000 ALTER TABLE `t_partition_sub` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_partition_sub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_system_versioned`
--

DROP TABLE IF EXISTS `t_system_versioned`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_system_versioned` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 WITH SYSTEM VERSIONING;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_system_versioned`
--

LOCK TABLES `t_system_versioned` WRITE;
/*!40000 ALTER TABLE `t_system_versioned` DISABLE KEYS */;
INSERT INTO `t_system_versioned` VALUES (1,'A');
/*!40000 ALTER TABLE `t_system_versioned` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_system_versioned_invisible`
--

DROP TABLE IF EXISTS `t_system_versioned_invisible`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_system_versioned_invisible` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 WITH SYSTEM VERSIONING;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_system_versioned_invisible`
--

LOCK TABLES `t_system_versioned_invisible` WRITE;
/*!40000 ALTER TABLE `t_system_versioned_invisible` DISABLE KEYS */;
INSERT INTO `t_system_versioned_invisible` VALUES (1,'A');
/*!40000 ALTER TABLE `t_system_versioned_invisible` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_system_versioned_mismatch`
--

DROP TABLE IF EXISTS `t_system_versioned_mismatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_system_versioned_mismatch` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 WITH SYSTEM VERSIONING;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_system_versioned_mismatch`
--

LOCK TABLES `t_system_versioned_mismatch` WRITE;
/*!40000 ALTER TABLE `t_system_versioned_mismatch` DISABLE KEYS */;
INSERT INTO `t_system_versioned_mismatch` VALUES (1,'A');
/*!40000 ALTER TABLE `t_system_versioned_mismatch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_system_versioned_not_diff`
--

DROP TABLE IF EXISTS `t_system_versioned_not_diff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_system_versioned_not_diff` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 WITH SYSTEM VERSIONING;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_system_versioned_not_diff`
--

LOCK TABLES `t_system_versioned_not_diff` WRITE;
/*!40000 ALTER TABLE `t_system_versioned_not_diff` DISABLE KEYS */;
INSERT INTO `t_system_versioned_not_diff` VALUES (1,'A'),(2,'B');
/*!40000 ALTER TABLE `t_system_versioned_not_diff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_system_versioned_visible`
--

DROP TABLE IF EXISTS `t_system_versioned_visible`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_system_versioned_visible` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  `start_timestamp` timestamp(6) GENERATED ALWAYS AS ROW START,
  `end_timestamp` timestamp(6) GENERATED ALWAYS AS ROW END,
  PRIMARY KEY (`id`,`end_timestamp`),
  PERIOD FOR SYSTEM_TIME (`start_timestamp`, `end_timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 WITH SYSTEM VERSIONING;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_system_versioned_visible`
--

LOCK TABLES `t_system_versioned_visible` WRITE;
/*!40000 ALTER TABLE `t_system_versioned_visible` DISABLE KEYS */;
INSERT INTO `t_system_versioned_visible` VALUES (1,'A','2021-07-26 15:02:05.594013','2038-01-19 03:14:07.999999');
/*!40000 ALTER TABLE `t_system_versioned_visible` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_trigger_result`
--

DROP TABLE IF EXISTS `t_trigger_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_trigger_result` (
  `id` datetime NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_trigger_result`
--

LOCK TABLES `t_trigger_result` WRITE;
/*!40000 ALTER TABLE `t_trigger_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_trigger_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_utf8_diff`
--

DROP TABLE IF EXISTS `t_utf8_diff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_utf8_diff` (
  `id` int(11) NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_utf8_diff`
--

LOCK TABLES `t_utf8_diff` WRITE;
/*!40000 ALTER TABLE `t_utf8_diff` DISABLE KEYS */;
INSERT INTO `t_utf8_diff` VALUES (1,'A'),(2,'B');
/*!40000 ALTER TABLE `t_utf8_diff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `t_view_ok`
--

DROP TABLE IF EXISTS `t_view_ok`;
/*!50001 DROP VIEW IF EXISTS `t_view_ok`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `t_view_ok` (
  `id` tinyint NOT NULL,
  `name` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_wrong_auto_increment`
--

DROP TABLE IF EXISTS `t_wrong_auto_increment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_wrong_auto_increment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_wrong_auto_increment`
--

LOCK TABLES `t_wrong_auto_increment` WRITE;
/*!40000 ALTER TABLE `t_wrong_auto_increment` DISABLE KEYS */;
INSERT INTO `t_wrong_auto_increment` VALUES (1,'a');
/*!40000 ALTER TABLE `t_wrong_auto_increment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `base`
--

USE `base`;

--
-- Final view structure for view `t_view_ok`
--

/*!50001 DROP TABLE IF EXISTS `t_view_ok`*/;
/*!50001 DROP VIEW IF EXISTS `t_view_ok`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `t_view_ok` AS select `t_ok`.`id` AS `id`,`t_ok`.`name` AS `name` from `t_ok` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-27  0:07:29
