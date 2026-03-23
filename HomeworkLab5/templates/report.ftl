<html>
<head>
    <title>Catalog Report</title>
</head>
<body>
    <h2>Lista Resurselor Bibliografice</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Titlu</th>
            <th>Locatie</th>
        </tr>
        <#list resources as res>
        <tr>
            <td>${res.id!"N/A"}</td>
            <td>${res.title!"Fara titlu"}</td>
            <td>${res.location!"Locatie lipsa"}</td>
        </tr>
        </#list>
    </table>
</body>
</html>