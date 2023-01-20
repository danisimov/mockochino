{% include_relative doc-navigation-bar.md %}

**Type:** GET<br>
**Path:** /settings<br>
**Parameters:** NONE<br>
**Response:**<br>

```shell
{
  "result": true,
  "data": [
    {
      "uuid": "57d5f714-746d-4a42-af91-77a5825d095b",
      "delay": 0,
      "code": 200,
      "token": "",
      "response": {
        "result": true,
        "message": "echo"
      }
    },
    {
      "uuid": "74f7cd3c-2373-11ec-9621-0242ac130002",
      "delay": 0,
      "code": 404,
      "token": "",
      "response": {
        "result": false,
        "message": "echo"
      }
    }
  ]
}
```
