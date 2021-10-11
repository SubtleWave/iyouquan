## 项目介绍

该项目是基于vue的一整套（附带电商源码）在线模板编辑器，目的是能够让用户在线设计海报，图片及文字样式，并分享到朋友圈。

该项目参考了同类大型网站（如：mark 易企秀 稿定设计）的设计理念，将图片制作在线进行，简单易上手，节省了用户的时间与投入，随时随地可以进行编辑，具有轻便，易操作的有点


## 预览地址
https://iyouquan.capelabs.cn/

## 使用说明
## 只需要跑前端

## 前端说明
该项目本地运行依托于 **nginx** ， 请参考以下说明进行安装和运行：

### 0. 安装

下载 **nginx** [参考地址](http://nginx.org/en/download.html)，下载完成直接解压即可。

### 1. 配置

将dist目录下文件放到路径 **nginx文件夹/nginx-1.18.0 / html/ 项目文件** 中。



### 2. 启动

双击 **nginx.exe**，启动nginx。

### 3. 运行

直接在浏览器中 输入网址 **http://localhost/** 即可。

## 后台管理系统
https://iyouquan.capelabs.cn/malladmin/#/dashboard
### 0.nginx下启动
admindist 文件夹下文件放入 **nginx 下malladmin** 目录中
### 1.配置nginx.conf

```
   location /malladmin/ {
                   alias F:/tools/nginx-1.20.1/malladmin/;### 替换为自己的路径
                   index index.html index.htm;
                         }

```
### 2. 运行

直接在浏览器中 输入网址 **http://localhost/malladmin/ 即可。




## 后端说明
litemall 为后端项目
依赖工具：idea,maven,mysql,solr,redis
sql文件下为 数据库初始脚本（自带300套模板 此模板仅供学习使用，不可商用）
### 0. 初始化数据库
建立 **litemall** 数据库
### 1.数据库配置
litemall\litemall-db\src\main\resources\application-db.yml 
### 2.solr 以及 redis配置
litemall\litemall-core\src\main\resources\application-core.yml

### 3.启动
```
cd litemall
mvn -DskipTests clean
mvn -DskipTests install
cd litemall-all\target
java -jar litemall-all-0.1.0-exec.jar
```

注意：如果想前后端一起跑的话 需要注意一下几点
1.前端已经写死了 调用域名是：https://iyouquan.capelabs.cn/ 
所以需要修改本机hosts 
```
127.0.0.1 iyouquan.capelabs.cn 

```

2.且在nginx 中开启https 配置公私钥，教程如下：
Windows下Nginx配置SSL实现Https访问（包含证书生成）
https://www.cnblogs.com/vincent-li666/p/5851463.html

## 联系我们

项目处于刚发布阶段，会有一些不太成熟的地方，希望有想法的朋友提出宝贵的意见与建议。

请联系我们： ting289658745（vx） 或者 647622951(qq群)
