-- MySQL dump 10.13  Distrib 8.0.31, for Linux (x86_64)
--
-- Host: localhost    Database: compare
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `compare`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `compare` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `compare`;

--
-- Table structure for table `T_ALL_UPPER`
--

DROP TABLE IF EXISTS `T_ALL_UPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `T_ALL_UPPER` (
  `ID` int NOT NULL,
  `NAME` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_all_types` (
  `id` int NOT NULL,
  `c_tinyint_signed` tinyint DEFAULT NULL,
  `c_tinyint_unsigned` tinyint unsigned DEFAULT NULL,
  `c_tinyint_zerofill` tinyint(3) unsigned zerofill DEFAULT NULL,
  `c_smallint_signed` smallint DEFAULT NULL,
  `c_smallint_unsigned` smallint unsigned DEFAULT NULL,
  `c_smallint_zerofill` smallint(5) unsigned zerofill DEFAULT NULL,
  `c_mediumint_signed` mediumint DEFAULT NULL,
  `c_mediumint_unsigned` mediumint unsigned DEFAULT NULL,
  `c_mediumint_zerofill` mediumint(8) unsigned zerofill DEFAULT NULL,
  `c_int_signed` int DEFAULT NULL,
  `c_int_unsigned` int unsigned DEFAULT NULL,
  `c_int_zerofill` int(10) unsigned zerofill DEFAULT NULL,
  `c_bigint_signed` bigint DEFAULT NULL,
  `c_bigint_unsigned` bigint unsigned DEFAULT NULL,
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
  `c_char` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `c_varchar` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `c_tinytext` tinytext COLLATE utf8mb4_general_ci,
  `c_text` text COLLATE utf8mb4_general_ci,
  `c_mediumtext` mediumtext COLLATE utf8mb4_general_ci,
  `c_longtext` longtext COLLATE utf8mb4_general_ci,
  `c_enum` enum('a','b','c') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `c_set` set('A','B','C') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `c_json` json DEFAULT NULL,
  `c_date` date DEFAULT NULL,
  `c_time` time DEFAULT NULL,
  `c_datetime` datetime DEFAULT NULL,
  `c_timestamp` timestamp NULL DEFAULT NULL,
  `c_year` year DEFAULT NULL,
  `c_binary` binary(1) DEFAULT NULL,
  `c_varbinary` varbinary(16) DEFAULT NULL,
  `c_tinyblob` tinyblob,
  `c_blob` blob,
  `c_mediumblob` mediumblob,
  `c_longblob` longblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_all_types`
--

LOCK TABLES `t_all_types` WRITE;
/*!40000 ALTER TABLE `t_all_types` DISABLE KEYS */;
INSERT INTO `t_all_types` VALUES (1,127,255,010,32767,65535,00100,8388607,16777215,00001000,2147483647,4294967295,0000010000,9223372036854775807,18446744073709551615,00000000000000100000,1,2,0000000000,2.1,2.2,0000000000.2,3.1,3.2,00000000000000000000.3,_binary '','c','varchar','tinytext','text','mediumtext','longtext','a','A','{\"json\": 1}','2020-01-01','12:12:12','2020-01-01 12:12:12','2019-12-31 16:01:01',2020,_binary 'b',_binary 'varbinary',_binary 'tinyblob',_binary 'blob',_binary 'mediumblob',_binary 'longblob');
/*!40000 ALTER TABLE `t_all_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_article`
--

DROP TABLE IF EXISTS `t_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_article` (
  `article_id` bigint unsigned NOT NULL COMMENT '記事ID',
  `title` mediumtext COLLATE utf8mb4_general_ci NOT NULL COMMENT 'タイトル',
  `detail` longtext COLLATE utf8mb4_general_ci NOT NULL COMMENT '詳細',
  `in_date` datetime NOT NULL COMMENT '登録日時',
  `up_date` datetime NOT NULL COMMENT '更新日時',
  PRIMARY KEY (`article_id`),
  KEY `k1` (`up_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='記事';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_article`
--

LOCK TABLES `t_article` WRITE;
/*!40000 ALTER TABLE `t_article` DISABLE KEYS */;
INSERT INTO `t_article` VALUES (2,'記事2','詳細詳細詳細詳細詳細詳細詳細詳細','2020-10-11 00:00:00','2020-10-20 00:00:00'),(3,'記事3','詳細詳細詳細詳細詳細詳細詳細詳細','2020-10-21 00:00:00','2020-10-30 00:00:00'),(4,'記事4','詳細詳細詳細詳細詳細詳細詳細詳細','2020-11-01 00:00:00','2020-11-10 00:00:00'),(5,'記事5','詳細詳細詳細詳細詳細詳細詳細','2020-11-11 00:00:00','2020-11-20 00:00:00');
/*!40000 ALTER TABLE `t_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_blob_primary`
--

DROP TABLE IF EXISTS `t_blob_primary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_blob_primary` (
  `id` tinyblob NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`(16))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_blob_primary`
--

LOCK TABLES `t_blob_primary` WRITE;
/*!40000 ALTER TABLE `t_blob_primary` DISABLE KEYS */;
INSERT INTO `t_blob_primary` VALUES (_binary 'man a','MAN a');
/*!40000 ALTER TABLE `t_blob_primary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_comment_mismatch`
--

DROP TABLE IF EXISTS `t_comment_mismatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_comment_mismatch` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
/*!50500 PARTITION BY LIST  COLUMNS(id)
(PARTITION p1 VALUES IN (1,2,3) ENGINE = InnoDB,
 PARTITION p2 VALUES IN (4,5,6) ENGINE = InnoDB,
 PARTITION p3 VALUES IN (7,8,9) ENGINE = InnoDB) */;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_data_lower_upper` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_data_lower_upper`
--

LOCK TABLES `t_data_lower_upper` WRITE;
/*!40000 ALTER TABLE `t_data_lower_upper` DISABLE KEYS */;
INSERT INTO `t_data_lower_upper` VALUES (1,'MaN b');
/*!40000 ALTER TABLE `t_data_lower_upper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_diff`
--

DROP TABLE IF EXISTS `t_diff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_diff` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_diff`
--

LOCK TABLES `t_diff` WRITE;
/*!40000 ALTER TABLE `t_diff` DISABLE KEYS */;
INSERT INTO `t_diff` VALUES (1,'a'),(2,'b');
/*!40000 ALTER TABLE `t_diff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_diff_generated`
--

DROP TABLE IF EXISTS `t_diff_generated`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_diff_generated` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age` int GENERATED ALWAYS AS ((`id` * 100)) VIRTUAL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_diff_generated`
--

LOCK TABLES `t_diff_generated` WRITE;
/*!40000 ALTER TABLE `t_diff_generated` DISABLE KEYS */;
INSERT INTO `t_diff_generated` (`id`, `name`) VALUES (1,'a'),(2,'b');
/*!40000 ALTER TABLE `t_diff_generated` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_diff_generated_stored`
--

DROP TABLE IF EXISTS `t_diff_generated_stored`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_diff_generated_stored` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age` int GENERATED ALWAYS AS ((`id` * 100)) STORED,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_diff_generated_stored`
--

LOCK TABLES `t_diff_generated_stored` WRITE;
/*!40000 ALTER TABLE `t_diff_generated_stored` DISABLE KEYS */;
INSERT INTO `t_diff_generated_stored` (`id`, `name`) VALUES (1,'a'),(2,'b');
/*!40000 ALTER TABLE `t_diff_generated_stored` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_dup_unique`
--

DROP TABLE IF EXISTS `t_dup_unique`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dup_unique` (
  `id` int NOT NULL,
  `uid` varchar(16) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_dup_unique`
--

LOCK TABLES `t_dup_unique` WRITE;
/*!40000 ALTER TABLE `t_dup_unique` DISABLE KEYS */;
INSERT INTO `t_dup_unique` VALUES (1,'a'),(2,'b'),(3,'c');
/*!40000 ALTER TABLE `t_dup_unique` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_foreign_had`
--

DROP TABLE IF EXISTS `t_foreign_had`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_foreign_had` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_foreign_has` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `r_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `r_id` (`r_id`),
  CONSTRAINT `t_foreign_has_ibfk_1` FOREIGN KEY (`r_id`) REFERENCES `t_foreign_had` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_foreign_has`
--

LOCK TABLES `t_foreign_has` WRITE;
/*!40000 ALTER TABLE `t_foreign_has` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_foreign_has` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_has_trigger_mismatch`
--

DROP TABLE IF EXISTS `t_has_trigger_mismatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_has_trigger_mismatch` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_has_trigger_mismatch`
--

LOCK TABLES `t_has_trigger_mismatch` WRITE;
/*!40000 ALTER TABLE `t_has_trigger_mismatch` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_has_trigger_mismatch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_invalid_charset`
--

DROP TABLE IF EXISTS `t_invalid_charset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_invalid_charset` (
  `id` int NOT NULL,
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_lower_UPPER` (
  `Id` int NOT NULL,
  `NaMe` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_movie` (
  `movie_id` bigint unsigned NOT NULL COMMENT '動画ID',
  `title` mediumtext COLLATE utf8mb4_general_ci NOT NULL COMMENT 'タイトル',
  `detail` longtext COLLATE utf8mb4_general_ci NOT NULL COMMENT '詳細',
  `in_date` datetime NOT NULL COMMENT '登録日時',
  `up_date` datetime NOT NULL COMMENT '更新日時',
  PRIMARY KEY (`movie_id`),
  KEY `k1` (`up_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='動画';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_movie`
--

LOCK TABLES `t_movie` WRITE;
/*!40000 ALTER TABLE `t_movie` DISABLE KEYS */;
INSERT INTO `t_movie` VALUES (1,'動画1','詳細詳細詳細詳細詳細詳細詳細','2020-10-01 00:00:00','2020-10-10 00:00:00'),(2,'動画2','詳細詳細詳細詳細詳細詳細詳細','2020-10-11 00:00:00','2020-10-20 00:00:00'),(3,'動画3','詳細詳細詳細詳細詳細詳細詳細','2020-10-21 00:00:00','2020-10-30 00:00:00'),(5,'動画5','詳細詳細詳細詳細詳細詳細詳細','2020-11-11 00:00:00','2020-11-20 00:00:00');
/*!40000 ALTER TABLE `t_movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_myisam`
--

DROP TABLE IF EXISTS `t_myisam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_myisam` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
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
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_no_primary` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_no_primary`
--

LOCK TABLES `t_no_primary` WRITE;
/*!40000 ALTER TABLE `t_no_primary` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_no_primary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_only_compare`
--

DROP TABLE IF EXISTS `t_only_compare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_only_compare` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_only_compare`
--

LOCK TABLES `t_only_compare` WRITE;
/*!40000 ALTER TABLE `t_only_compare` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_only_compare` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_partition_mismatch`
--

DROP TABLE IF EXISTS `t_partition_mismatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_partition_mismatch` (
  `id` int NOT NULL,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_partition_mismatch`
--

LOCK TABLES `t_partition_mismatch` WRITE;
/*!40000 ALTER TABLE `t_partition_mismatch` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_partition_mismatch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_utf8_diff`
--

DROP TABLE IF EXISTS `t_utf8_diff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_utf8_diff` (
  `id` int NOT NULL,
  `name` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_utf8_diff`
--

LOCK TABLES `t_utf8_diff` WRITE;
/*!40000 ALTER TABLE `t_utf8_diff` DISABLE KEYS */;
INSERT INTO `t_utf8_diff` VALUES (1,'a'),(2,'b');
/*!40000 ALTER TABLE `t_utf8_diff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `t_view_ok`
--

DROP TABLE IF EXISTS `t_view_ok`;
/*!50001 DROP VIEW IF EXISTS `t_view_ok`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `t_view_ok` AS SELECT
 1 AS `id`,
 1 AS `name`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_wrong_auto_increment`
--

DROP TABLE IF EXISTS `t_wrong_auto_increment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_wrong_auto_increment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_wrong_auto_increment`
--

LOCK TABLES `t_wrong_auto_increment` WRITE;
/*!40000 ALTER TABLE `t_wrong_auto_increment` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_wrong_auto_increment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `compare`
--

USE `compare`;

--
-- Final view structure for view `t_view_ok`
--

/*!50001 DROP VIEW IF EXISTS `t_view_ok`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `t_view_ok` AS select 1 AS `id`,1 AS `name` */;
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

-- Dump completed on 2024-09-24 23:40:21
