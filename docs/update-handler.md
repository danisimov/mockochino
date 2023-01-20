{% include_relative doc-navigation-bar.md %}

**Type:** POST<br>
**Path:** /settings/\{\{uuid\}\}<br>
**Parameters:**<br>

```shell
(int) delay - set request processing delay in ms
(int) code - set status code for produced responses
(String) authToken - set expected token for Authorization header verification
(JSON object) response - set body for produced responses
```

**Response:**<br>

```shell
{
    "result": true
}
```
