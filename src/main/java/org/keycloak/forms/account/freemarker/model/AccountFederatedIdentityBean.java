/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.forms.account.freemarker.model;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.OrderedModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.resources.account.AccountFormService;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 * @author <a href="mailto:velias@redhat.com">Vlastimil Elias</a>
 */
public class AccountFederatedIdentityBean {

  private static OrderedModel.OrderedModelComparator<FederatedIdentityEntry>
      IDP_COMPARATOR_INSTANCE = new OrderedModel.OrderedModelComparator<>();

  private final List<FederatedIdentityEntry> identities;
  private final boolean removeLinkPossible;
  private final KeycloakSession session;

  public AccountFederatedIdentityBean(
      KeycloakSession session, RealmModel realm, UserModel user, URI baseUri, String stateChecker) {
    this.session = session;

    AtomicInteger availableIdentities = new AtomicInteger(0);
    this.identities =
        realm
            .getIdentityProvidersStream()
            .filter(IdentityProviderModel::isEnabled)
            .map(
                provider -> {
                  String providerId = provider.getAlias();

                  FederatedIdentityModel identity =
                      getIdentity(
                          session.users().getFederatedIdentitiesStream(realm, user), providerId);

                  if (identity != null) {
                    availableIdentities.getAndIncrement();
                  }

                  String displayName =
                      KeycloakModelUtils.getIdentityProviderDisplayName(session, provider);
                  return new FederatedIdentityEntry(
                      identity,
                      displayName,
                      provider.getAlias(),
                      provider.getAlias(),
                      provider.getConfig() != null ? provider.getConfig().get("guiOrder") : null);
                })
            .sorted(IDP_COMPARATOR_INSTANCE)
            .collect(Collectors.toList());

    // Removing last social provider is not possible if you don't have other possibility to
    // authenticate
    this.removeLinkPossible =
        availableIdentities.get() > 1
            || user.getFederationLink() != null
            || AccountFormService.isPasswordSet(session, realm, user);
  }

  private FederatedIdentityModel getIdentity(
      Stream<FederatedIdentityModel> identities, String providerId) {
    return identities
        .filter(
            federatedIdentityModel ->
                Objects.equals(federatedIdentityModel.getIdentityProvider(), providerId))
        .findFirst()
        .orElse(null);
  }

  public List<FederatedIdentityEntry> getIdentities() {
    return identities;
  }

  public boolean isRemoveLinkPossible() {
    return removeLinkPossible;
  }

  public static class FederatedIdentityEntry implements OrderedModel {

    private FederatedIdentityModel federatedIdentityModel;
    private final String providerId;
    private final String providerName;
    private final String guiOrder;
    private final String displayName;

    public FederatedIdentityEntry(
        FederatedIdentityModel federatedIdentityModel,
        String displayName,
        String providerId,
        String providerName,
        String guiOrder) {
      this.federatedIdentityModel = federatedIdentityModel;
      this.displayName = displayName;
      this.providerId = providerId;
      this.providerName = providerName;
      this.guiOrder = guiOrder;
    }

    public String getProviderId() {
      return providerId;
    }

    public String getProviderName() {
      return providerName;
    }

    public String getUserId() {
      return federatedIdentityModel != null ? federatedIdentityModel.getUserId() : null;
    }

    public String getUserName() {
      return federatedIdentityModel != null ? federatedIdentityModel.getUserName() : null;
    }

    public boolean isConnected() {
      return federatedIdentityModel != null;
    }

    @Override
    public String getGuiOrder() {
      return guiOrder;
    }

    public String getDisplayName() {
      return displayName;
    }
  }
}
