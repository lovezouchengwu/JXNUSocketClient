/*
Navicat MySQL Data Transfer

Source Server         : root@localhost
Source Server Version : 50163
Source Host           : localhost:3306
Source Database       : ceshi

Target Server Type    : MYSQL
Target Server Version : 50163
File Encoding         : 65001

Date: 2012-11-04 21:53:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `jqconcentration`
-- ----------------------------
DROP TABLE IF EXISTS `jqconcentration`;
CREATE TABLE `nodeinfo` (
  `ID` int(255) NOT NULL AUTO_INCREMENT,
  `NetID` int(255) DEFAULT NULL,
  `NodeID` int(255) DEFAULT NULL,
  `Nowdate` date DEFAULT NULL,
  `Nowtime` time DEFAULT NULL,
  `JQ` varchar(255) DEFAULT NULL,
  `checkresult` varchar(255) DEFAULT NULL,
  `isConnected` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of jqconcentration
-- ----------------------------
INSERT INTO `nodeinfo` VALUES ('1', '100', '3001', '2012-11-04', '20:38:29', '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('2', '100', '3002', '2012-11-04', '20:38:31', '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('3', '100', '3003', '2012-11-04', null, '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('4', '101', '3004', '2012-11-04', null, '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('5', '101', '3005', '2012-11-04', null, '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('6', '101', '3006', '2012-11-04', null, '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('7', '102', '3007', null, null, '0.206ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('8', '102', '3008', null, null, '0.206ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('9', '102', '3009', null, null, '0.306ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('10', '102', '3010', null, null, '0.498ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('11', '100', '3001', null, null, '0ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('12', '100', '3002', null, null, '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('13', '100', '3003', null, null, '0ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('14', '101', '3004', null, null, '0.206ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('15', '101', '3005', null, null, '0ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('16', '101', '3006', null, null, '0ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('17', '101', '3007', null, null, '0ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('18', '101', '3008', null, null, '0ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('19', '101', '3009', null, null, '0.498ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('20', '101', '3010', null, null, '0.498ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('21', '101', '3001', null, null, '0.498ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('22', '101', '3002', null, null, '0.306ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('23', '101', '3003', null, null, '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('24', '101', '3004', null, null, '0.100ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('25', '101', '3005', null, null, '0.206ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('26', '101', '3006', null, null, '0.498ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('27', '101', '3007', null, null, '0ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('28', '101', '3008', null, null, '0ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('29', '101', '3009', null, null, '0.306ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('30', '101', '3010', null, null, '0.206ppm', '环境达标', '是');
INSERT INTO `nodeinfo` VALUES ('31', '101', '3001', null, null, '0.100ppm', '环境达标', '是');


-- ----------------------------
-- Table structure for `node`
-- ----------------------------
DROP TABLE IF EXISTS `node`;
CREATE TABLE `node` (
  `ID` int(11) NOT NULL,
  `NetID` int(11) DEFAULT NULL,
  `NodeID` int(11) DEFAULT NULL,
  `NodeTypeID` int(11) DEFAULT NULL,
  `NodeLatitude` varchar(255) DEFAULT NULL,
  `NodeLongitude` varchar(255) DEFAULT NULL,
  `NodeAltitude` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of node
-- ----------------------------
INSERT INTO `node` VALUES ('1', '1', '3001', '33', '28.647152', '115.824001', '2000');
INSERT INTO `node` VALUES ('2', '1', '3002', '33', '28.647152', '115.824002', '2000');
INSERT INTO `node` VALUES ('3', '1', '3003', '33', '28.647152', '115.824003', '2000');
INSERT INTO `node` VALUES ('4', '2', '3004', '22', '28.647153', '115.824003', '2000');
INSERT INTO `node` VALUES ('5', '2', '3005', '22', '28.647151', '115.824002', '2000');
INSERT INTO `node` VALUES ('6', '2', '3006', '22', '28.647153', '115.824002', '2000');
INSERT INTO `node` VALUES ('7', '3', '3007', '33', '28.647154', '115.824004', '2000');
INSERT INTO `node` VALUES ('8', '3', '3008', '22', '28.647151', '115.824004', '2000');
INSERT INTO `node` VALUES ('9', '3', '3009', '22', '28.647151', '115.824003', '2000');
INSERT INTO `node` VALUES ('10', '3', '3010', '22', '28.647153', '115.824005', '2000');
-- ----------------------------
-- Table structure for `subnet`
-- ----------------------------
DROP TABLE IF EXISTS `subnet`;
CREATE TABLE `subnet` (
  `ID` int(11) NOT NULL,
  `netID` int(11) DEFAULT NULL,
  `IPAddress` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of subnet
-- ----------------------------
INSERT INTO `subnet` VALUES ('1', '301', '172.16.19.211');
INSERT INTO `subnet` VALUES ('2', '302', '172.16.19.211');
INSERT INTO `subnet` VALUES ('3', '303', '172.16.19.211');
