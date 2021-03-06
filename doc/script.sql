USE [master]
GO
/****** Object:  Database [huoli]    Script Date: 2018/11/16 1:12:20 ******/
CREATE DATABASE [huoli]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'wxj_test', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\wxj_test.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'wxj_test_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\wxj_test_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [huoli] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [huoli].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [huoli] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [huoli] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [huoli] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [huoli] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [huoli] SET ARITHABORT OFF 
GO
ALTER DATABASE [huoli] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [huoli] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [huoli] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [huoli] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [huoli] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [huoli] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [huoli] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [huoli] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [huoli] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [huoli] SET  DISABLE_BROKER 
GO
ALTER DATABASE [huoli] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [huoli] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [huoli] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [huoli] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [huoli] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [huoli] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [huoli] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [huoli] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [huoli] SET  MULTI_USER 
GO
ALTER DATABASE [huoli] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [huoli] SET DB_CHAINING OFF 
GO
ALTER DATABASE [huoli] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [huoli] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [huoli] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [huoli] SET QUERY_STORE = OFF
GO
USE [huoli]
GO
/****** Object:  Table [dbo].[data_original]    Script Date: 2018/11/16 1:12:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[data_original](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
	[create_user] [int] NULL,
	[del_flag] [varchar](2) NULL,
	[value1] [int] NULL,
	[value2] [int] NULL,
	[value3] [int] NULL,
	[value4] [int] NULL,
	[value5] [int] NULL,
	[value6] [int] NULL,
	[file_id] [int] NULL,
	[userid] [varchar](50) NULL,
	[month] [varchar](20) NULL,
	[total] [int] NULL,
	[mobile] [varchar](30) NULL,
	[name] [varchar](30) NULL,
 CONSTRAINT [PK_data_original] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[data_result]    Script Date: 2018/11/16 1:12:24 ******/
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
	[relation_type] [varchar](20) NULL,
	[value4] [int] NULL,
	[value5] [int] NULL,
	[value6] [int] NULL,
	[month] [varchar](50) NULL,
	[relation_id] [int] NULL,
	[total] [int] NULL,
	[person_nub] [int] NULL,
	[userid] [varchar](50) NULL,
 CONSTRAINT [PK_data_result] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[dept_relation]    Script Date: 2018/11/16 1:12:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[dept_relation](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[dept_type] [varchar](10) NOT NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
	[dept_id] [int] NULL,
 CONSTRAINT [PK_dept_relation] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ignore_groups]    Script Date: 2018/11/16 1:12:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ignore_groups](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[tagid] [int] NULL,
	[ignore_flag] [varchar](10) NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
 CONSTRAINT [PK_ignore_groups] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ignore_users]    Script Date: 2018/11/16 1:12:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ignore_users](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[userid] [varchar](50) NULL,
	[ignore_flag] [varchar](10) NULL,
	[create_date] [date] NULL,
	[edit_date] [date] NULL,
 CONSTRAINT [PK_ignore_users] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[msg_history]    Script Date: 2018/11/16 1:12:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[msg_history](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[userid] [varchar](50) NULL,
	[content] [text] NULL,
	[create_date] [date] NULL,
	[create_name] [varchar](50) NULL,
	[state] [varchar](20) NULL,
	[month] [varchar](30) NULL,
 CONSTRAINT [PK_msg_history] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[org_department]    Script Date: 2018/11/16 1:12:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[org_department](
	[id] [int] NOT NULL,
	[name] [varchar](50) NULL,
	[parentid] [int] NULL,
	[order_nub] [bigint] NULL,
 CONSTRAINT [PK_org_department] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[org_group]    Script Date: 2018/11/16 1:12:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[org_group](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[tagname] [varchar](50) NULL,
	[tagid] [int] NULL,
 CONSTRAINT [PK_org_group] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[org_relation]    Script Date: 2018/11/16 1:12:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[org_relation](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[userid] [varchar](50) NOT NULL,
	[relation_id] [int] NULL,
	[relation_type] [varchar](20) NULL,
 CONSTRAINT [PK_user_org_relation] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[sys_file]    Script Date: 2018/11/16 1:12:25 ******/
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
	[month] [varchar](20) NULL,
	[type] [varchar](10) NULL,
	[relation_type] [varchar](10) NULL,
 CONSTRAINT [PK_sys_file] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user_data]    Script Date: 2018/11/16 1:12:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_data](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[userid] [varchar](50) NULL,
	[pass_word] [varchar](50) NULL,
	[account] [varchar](50) NULL,
	[name] [varchar](50) NULL,
	[position] [varchar](50) NULL,
	[mobile] [varchar](50) NULL,
	[email] [varchar](50) NULL,
	[avatar] [varchar](100) NULL,
	[telephone] [varchar](50) NULL,
	[dept_id] [int] NULL,
	[tag_names] [varchar](100) NULL,
 CONSTRAINT [PK_user_data] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
USE [master]
GO
ALTER DATABASE [huoli] SET  READ_WRITE 
GO
