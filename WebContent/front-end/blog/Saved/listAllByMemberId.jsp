<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.blog.saved.model.*"%>
<%-- �����m�߱ĥ� EL ���g�k���� --%>

<%
	SavedVO savedVO = new SavedVO();
	String member_id = savedVO.getMember_id();
    SavedService service = new SavedService();
    List<SavedVO> list = service.getByMemberId(member_id);
    pageContext.setAttribute("list", list);
%>


<html>
<head>
<title>�Y�ӷ|�������ä峹 - listAllByMemberId.jsp</title>

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
		 <h3>�Y�ӷ|�������ä峹 - listAllByMemberId.jsp</h3>
		 <h4><a href="select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">�^����</a></h4>
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
		<th>���ä峹id</th>
		<th>�|��id</th>
		<th>�峹id</th>
		<th>���îɶ�</th>
	</tr>
	<c:forEach var="savedVO" items="${list}">
		
		<tr>
			<td>${savedVO.saved_post_id}</td>
			<td>${savedVO.member_id}</td>
			<td>${savedVO.post_id}</td>
			<td>${savedVO.create_time}</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Saved/Saved.do" style="margin-bottom: 0px;">
			     <input type="submit" value="�R��">
			     <input type="hidden" name="saved_post_id"  value="${savedVO.saved_post_id}">
			     <input type="hidden" name="action" value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>

</body>
</html>