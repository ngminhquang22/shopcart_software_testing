Postman tests for ShopCart - Cart scenarios

Files:
- ShopCart_CartTests.postman_collection.json : Postman collection with requests and tests
- ShopCart_Environment.postman_environment.json : Postman environment variables

How to run:

1) Import into Postman
- Open Postman -> Import -> Choose Files -> select the collection and environment JSON files.
- Select the environment `ShopCart Local Env` in the top-right environment selector.
- Run the requests manually or use the Collection Runner.

2) Run via Newman (CLI)
- Install newman: `npm install -g newman`
- Run:

```bash
newman run tests/postman/ShopCart_CartTests.postman_collection.json -e tests/postman/ShopCart_Environment.postman_environment.json
```

Notes:
- Ensure backend is running at `http://localhost:8080` and frontend is not blocking CORS (backend CORS enabled).
- The collection expects `user-1` and product `nike-air-force-1-white` available in seed data.
- Some error codes may vary depending on backend implementation; tests assert general success or client error codes.
