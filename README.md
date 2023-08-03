# keycloak-account-v1

Attempt to recreate the java resources that were used in the account theme v1.

Resource path is `{realmBase}/account-v1`

### start
```
mvn clean install && docker compose up
```

### setup
Create realm `test`

Create a user

Update `account` and `account-console` clients:

1. Remove *Home URL*. Add a *Valid redirect URI*:
![image](https://github.com/xgp/keycloak-account-v1/assets/244253/b0c9fb5f-4375-4951-b8ce-f92956583c30)

2. Turn off PKCE (set to *Choose...*)
![image](https://github.com/xgp/keycloak-account-v1/assets/244253/b4333b9c-f238-4384-afd5-8707f716dd53)

### try

go to http://localhost:8080/auth/realms/test/account-v1/
