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

@JBossLog
@AutoService(RealmResourceProviderFactory.class)
public class AccountFormServiceFactory implements RealmResourceProviderFactory {

  public static final String ID = "account-v1";

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public RealmResourceProvider create(KeycloakSession session) {
    RealmModel realm = session.getContext().getRealm();
    ClientModel client = session.getContext().getClient();
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
