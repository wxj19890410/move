USE [master]
GO
/****** Object:  Database [wxj_test]    Script Date: 2018/10/30 0:52:16 ******/
CREATE DATABASE [wxj_test]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'wxj_test', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\wxj_test.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'wxj_test_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\wxj_test_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [wxj_test] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [wxj_test].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [wxj_test] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [wxj_test] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [wxj_test] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [wxj_test] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [wxj_test] SET ARITHABORT OFF 
GO
ALTER DATABASE [wxj_test] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [wxj_test] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [wxj_test] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [wxj_test] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [wxj_test] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [wxj_test] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [wxj_test] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [wxj_test] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [wxj_test] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [wxj_test] SET  DISABLE_BROKER 
GO
ALTER DATABASE [wxj_test] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [wxj_test] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [wxj_test] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [wxj_test] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [wxj_test] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [wxj_test] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [wxj_test] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [wxj_test] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [wxj_test] SET  MULTI_USER 
GO
ALTER DATABASE [wxj_test] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [wxj_test] SET DB_CHAINING OFF 
GO
ALTER DATABASE [wxj_test] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [wxj_test] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [wxj_test] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [wxj_test] SET QUERY_STORE = OFF
GO
USE [wxj_test]
GO
/****** Object:  Table [dbo].[data_original]    Script Date: 2018/10/30 0:52:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[data_original](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
	[create_user] [int] NULL,
	[del_flag] [varchar](1) NULL,
	[value1] [int] NULL,
	[value2] [int] NULL,
	[value3] [int] NULL,
	[value4] [int] NULL,
	[value5] [int] NULL,
	[value6] [int] NULL,
	[file_id] [int] NULL,
	[open_id] [varchar](50) NOT NULL,
	[month] [char](10) NOT NULL,
 CONSTRAINT [PK_data_original] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[data_result]    Script Date: 2018/10/30 0:52:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[data_result](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
	[create_user] [int] NULL,
	[del_flag] [varchar](1) NULL,
	[value1] [int] NULL,
	[value2] [int] NULL,
	[value3] [int] NULL,
	[file_id] [int] NULL,
	[open_id] [varchar](50) NOT NULL,
	[value4] [int] NULL,
	[value5] [int] NULL,
	[value6] [int] NULL,
	[month] [char](10) NOT NULL,
 CONSTRAINT [PK_data_result] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[org_department]    Script Date: 2018/10/30 0:52:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[org_department](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[del_flag] [varchar](1) NULL,
	[create_user] [int] NULL,
	[edit_date] [date] NULL,
	[create_date] [date] NULL,
	[name] [varbinary](20) NOT NULL,
	[parent_id] [int] NULL,
	[d_level] [char](2) NOT NULL,
 CONSTRAINT [PK_org_department] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[org_group]    Script Date: 2018/10/30 0:52:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[org_group](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varbinary](50) NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
	[create_user] [int] NULL,
	[del_flag] [varchar](1) NULL,
 CONSTRAINT [PK_org_group] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[org_relation]    Script Date: 2018/10/30 0:52:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[org_relation](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
	[create_user] [int] NULL,
	[del_flag] [varchar](1) NULL,
	[open_id] [varchar](50) NOT NULL,
	[group_id] [int] NULL,
	[dept_id] [int] NULL,
 CONSTRAINT [PK_user_org_relation] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[sys_file]    Script Date: 2018/10/30 0:52:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[sys_file](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[del_flag] [varchar](1) NULL,
	[create_user] [int] NULL,
	[edit_date] [date] NULL,
	[create_date] [date] NULL,
	[path] [varchar](50) NULL,
	[ext] [varchar](10) NULL,
	[name] [varchar](50) NULL,
	[open_id] [varchar](50) NOT NULL,
	[month] [char](10) NOT NULL,
 CONSTRAINT [PK_sys_file] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user_data]    Script Date: 2018/10/30 0:52:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_data](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[del_flag] [varchar](1) NULL,
	[name] [varchar](20) NULL,
	[pass_word] [varchar](50) NULL,
	[account] [varchar](50) NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
	[create_user] [int] NULL,
	[open_id] [varchar](50) NOT NULL,
	[phone] [varchar](20) NULL,
 CONSTRAINT [PK_user_data] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
USE [master]
GO
ALTER DATABASE [wxj_test] SET  READ_WRITE 
GO
