-- phpMyAdmin SQL Dump
-- version 4.0.10.10
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 10, 2015 at 01:14 PM
-- Server version: 5.1.73
-- PHP Version: 5.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `cscexitint_Fall13_smitra`
--

-- --------------------------------------------------------

--
-- Table structure for table `Account`
--

CREATE TABLE IF NOT EXISTS `Account` (
  `AccountNumber` int(5) NOT NULL AUTO_INCREMENT,
  `Type` enum('Checking','Savings') CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL DEFAULT 'Checking',
  `Balance` varchar(10) CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL,
  `ServiceCharge` varchar(10) CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL DEFAULT '0',
  `OwnerID` int(5) NOT NULL,
  PRIMARY KEY (`AccountNumber`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `Account`
--

INSERT INTO `Account` (`AccountNumber`, `Type`, `Balance`, `ServiceCharge`, `OwnerID`) VALUES
(1, 'Checking', '100', '0', 1),
(2, 'Savings', '5000', '0', 1),
(3, 'Checking', '200', '0', 2),
(4, 'Savings', '4000', '0', 2),
(5, 'Checking', '300', '0', 3),
(6, 'Savings', '3000', '0', 3),
(7, 'Checking', '400', '0', 4),
(8, 'Savings', '2000', '0', 4),
(9, 'Checking', '500', '0', 5),
(10, 'Savings', '1000', '0', 5);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
