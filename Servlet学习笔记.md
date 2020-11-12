# Servlet学习笔记

## Servlet介绍

### 什么是Servlet？

Servlet是一个运行于Web服务器的Java程序，用于接受和响应客户端的http请求。

主要是配合动态资源使用，当然静态资源也需要使用servlet访问，只不过Tomcat里面已经定义好了一个DefaultServlet。

### Servlet入门

#### 1.创建Servlet类

创建一个普通类，实现Servlet接口

#### 2.配置Servlet

在web.xml中配置Servlet

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xmlns="http://java.sun.com/xml/ns/javaee" 
         xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" 
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" 
         id="WebApp_ID" version="2.5">
         <!-- 名称需要和项目名一致 -->
         <display-name>servlet_practice</display-name>
         
         <!-- 向Tomcat注册Servlet servlet-name：Servlet的名称， servlet-class：具体的路径 -->
         <servlet>
             <servlet-name>TestServlet</servlet-name>
             <servlet-class>cn.liuwei.servlet.TestServlet</servlet-class>
         </servlet>
         
         <!-- 注册servlet的映射。 servlet-name：找到上面注册的servlet， url-pattern：地址栏的路径 -->
         <servlet-mapping>
             <servlet-name>TestServlet</servlet-name>
             <url-pattern>/testServlet</url-pattern>
         </servlet-mapping>

</web-app>
```

#### 3.运行web项目

### Servlet的执行过程

当使用url进行对Tomcat请求时，访问流程如下所示

1. 先找到Tomcat
2. 根据url中的名称找到具体的项目
3. 找到项目下的web.xml，然后寻找url-pattern，看有没有哪个pattern对应url中的值
4. 找到servlet-mapping中的servlet-name
5. 找到前面注册的servlet-name
6. 根据servlet-class中的值找到对应的Servlet类并创建实例
7. 执行Servlet中的方法

### Servlet的通用写法

### Servlet的生命周期

#### 生命周期

Servlet从创建到销毁的一段时间：

1. 初始化：一个Servlet只会初始化一次

#### 生命周期方法

Servlet从创建到销毁期间调用的方法：

1. init：初始化方法，在创建servlet的实例时调用该方法。默认情况下，初次访问servlet才会创建实例
2. service：当有请求到servlet时调用该方法。该方法可以被执行多次
3. destroy：servlet销毁时调用该方法

#### Servlet提前执行init方法

默认情况地下，只有在初次访问Servlet时才会执行init方法。有的时候，我们可能需要在这个方法里面执行一些初始化工作，甚至是做一些比较耗时的逻辑。这个时候就需要让init方法提前执行，免得影响初次访问的体验。

想要让init方法提前执行，需要在web.xml文件中对需要提前执行的Servlet进行设置

```xml
<!-- 向Tomcat注册Servlet servlet-name：Servlet的名称， servlet-class：具体的路径 -->
         <servlet>
             <servlet-name>TestServlet</servlet-name>
             <servlet-class>cn.liuwei.servlet.TestServlet</servlet-class>
             <!-- 设置Servlet的init方法提前执行 -->
             <load-on-startup>2</load-on-startup>
         </servlet>
```

load-on-startup的值越小，提前执行的优先级越高。并且一般不设置为1，因为1是Tomcat的的Servlet的优先级

### ServletConfig

ServletConfig用于获取Servlet的配置信息

```java
//1.获取ServletConfig对象, 获取的是用于配置的信息
ServletConfig config = this.getServletConfig();
```

ServletConfig类中有4个方法：

- getServletName：获取Servlet的配置名称
- getInitParameter("name")：获取Servlet配置时的某个参数的值

```java
String initName = config.getInitParameter("name");
```

参数在web.xml文件中配置

```xml
<servlet>
     <servlet-name>HelloServletConfig</servlet-name>
     <servlet-class>cn.liuwei.servlet.HelloServletConfig</servlet-class>
             
     <init-param>
         <param-name>name</param-name>
         <param-value>Andrew</param-value>
     </init-param>
 </servlet>
```

- getInitParameterNames：获取Servlet配置时的所有参数的值

```java
Enumeration<String> names = config.getInitParameterNames();
```

#### 为什么需要ServletConfig？

当jar包的编写者需要一个由jar包的使用者定义的变量时，可以提前在Servlet代码中用ServletConfig获取变量，然后让使用者在web.xml文件中定义这个变量的值

### ServletContext

> 每个Web应用只有一个ServletContext对象，即不管在哪个Servlet中，他们获取到的ServletContext对象都是同一个

通过getServletContext()方法获取ServletContext对象

```java
ServletContext context = getServletContext();
```

#### ServletContext的作用

##### 1.可以获取全局配置参数

在web.xml中配置全局参数

```xml
 <context-param>
     <param-name>address</param-name>
     <param-value>上海杨浦</param-value>
</context-param>
```

通过getInitParameter方法获取全局参数

##### 2.获取web应用中的资源

通过getRealPath方法获取给定文件的绝对路径

```java
String path = context.getRealPath("");
```

通过getResourceAsStream方法获取Web工程下的资源，并转换成流对象

```java
//使用context的getResourceAsStream方法时的根路径是Tomcat中的项目文件的路径
InputSream is = context.getResourceAsStream("");
```

##### 3.使用ClassLoader获取资源文件

```java
//获取该Java文件的class类，然后获取到加载这个class到虚拟机中的那个类加载器对象
//通过这个方法的根路径是项目文件下的WEB-INF/classes
InputStream is = this.getClass().getClassLoader().getResourceAsStream("");
```

##### 4.使用ServletContext存取数据

##### 5.ServletContext的生命周期

服务器启动时，会为托管的每一个web应用程序创建一个ServletContext对象

从服务器移除托管，或者关闭服务器是会销毁ServletContext对象

##### 6.ServletContext的作用范围

同一个项目的ServletContext对象是一致的