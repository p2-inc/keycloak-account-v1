package org.keycloak.services.resources.account;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.Config.Scope;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderEvent;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;
import jakarta.ws.rs.NotFoundException;

@JBossLog
@AutoService(RealmResourceProviderFactory.class)
public class AccountFormServiceFactory implements RealmResourceProviderFactory {

  public static final String ID = "account-v1";

  @Override
  public String getId() {
    return ID;
  }

  private ClientModel getAccountManagementClient(RealmModel realm) {
    ClientModel client = realm.getClientByClientId("account-v1");
    if (client == null || !client.isEnabled()) {
      log.debug("account management not enabled");
      throw new NotFoundException("account management not enabled");
    }
    return client;
  }

  @Override
  public RealmResourceProvider create(KeycloakSession session) {
    RealmModel realm = session.getContext().getRealm();
    ClientModel client = getAccountManagementClient(realm);
    EventBuilder event = new EventBuilder​(realm, session, session.getContext().getConnection());
    return new AccountFormService(session, client, event);
  }

  @Override
  public void init(Scope config) {}

  @Override
  public void postInit(KeycloakSessionFactory factory) {}

  @Override
  public void close() {}
}
