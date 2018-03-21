<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>上传逻辑块</title>
</head>
<body>
<h2>前端上传示例</h2>
<hr/>
<form method="POST" enctype="multipart/form-data" action="/uploadJs">
    <p>文件：<input type="file" name="file" /></p>
    <p>&nbsp;</p>
    <p><input type="submit" value="上传" /></p>
</form>
</body>
</html>