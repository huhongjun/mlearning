<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html >
  <head>
   <base href="<%=basePath%>"> 
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=basePath %>static/jquery/jquery-1.8.2.min.js"></script> 
	<script type="text/javascript">
	$(function(){
		var dataStr='['+
		'{'+
		   '"memberId":"会员id",'+
			'"courseId":"课程id",'+
			'"courseProgress":"进度（长整型）",'+
			'"courseName":"课程名称"'+
		'},'+
		'{'+
			'"memberId":"会员id",'+
			'"courseId":"课程id",'+
			'"courseProgress":"进度（长整型）",'+
			'"courseName":"课程名称"'+
		'}'+
	']';
		 $.ajax({
			  url:"<%=basePath%>rest/courseRes/saveCourse",
			  type:"post",
			  cache:false,
			  async:true,
			  dataType:"text",
			  data:{jsonParam:dataStr},
			  success:function(data){
				    $("body").append(data);
	    	      
			  }
		  });
		
		
	});
	
	
	
	</script>
  </head>
  
  <body>
  	<a href="http://localhost:8080/mlearning/rest/courseRes/getCourseProgress/wlitin@zhijin.com">获得用户学习进度</a></td> 
 	
    
  </body>
</html>
