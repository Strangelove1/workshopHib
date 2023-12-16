<%--
  Created by IntelliJ IDEA.
  User: riain
  Date: 14.12.2023
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var rowCount = 1;

            $("#addIngredient").click(function () {
                var newRow = '<tr>' +
                    '<td><form:label path="ingredientsList' + rowCount + '"> List of ingredients </form:label></td>' +
                    '<td><form:select path="ingredientsList' + rowCount + '" items="${ingredients}" itemLabel="name" itemValue="id">  </form:select></td>' +
                    '</tr>';
                $("#ingredientTable").append(newRow);
                rowCount++;
            });
        });
    </script>
</head>
<body>
<form:form method="post" modelAttribute="meal">
    <table id="ingredientTable">
        <tr>
            <td><form:label path="name"> Name </form:label></td>
            <td><form:input path="name"/></td>
            <td><form:errors path="name"/></td>
        </tr>
        <tr>
            <td><form:label path="description"> Description </form:label></td>
            <td><form:input path="description"/></td>
            <td><form:errors path="description"/></td>
        </tr>

        <tr>
            <td><form:label path="ingredientsList"> List of ingredients </form:label></td>
            <td><form:select path="ingredientsList" items="${ingredients}" itemLabel="name" itemValue="id">  </form:select></td>
        </tr>
    </table>
    <button type="button" id="addIngredient">Add another ingredient</button>
    <input type="submit" value="submit">
</form:form>

</body>
</html>