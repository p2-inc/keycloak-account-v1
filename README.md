# keycloak-account-v1

Attempt to recreate the java resources that were used in the account theme v1.

Requires changes to Keycloak 
- Issue: https://github.com/keycloak/keycloak/issues/22318
- PR: https://github.com/keycloak/keycloak/pull/22317

### install
Checkout https://github.com/p2-inc/keycloak/tree/xgp/account-resource-spi and
```bash
mvn clean install -DskipTests
export KC_VERSION=999.0.0-SNAPSHOT
cd quarkus/container/
cp ../dist/target/keycloak-$KC_VERSION.tar.gz .
docker build --build-arg KEYCLOAK_DIST=keycloak-$KC_VERSION.tar.gz --tag quay.io/keycloak/keycloak:$KC_VERSION .
```

### start
```bash
mvn clean install
docker compose up
```

### setup
Create realm `test`

Create a user

Set the account theme to `account-v1`

### try

go to http://localhost:8080/auth/realms/test/account/
