<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.blog.message.model.*"%>
<%-- �����m�߱ĥ� EL ���g�k���� --%>

<%
	String post_id = (String) session.getAttribute("post_id");
    MessageService service = new MessageService();
    List<MessageVO> list = service.getByPostId(post_id);
    pageContext.setAttribute("list", list);
%>


<html>
<head>
<title>�Y�g�峹���d����� - listAllByPostId.jsp</title>

<style>
  table#table-1 {
	background-color: #CCCCFF;
    border: 2px solid black;
    text-align: center;
  }
  table#table-1 h4 {
    color: red;
    display: block;
    margin-bottom: 1px;
  }
  h4 {
    color: blue;
    display: inline;
  }
</style>

<style>
  table {
	width: 800px;
	background-color: white;
	margin-top: 5px;
	margin-bottom: 5px;
  }
  table, th, td {
    border: 1px solid #CCCCFF;
  }
  th, td {
    padding: 5px;
    text-align: center;
  }
</style>

</head>
<body bgcolor='white'>

<h4>�����m�߱ĥ� EL ���g�k����:</h4>
<table id="table-1">
	<tr><td>
		 <h3>�Y�g�峹���d����� - listAllByPostId.jsp</h3>
		 <h4><a href="<%=request.getContextPath()%>/front-end/blog/Message/select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">�^����</a></h4>
	</td></tr>
</table>

<%-- ���~���C --%>
<c:if test="${not empty errorMsgs}">
	<font style="color:red">�Эץ��H�U���~:</font>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message}</li>
		</c:forEach>
	</ul>
</c:if>

<table>
	<tr>
		<th>�d��id</th>
		<th>�|��id</th>
		<th>�峹id</th>
		<th>�d�����e</th>
		<th>���`�ɶ�</th>
		<th>��s�ɶ�</th>
	</tr>
	<c:forEach var="messageVO" items="${list}">
		
		<tr>
			<td>${messageVO.message_id}</td>
			<td>${messageVO.member_id}</td>
			<td>${messageVO.post_id}</td>
			<td>${messageVO.message_content}</td>
			<td>${messageVO.create_time}</td>
			<td>${messageVO.update_time}</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Message/Message.do" style="margin-bottom: 0px;">
			     <input type="submit" value="�R��">
			     <input type="hidden" name="message_id"  value="${messageVO.message_id}">
			     <input type="hidden" name="action" value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>

</body>
</html>