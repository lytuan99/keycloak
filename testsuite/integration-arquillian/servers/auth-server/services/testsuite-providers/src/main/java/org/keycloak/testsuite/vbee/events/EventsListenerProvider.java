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

package org.keycloak.testsuite.vbee.events;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerTransaction;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.testsuite.vbee.models.ExtendedEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author <a href="mailto:mstrukel@redhat.com">Marko Strukelj</a>
 */
public class EventsListenerProvider implements EventListenerProvider {

    private static final BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();
    private static final BlockingQueue<AdminEvent> adminEvents = new LinkedBlockingQueue<>();
    private final EventListenerTransaction tx = new EventListenerTransaction((event, includeRepre) -> adminEvents.add(event), events::add);
    private KeycloakSession keycloakSession;

    public EventsListenerProvider(KeycloakSession session) {
        session.getTransactionManager().enlistAfterCompletion(tx);
        keycloakSession = session;
    }

    @Override
    public void onEvent(Event event) {
        System.out.println("on Event: " + event.getType());
        RealmModel realm = keycloakSession.realms().getRealm(event.getRealmId());
        UserSessionModel userSessionModel = keycloakSession.sessions().getUserSession(realm, event.getSessionId());
        System.out.println("Auth Method: " + userSessionModel.getAuthMethod());
        System.out.println("Broker UserId: " + userSessionModel.getBrokerUserId());
        System.out.println("NOTES: " + userSessionModel.getNotes().toString());
        System.out.println("Broker sessionId: "+ userSessionModel.getBrokerSessionId());

        ExtendedEvent extendedEvent = new ExtendedEvent(event);

        if (event.getType() == EventType.UPDATE_PROFILE) {
            extendedEvent.handleUpdateUserEvent(event.getDetails());
            extendedEvent.executeSendingWebhook();
        }

        if(event.getType() == EventType.REGISTER ||
                event.getType() == EventType.LOGIN) {
            UserModel user = keycloakSession.users().getUserById(keycloakSession.realms().getRealm(event.getRealmId()), event.getUserId());
            String identityProvider = event.getDetails().get("identity_provider");

            extendedEvent.handleRegisterEvent(user.getAttributes(), identityProvider);
            extendedEvent.executeSendingWebhook();
        }

        if (event.getType() == EventType.REFRESH_TOKEN ||
                event.getType() == EventType.LOGOUT) {
            extendedEvent.setDetails(event.getDetails());
            extendedEvent.executeSendingWebhook();
        }

        if (event.getType() == EventType.TOKEN_EXCHANGE) {
            UserModel userModel = keycloakSession.sessions().getUserSession(realm, event.getSessionId()).getUser();
            String identityProvider = event.getDetails().get("subject_issuer");

            extendedEvent.handleExchangeTokenEvent(userModel.getAttributes(), userModel.getId(), identityProvider);
            extendedEvent.executeSendingWebhook();
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
//        tx.addAdminEvent(event, includeRepresentation);
        System.out.println("on Event admin");
    }

    @Override
    public void close() {

    }

    public static Event poll() {
        return events.poll();
    }

    public static AdminEvent pollAdminEvent() {
        return adminEvents.poll();
    }

    public static void clear() {
        events.clear();
    }

    public static void clearAdminEvents() {
        adminEvents.clear();
    }
}
