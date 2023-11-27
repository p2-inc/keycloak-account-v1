# keycloak-account-v1

This extension and theme brings back the `base` account theme that existed in Keycloak until before version 22. We were able to get a change into Keycloak version 23 that allows overriding of all of the Resources associated with serving the account console. This allows us to recreate the Java Resources and FTL theme files that were used in the account theme v1.

Changes to Keycloak that make this possible
- Issue: https://github.com/keycloak/keycloak/issues/22318
- PR: https://github.com/keycloak/keycloak/pull/22317

You can use this alone, but you will probably want to inherit from this theme in your own account theme if you are building a new one, or porting a theme that you had already built based on the "old" v1 theme. To do that, update your account themes' `theme.properties` file to include `parent=account-v1` and make sure the themes are redeployed on Keycloak.

## Install

Jars are distributed through Maven Central. You will either need to deploy the jar in your `providers/` dir, or build a "fat" jar that includes that jar an your custom theme code.

(https://repo1.maven.org/maven2/io/phasetwo/keycloak/keycloak-account-v1/)

## Try it

```bash
mvn clean install
docker compose up
```

1. Create realm `test`
2. Create a user
3. Set the account theme to `account-v1`
4. Go to http://localhost:8180/realms/test/account/
