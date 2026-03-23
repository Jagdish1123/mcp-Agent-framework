# MCP Controller Postman Testing Guide

This document contains all the Postman configurations and JSON bodies you need to test the MCP application endpoints running on `http://localhost:8081`.

---

## 1. Check Controller Status
Verify that the `McpController` is active and running.

* **Method:** `GET`
* **URL:** `http://localhost:8081/mcp`
* **Headers:** None
* **Body:** None

---

## 2. Discover Available Tools
List all the tools that have been registered and are available to be dispatched.

* **Method:** `GET`
* **URL:** `http://localhost:8081/mcp/tools`
* **Headers:** None
* **Body:** None

---

## 3. Call a Tool (`POST /mcp/call`)
This endpoint accepts requests to execute tools. Below are the specific payload examples based on the tools that your application exposes.

* **Method:** `POST`
* **URL:** `http://localhost:8081/mcp/call`
* **Headers:**
  * `Content-Type`: `application/json`
* **Body:** (Select **raw** -> **JSON** in Postman)

### A. Testing `create_note`
```json
{
  "tool": "create_note",
  "args": {
    "title": "Welcome to MCP",
    "description": "This is a test note created from Postman."
  }
}
```

### B. Testing `list_notes`
```json
{
  "tool": "list_notes",
  "args": {}
}
```

### C. Testing `get_note`
*Copy an ID from the `list_notes` response and replace the dummy ID below.*
```json
{
  "tool": "get_note",
  "args": {
    "id": "YOUR-UUID-GOES-HERE"
  }
}
```

### D. Testing `update_note`
*Use a valid ID from your existing notes.*
```json
{
  "tool": "update_note",
  "args": {
    "id": "YOUR-UUID-GOES-HERE",
    "title": "Updated Title via Postman",
    "description": "This note has been modified to test the update functionality."
  }
}
```

### E. Testing `delete_note`
*Use a valid ID that you wish to delete.*
```json
{
  "tool": "delete_note",
  "args": {
    "id": "YOUR-UUID-GOES-HERE"
  }
}
```
