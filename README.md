# keycloak-account-v1

This extension and theme brings back the account theme that existed in Keycloak until before version 22. We were able to get a change into Keycloak version 23 that allows overriding of all of the Resources associated with serving the account console. This allows us to recreate the Java Resources and FTL theme files that were used in the account theme v1.

Changes to Keycloak that make this possible
- Issue: https://github.com/keycloak/keycloak/issues/22318
- PR: https://github.com/keycloak/keycloak/pull/22317

## Keycloak Versions

This module depends on Keycloak internals that may change without notice. Therefore, you must use different versions of this module based on the version of Keycloak you are using.

| Keycloak Version Range  | This Module Version |
|-------------------------|--------------------------------------------|
| Any version prior to 22 | You do not need this module |
| ~22.0.0                 | No solution for this Keycloak version |
| ~23.0.0                 | v0.3 - [.jar download link](https://github.com/keycloakify/keycloakify/releases/download/v0.0.1/keycloak-account-v1-0.3-SNAPSHOT.jar) |
| 24.0.0             | v0.4 - [.jar download link](https://github.com/keycloakify/keycloakify/releases/download/v0.0.1/keycloak-account-v1-0.4.jar) |
| 25.0.0 to ?             | v0.6 - [.jar download link](https://github.com/keycloakify/keycloakify/releases/download/v0.0.1/keycloak-account-v1-0.6.jar) |


## Updating Your Account Theme for Keycloak Version 23 and Beyond  

[Account console v1](https://www.keycloak.org/docs/latest/release_notes/index.html#account-console-v1-removal) have been removed from Keycloak in version 22.  
If you have a custom theme based on Account console v1 you can make it work with Keycloak 23 and up by follow these steps:  

1. Download and load the correct .jar of this module depending of your Keycloak version (see above).  
If you are unsure how to load a JAR extension into Keycloak, refer to [this guide](https://docs.keycloakify.dev/importing-your-theme-in-keycloak#bare-metal) for assistance.  

2. Modify your `theme.properties` as follows:  

If you where extending the base theme (`parent=base`) you just need to apply the following change and you're done.
You can skip part 3.  

`src/main/resources/theme/[your theme]/account/theme.properties`
```diff
- parent=base
+ parent=account-v1
```  

If you where extending the `keycloak` theme:  

`src/main/resources/theme/[your theme]/account/theme.properties`
```diff
- parent=keycloak
+ parent=account-v1


# If you had a styles property:
-styles=css/my-account.css
+styles=css/my-account.css img/icon-sidebar-active.png img/logo.png resources-common/node_modules/patternfly/dist/css/patternfly.min.css resources-common/node_modules/patternfly/dist/css/patternfly-additions.min.css resources-common/node_modules/patternfly/dist/css/patternfly-additions.min.css

# If you had no styles property
+styles=css/account.css img/icon-sidebar-active.png img/logo.png resources-common/node_modules/patternfly/dist/css/patternfly.min.css resources-common/node_modules/patternfly/dist/css/patternfly-additions.min.css resources-common/node_modules/patternfly/dist/css/patternfly-additions.min.css

# If you had no local properties
+locales=ar,ca,cs,da,de,en,es,fr,fi,hu,it,ja,lt,nl,no,pl,pt-BR,ru,sk,sv,tr,zh-CN

# If you had no kcButtonClass property:
+kcButtonClass=btn
# If you had no kcButtonPrimaryClass property:
+kcButtonPrimaryClass=btn-primary
# If you had no kcButtonDefaultClass property:
+kcButtonDefaultClass=btn-default
# If you had no kcButtonLargeClass property:
+kcButtonLargeClass=btn-lg
```  

3. Repatriate the assets and i18n resources that used to be provided in the `keycloak` account-v1 theme:  

```bash
cd src/main/resources/theme/[your theme]/account
git clone https://github.com/p2-inc/keycloak-account-v1
mkdir -p messages
# Be careful not to overwrite your own changes
cp keycloak-account-v1/assets/messages/* messages/
mkdir -p resources
# Be careful not to overwrite your own changes.
# If you had a custom `resources/account.css` give it another name, like `resources/my-account.css`
cp keycloak-account-v1/assets/resources/* resources/
rm -r keycloak-account-v1
```

## Devolvement

# Install

Jars are distributed through Maven Central. You will either need to deploy the jar in your `providers/` dir, or build a "fat" jar that includes that jar an your custom theme code.

(https://repo1.maven.org/maven2/io/phasetwo/keycloak/keycloak-account-v1/)

# Try it

```bash
mvn clean install
docker compose up
```

0. Connect to http://localhost:8180/ and login as `admin`/`admin`
1. Create realm `test`
2. Create a user
3. Set the account theme to `account-v1`
4. Go to http://localhost:8180/realms/test/account/